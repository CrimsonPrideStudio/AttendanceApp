package com.example.components.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.components.Adapter.AnnouncementAdapter
import com.example.components.Adapter.DashboardAdapter
import com.example.components.R
import com.example.components.api.ApiService
import com.example.components.api.NotificationApiService
import com.example.components.databinding.AddAnnouncementBinding
import com.example.components.databinding.FragmentAnnouncementBinding
import com.example.components.model.AnnouncementData
import com.example.components.model.AnnouncementResponse
import com.example.components.model.NotificationDataModel
import com.example.components.model.PushNotificationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Announcement : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentAnnouncementBinding
    private lateinit var bindingForm: AddAnnouncementBinding

    //Api Variables
    private var stream: String = "All"
    private var semester: Int = 0

    private lateinit var layoutManager: RecyclerView.LayoutManager

    //Spinners
    private lateinit var yearAdepter: ArrayAdapter<CharSequence>
    private lateinit var streamAdepter: ArrayAdapter<CharSequence>

    //AlertDialogBox
    private lateinit var dialog: AlertDialog
    private lateinit var dialogBuilder: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAnnouncementBinding.inflate(layoutInflater)
        bindingForm = AddAnnouncementBinding.inflate(layoutInflater)

        hideFabButtonOnStudentApp()
        initializeSpinners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingForm.createBtn.setOnClickListener {
            addAnnouncement()
        }
        binding.addAnnouncementFloatingActionBtn.setOnClickListener {
            dialog.show()
        }
        bindingForm.cancelBtn.setOnClickListener {
            dialog.hide()
        }

        getAnnouncement()
    }

    private fun hideFabButtonOnStudentApp() {
        if (requireContext().packageName.contains("student")) {
            binding.apply {
                addAnnouncementFloatingActionBtn.hide()
            }
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
        initializeForm()

    }

    private fun addFormSpinnerFunctionality() {
        bindingForm.yearSpinner.adapter = yearAdepter
        bindingForm.yearSpinner.onItemSelectedListener = this
        bindingForm.streamSpinner.adapter = streamAdepter
        bindingForm.streamSpinner.onItemSelectedListener = this
    }

    private fun addAnnouncement() {
        bindingForm.apply {
            val title = announcementTitle.text.toString()
            val teacherName = "Priyanshu Sahu"
            val announcement = announcementInput.text.toString()
            val semester = semester
            val stream = streamSpinner.selectedItem.toString()
            val announcementId = semester.toString() + stream + title.substring(0, 4)
            val addSubjectDataModel =
                AnnouncementData(announcementId, title,  announcement,teacherName, semester, stream)
            val response = ApiService.apiInterface.addAnnouncement(addSubjectDataModel)
            response.enqueue(object : Callback<AnnouncementResponse> {
                override fun onResponse(
                    call: Call<AnnouncementResponse>,
                    response: Response<AnnouncementResponse>
                ) {
                    if (response.isSuccessful) {
                        val resData = response.body()
                        if (resData != null) {
                            if (resData.statusCode == 200) {
                                PushNotificationModel(
                                    NotificationDataModel(title, announcement),
                                    "/topics/${semester}${stream}"
                                ).also {
                                    Log.e("MainACTIVITY","/topics/${semester}${stream}")
                                    sendNotification(it)
                                }
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

    }

    private fun getAnnouncement() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {

                val response = ApiService.apiInterface.getAnnouncement(semester, stream)

                if (response.isSuccessful) {
                   val announcementData = response.body()!!
                    Log.e("DashboardData", announcementData.toString())
                    withContext(Dispatchers.Main) {
                        binding.apply {
                            layoutManager = LinearLayoutManager(activity)
                            recycleViewAnnouncement.layoutManager = layoutManager
                            val adapter = AnnouncementAdapter(requireContext(),announcementData)
                            recycleViewAnnouncement.adapter = adapter
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

    private fun sendNotification(notification: PushNotificationModel) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = NotificationApiService.notificationAPi.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d("MainACTIVITY", "Response ${response}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (errorBody.isNullOrEmpty()) {
                        response.message()
                    } else {
                        errorBody
                    }
                    Log.e("MainACTIVITY", "Error: ${response.code()}, Message: $errorMessage")
                }
            } catch (e: java.lang.Exception) {
                Log.e("MainACTIVITY", e.toString())
            }


        }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent.toString().contains("yearSpinner")) {
            semester = position
        }
        stream = bindingForm.streamSpinner.toString()


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.e("This", "Nothing Selected")
    }


}