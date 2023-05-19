package com.example.components.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.components.Adapter.AttendanceAdapter
import com.example.components.Fragments.Dashboard.Companion.classData
import com.example.components.R
import com.example.components.api.ApiService
import com.example.components.databinding.FragmentTeacherAttendanceBinding
import com.example.components.model.AnnouncementResponse
import com.example.components.model.SendAttendanceList
import com.example.components.model.StudentAttendance
import com.example.components.utils.GetLocation
import com.example.components.utils.PermissionUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TeacherAttendanceFragment : Fragment() {

    lateinit var binding: FragmentTeacherAttendanceBinding
    private var presentList: ArrayList<String> = ArrayList<String>()
    private var isOpen: Boolean = false

    enum class ButtonStates {
        CLOSED, START, UPLOAD,WAITING
    }

    var buttonStates: ButtonStates = ButtonStates.START
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTeacherAttendanceBinding.inflate(layoutInflater)

        binding.attendanceRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        setClassPlaceholderData();

        return binding.root
    }

    private fun setClassPlaceholderData() {
        binding.classTitle.text = getString(
            R.string.subjectAndSemester,
            classData.Subject, classData.semester.toString()
        )
        binding.date.text = classData.Update_Date
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startEndBtn.setOnClickListener {

            if (buttonStates == ButtonStates.START) {
                if (PermissionUtil.requestLocationPermission(requireActivity())) {
                    GetLocation.getCurrentLocation(
                        requireContext(),
                        requireActivity()
                    ) { location ->
                        sendTeacherData(location)
                        getPresentStudentList()
                    }
                }
            }
            if (buttonStates == ButtonStates.CLOSED) {
                buttonStates = ButtonStates.START
                binding.startEndBtn.text = buttonStates.toString()
            }else if(buttonStates == ButtonStates.START){
                buttonStates = ButtonStates.WAITING
                binding.startEndBtn.text = buttonStates.toString()
            }else if(buttonStates == ButtonStates.UPLOAD){
                buttonStates = ButtonStates.CLOSED
                binding.startEndBtn.text = buttonStates.toString()
            }else if(buttonStates == ButtonStates.WAITING){
                buttonStates = ButtonStates.UPLOAD
                binding.startEndBtn.text = "Uploading..."
                uploadAttendance()
            }

        }


    }

    private fun sendTeacherData(currLocation: String) {
        val ref = FirebaseDatabase.getInstance()
            .getReference("Portal/${classData.semester}/${classData.Stream}").child(
                classData.Subject
            ).setValue(currLocation)
    }

    private fun getPresentStudentList() {
        val ref = FirebaseDatabase.getInstance()
            .getReference("Attendance/${classData.semester}/${classData.Stream}/${classData.Subject}/")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val studentList: ArrayList<StudentAttendance> = ArrayList<StudentAttendance>()
                    presentList = ArrayList<String>()
                    for (student in snapshot.children) {
                        val studentAttendance = StudentAttendance()
                        studentAttendance.name = student.value.toString()
                        studentAttendance.rollNumber = student.key.toString()
                        studentList.add(studentAttendance)
                        presentList.add(student.key.toString())
                    }
                    binding.attendanceRecyclerView.adapter =
                        AttendanceAdapter(requireContext(), studentList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun uploadAttendance() {

        val sendAttendanceList =
            SendAttendanceList(true, classData.Subject, presentList, classData.id)
        val response = ApiService.apiInterface.sendAttendance(sendAttendanceList)
        response.enqueue(object : Callback<AnnouncementResponse> {
            override fun onResponse(
                call: Call<AnnouncementResponse>,
                response: Response<AnnouncementResponse>
            ) {
                if (response.isSuccessful) {
                    val resData = response.body()
                    if (resData != null) {
                        if (resData.statusCode == 200) {
                            Toast.makeText(
                                requireContext(),
                                "Successfully Added Data to the server",
                                Toast.LENGTH_SHORT
                            ).show()
                            buttonStates = ButtonStates.START
                            binding.startEndBtn.text = buttonStates.toString()
                        }else{
                            Toast.makeText(
                                requireContext(),
                                resData.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Log.e("Error", "Error")
                }
            }

            override fun onFailure(call: Call<AnnouncementResponse>, t: Throwable) {
                Log.e("Error", t.toString())
            }

        })


    }

    private fun isPortalOpened() {
        FirebaseDatabase.getInstance()
            .getReference("Portal/${classData.semester}/${classData.Stream}").child(
                classData.Subject
            )
    }

}