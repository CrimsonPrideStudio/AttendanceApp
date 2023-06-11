package com.example.components.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.components.Fragments.SignUp1Fragment.Companion.studentDataPostModel
import com.example.components.R
import com.example.components.databinding.FragmentSignUp3Binding


class SignUp3Fragment : Fragment(), AdapterView.OnItemSelectedListener {


    //Spinners
    private lateinit var yearAdepter: ArrayAdapter<CharSequence>
    private lateinit var streamAdepter: ArrayAdapter<CharSequence>


    lateinit var binding: FragmentSignUp3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUp3Binding.inflate(layoutInflater)
        initializeSpinners()
        addDashboardSpinnerFunctionality()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

                next3.setOnClickListener {
                    studentDataPostModel.student_id = etRollNumber.text.toString()
                    studentDataPostModel.EnrollNumber = etEnrollNumber.text.toString()
                    studentDataPostModel.semester =
                        (semester.selectedItem.toString()[0].code) - '0'.code;
                    studentDataPostModel.Stream = stream.selectedItem.toString()
                    findNavController().navigate(R.id.action_signUp3Fragment_to_signUp4Fragment)
                }

        }
    }

    private fun initializeSpinners() {
        streamAdepter = ArrayAdapter.createFromResource(
            requireContext(),
            com.example.components.R.array.streams,
            android.R.layout.simple_spinner_item
        )
        yearAdepter = ArrayAdapter.createFromResource(
            requireContext(),
            com.example.components.R.array.semesters,
            android.R.layout.simple_spinner_item
        )
        streamAdepter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearAdepter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    }

    private fun validateInput(): Boolean {
        var rollNumberValid = false;
        var enrollValid = false;
        binding.apply {
            etRollNumber.doOnTextChanged { text, _, _, _ ->
                if (text!!.length == 12) {
                    rollNumberLayout.error = null
                    rollNumberValid = true
                } else {
                    rollNumberLayout.error = "Enter Valid Roll Number*"
                    rollNumberValid = false
                }
            }
            etEnrollNumber.doOnTextChanged { text, _, _, _ ->
                if (text!!.length == 6) {
                    enrollNumberLayout.error = null
                    enrollValid = true
                } else {
                    enrollValid = false
                    enrollNumberLayout.error = "Enter Valid Enrollment Number*"
                }
            }
        }
        return rollNumberValid && enrollValid
    }

    private fun addDashboardSpinnerFunctionality() {
        binding.semester.adapter = yearAdepter
        binding.semester.onItemSelectedListener = this
        binding.stream.adapter = streamAdepter
        binding.stream.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}