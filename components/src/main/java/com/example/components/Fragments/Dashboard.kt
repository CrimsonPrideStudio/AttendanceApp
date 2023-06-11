package com.example.components.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.location.Location
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.components.Adapter.DashboardAdapter
import com.example.components.R
import com.example.components.api.ApiService
import com.example.components.databinding.AddClassDataformBinding
import com.example.components.databinding.FragmentDashboardBinding
import com.example.components.model.AddSubjectDataModel
import com.example.components.model.AddSubjectResponseModel
import com.example.components.model.DashboardData
import com.example.components.model.DashboardDataItem
import com.example.components.utils.GetLocation
import com.example.components.utils.MyAlertDialog
import com.example.components.utils.PermissionUtil
import com.example.components.utils.SharedPrefs
import com.example.components.utils.SharedPrefs.Companion.KEY_STUDENT_ID
import com.example.components.utils.SharedPrefs.Companion.KEY_STUDENT_NAME
import com.example.components.utils.SharedPrefs.Companion.KEY_STUDENT_SEMESTER
import com.example.components.utils.SharedPrefs.Companion.KEY_STUDENT_STREAM
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Dashboard : Fragment(), AdapterView.OnItemSelectedListener {


    //RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var dashboardData: DashboardData

    //binding
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var bindingForm: AddClassDataformBinding

    //AlertDialogBox
    private lateinit var dialog: AlertDialog
    private lateinit var dialogBuilder: AlertDialog.Builder

    //Api Variables
    private var stream: String = "All"
    private var semester: Int = 0

    //Spinners
    private lateinit var yearAdepter: ArrayAdapter<CharSequence>
    private lateinit var streamAdepter: ArrayAdapter<CharSequence>

    //location
    lateinit var currentLocation: String

    companion object {
        lateinit var classData: DashboardDataItem
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        bindingForm = AddClassDataformBinding.inflate(layoutInflater)

        Log.e("this", "Dashboard")
        initializeSpinners()
        initializeForm()
        addDashboardSpinnerFunctionality()
        hideFabButtonOnStudentApp()
        getCurrentLocation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDashboardData()
        buttonClickListener()
    }

    @SuppressLint("InflateParams")
    private fun buttonClickListener() {
        binding.addClassFloatingButton.setOnClickListener {
            dialog.show()

        }
        bindingForm.createBtn.setOnClickListener {
            addDashboardData();
            getDashboardData()
            dialog.hide()
        }
        bindingForm.cancelBtn.setOnClickListener {
            dialog.hide()
        }
    }

    private fun initializeForm() {

        addFormSpinnerFunctionality()
        dialogBuilder = AlertDialog.Builder(requireContext(), R.style.customAlert)
        dialogBuilder.setView(bindingForm.root)
        dialog = dialogBuilder.create()


    }

    private fun initializeSpinners() {
        streamAdepter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.streams,
            android.R.layout.simple_spinner_item
        )
        yearAdepter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.semesters,
            android.R.layout.simple_spinner_item
        )
        streamAdepter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearAdepter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    }

    private fun addDashboardSpinnerFunctionality() {
        binding.yearHome.adapter = yearAdepter
        binding.yearHome.onItemSelectedListener = this
        binding.streamHome.adapter = streamAdepter
        binding.streamHome.onItemSelectedListener = this
    }

    private fun addFormSpinnerFunctionality() {
        bindingForm.yearSpinner.adapter = yearAdepter
        bindingForm.yearSpinner.onItemSelectedListener = this
        bindingForm.streamSpinner.adapter = streamAdepter
        bindingForm.streamSpinner.onItemSelectedListener = this
    }

    private fun hideFabButtonOnStudentApp() {
        if (requireContext().packageName.contains("student")) {
            binding.apply {
                addClassFloatingButton.hide()
                yearHome.visibility = View.GONE
                streamHome.visibility = View.GONE
            }
            semester  = 8
            stream = SharedPrefs(requireContext()).getStudentDataByOne(KEY_STUDENT_STREAM)!!
            val topic = "/topics/${semester}${stream}"
            Log.e("MainActivty", topic)
            FirebaseMessaging.getInstance().subscribeToTopic(topic)

        }
    }

    private fun addDashboardData() {
        bindingForm.apply {
            val subjectName = subjectNameInput.text.toString()
            val teacherName = teacherNameInput.text.toString()
            val semesterInner = (yearSpinner.selectedItem.toString()[0].code) - '0'.code;
            val streamInner = streamSpinner.selectedItem.toString()
            Log.e("DashboardDataasdasd", streamInner.toString())
            val id = semesterInner.toString() + streamInner + subjectName.substring(0, 4)
            val present = 0
            val totalStudent = totalStudentsInput.text.toString().toInt()
            val updateDate = ""
            val addSubjectDataModel = AddSubjectDataModel(
                present,
                streamInner,
                subjectName,
                totalStudent,
                updateDate,
                id,
                semesterInner,
                teacherName
            )
            val response = ApiService.apiInterface.addSubjectData(addSubjectDataModel)
            response.enqueue(object : Callback<AddSubjectResponseModel> {
                override fun onResponse(
                    call: Call<AddSubjectResponseModel>,
                    response: Response<AddSubjectResponseModel>
                ) {
                    if (response.isSuccessful) {
                        val resData = response.body()
                        if (resData != null) {
                            Log.e("DashboardData", resData.message.toString())
                        }
                    } else {
                        Log.e("Error", response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<AddSubjectResponseModel>, t: Throwable) {
                    Log.e("Error", t.toString())
                }

            })
        }

    }

    private fun getDashboardData() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {

                val response = ApiService.apiInterface.getDashboardData(semester, stream)

                if (response.isSuccessful) {
                    dashboardData = response.body()!!
                    Log.e("DashboardData", dashboardData.toString())
                    withContext(Dispatchers.Main) {
                        binding.apply {
                            layoutManager = LinearLayoutManager(activity)
                            recycleViewHome.layoutManager = layoutManager
                            val adapter = DashboardAdapter(requireContext(), dashboardData)
                            recycleViewHome.adapter = adapter
                            adapter.setOnItemClickListener(object :
                                DashboardAdapter.OnnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    classData = dashboardData[position]
                                    if (requireContext().packageName.contains("student")) {
                                        sendStudentData()
                                    } else {
                                        findNavController().navigate(R.id.action_userFragment_to_teacherAttendanceFragment)
                                    }
                                }

                            })
                        }
                    }
                    // Process the dashboardData object here
                } else {
                    Log.e("DashboardData", response.toString())
                    // Handle the error here
                }
            } catch (e: Exception) {
                Log.e("DashboardData", e.toString())
                // Handle the exception here
            }
        }
    }

    private fun sendStudentData() {
        val myAlertDialog = MyAlertDialog(requireContext())
        myAlertDialog.builder.show()
        val ref = FirebaseDatabase.getInstance()
            .getReference("Portal/${classData.semester}/${classData.Stream}/${classData.Subject}")

        ref.get().addOnSuccessListener {
            if (it.value != "") {
                val locationA = stringToLocation(it.value.toString())
                val locationB = stringToLocation(currentLocation)
                val refToAttendance =
                    SharedPrefs(requireContext()).getStudentDataByOne(KEY_STUDENT_ID)?.let { it1 ->
                        FirebaseDatabase.getInstance()
                            .getReference("Attendance/${classData.semester}/${classData.Stream}/${classData.Subject}")
                            .child(it1).setValue(SharedPrefs(requireContext()).getStudentDataByOne(
                                KEY_STUDENT_NAME))
                    }


            } else {

            }
        }

    }

    private fun distanceCalculate(locationA: Location, locationB: Location): Int {
        val startPoint = Location("Start")
        startPoint.latitude = locationA.latitude
        startPoint.longitude = locationA.longitude
        val endPoint = Location("Start")
        endPoint.latitude = locationB.latitude
        endPoint.longitude = locationB.longitude
        return startPoint.distanceTo(endPoint).toInt()
    }

    private fun stringToLocation(location: String): Location {
        val currLocation = location
        val startPoint = Location("Start")
        startPoint.latitude = currLocation.split(":")[0].toDouble()
        startPoint.longitude = currLocation.split(":")[1].toDouble()
        return startPoint
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent.toString().contains("yearHome")) {
            semester = position
        }
        stream = binding.streamHome.selectedItem.toString()

        getDashboardData()

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.e("This", "Nothing Selected")
    }

    private fun getCurrentLocation() {
        if (PermissionUtil.requestLocationPermission(requireActivity())) {
            GetLocation.getCurrentLocation(requireContext(), requireActivity()) { location ->
                currentLocation = location
            }
        }
    }

}

