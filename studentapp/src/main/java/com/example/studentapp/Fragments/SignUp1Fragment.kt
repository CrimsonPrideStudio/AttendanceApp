package com.example.studentapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.components.model.StudentDataPostModel
import com.example.studentapp.R
import com.example.studentapp.databinding.FragmentSignUp1Binding


class SignUp1Fragment : Fragment() {


    lateinit var binding:FragmentSignUp1Binding
    companion object{
        lateinit var studentDataPostModel: StudentDataPostModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUp1Binding.inflate(layoutInflater)
        studentDataPostModel = StudentDataPostModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupNextButton.setOnClickListener{
            getStudentDetailsFromUser()


            findNavController().navigate(R.id.action_signUpFragment_to_signUp2Fragment)
        }
    }

    private  fun getStudentDetailsFromUser(){
        binding.apply {
            studentDataPostModel.Name = etFirstName.text.toString() +" " + etSecondName.text.toString()
            studentDataPostModel.Email = etEmail.text.toString()
            studentDataPostModel.MobileNumber = etMobileNumber.text.toString()
        }
    }

}