package com.example.components.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.components.Fragments.SignUp1Fragment.Companion.studentDataPostModel
import com.example.components.R
import com.example.components.databinding.FragmentSignUp3Binding


class SignUp3Fragment : Fragment(), AdapterView.OnItemSelectedListener {


    //Spinners
    private lateinit var yearAdepter: ArrayAdapter<CharSequence>
    private lateinit var streamAdepter: ArrayAdapter<CharSequence>

    //Api Variables
    private var Stream: String = "All"
    private var semesterInt: Int = 0

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
                studentDataPostModel.semester = semesterInt
                studentDataPostModel.Stream = Stream
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

    private fun addDashboardSpinnerFunctionality() {
        binding.semester.adapter = yearAdepter
        binding.semester.onItemSelectedListener = this
        binding.stream.adapter = streamAdepter
        binding.stream.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent.toString().contains("yearHome")) {
            semesterInt = position
        }
        Stream = binding.stream.selectedItem.toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}