package com.example.components.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings.Secure
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
import com.example.components.model.StudentLoginPostModel
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



    lateinit var binding: FragmentLoginBinding

    lateinit var androidId:String

    companion object{
        lateinit var studentDetails: StudentDataResponse
    }
    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        FirebaseApp.initializeApp(requireContext());
        androidId = Secure.getString(requireContext().contentResolver,Secure.ANDROID_ID)

        if(SharedPrefs(requireContext()).isLoggedIn()){
            findNavController().navigate(R.id.action_loginFragment_to_userFragment)
        }

        Log.d("Android", "Android ID : $androidId")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.send.setOnClickListener {
            val studentId = binding.input.text.toString()

            if (studentId.length == 12) {

                val studentLoginPostModel = StudentLoginPostModel(androidId,studentId)
                login(studentLoginPostModel)

            }
        }


    }

    private fun login(studentLoginPostModel:StudentLoginPostModel) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {
                val call = ApiService.apiInterface.login(studentLoginPostModel)
                call.enqueue(object : Callback<StudentDataResponse> {
                    override fun onResponse(
                        call: Call<StudentDataResponse>,
                        response: Response<StudentDataResponse>
                    ) {
                        if (response.isSuccessful) {
                            studentDetails = response.body()!!
                            Log.e("STudentData", studentDetails.toString())
                            SharedPrefs(requireContext()).setStudentDetails(studentDetails)
                            findNavController().navigate(R.id.action_loginFragment_to_otpVerification)
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