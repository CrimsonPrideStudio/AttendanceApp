package com.example.components.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.components.R
import com.example.components.api.ApiService
import com.example.components.databinding.FragmentLoginBinding
import com.example.components.model.StudentDataResponse
import com.example.components.utils.SharedPrefs
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {


    //APi Variaables
    lateinit var studentDetails: StudentDataResponse

    lateinit var binding: FragmentLoginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        FirebaseApp.initializeApp(requireContext());

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.send.setOnClickListener {
            val studentId = binding.input.text.toString()
            if (studentId.length == 12) {
                getStudentDetails(studentId)
                findNavController().navigate(R.id.action_loginFragment_to_otpVerification)
            }
        }


    }

    private fun getStudentDetails(student_id: String) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {
                val call = ApiService.apiInterface.getStudentDetails(student_id)
                call.enqueue(object : Callback<StudentDataResponse> {
                    override fun onResponse(
                        call: Call<StudentDataResponse>,
                        response: Response<StudentDataResponse>
                    ) {
                        if (response.isSuccessful) {
                            val studentDetails = response.body()!!
                            Log.e("STudentData", studentDetails.toString())
                            SharedPrefs(requireContext()).setStudentDetails(studentDetails)
                            // Process the dashboardData object here
                        } else {
                            Log.e("DashboardData", response.toString())
                            // Handle the error here
                        }
                    }

                    override fun onFailure(call: Call<StudentDataResponse>, t: Throwable) {
                        Log.e("DashboardData", t.toString())
                        // Handle the exception here
                    }
                })
            } catch (e: Exception) {
                Log.e("DashboardData", e.toString())
                // Handle the exception here
            }
        }
    }

}