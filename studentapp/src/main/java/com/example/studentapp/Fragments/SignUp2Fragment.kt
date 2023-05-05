package com.example.studentapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.studentapp.Fragments.SignUp1Fragment.Companion.studentDataPostModel
import com.example.studentapp.R
import com.example.studentapp.databinding.FragmentSignUp2Binding


class SignUp2Fragment : Fragment() {


    lateinit var binding: FragmentSignUp2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUp2Binding.inflate(layoutInflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            next2.setOnClickListener {
                studentDataPostModel.Gender = getStudentGender()
                studentDataPostModel.DOB = getDateOfBirth()
                findNavController().navigate(R.id.action_signUp2Fragment_to_signUp3Fragment)
            }
        }
    }

    private fun getStudentGender(): String {
        val selectedRadioButtonId: Int = binding.radioGroupGender.checkedRadioButtonId
        val selectedRadioButton: RadioButton = binding.root.findViewById(selectedRadioButtonId)
        return selectedRadioButton.text.toString()
    }

    private fun getDateOfBirth(): String {
        val day = binding.agePicker.dayOfMonth
        val month = binding.agePicker.month + 1 // month is zero-indexed
        val year = binding.agePicker.year
        return "$day/$month/$year"
    }



}