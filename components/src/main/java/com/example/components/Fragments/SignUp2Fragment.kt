package com.example.components.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.components.Fragments.SignUp1Fragment.Companion.studentDataPostModel
import com.example.components.R
import com.example.components.databinding.FragmentSignUp2Binding
import java.lang.Integer.parseInt


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