package com.example.components.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.components.databinding.FragmentProfileBinding
import com.example.components.utils.SharedPrefs


class Profile : Fragment() {

    lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val sharedPrefs = SharedPrefs(requireContext())
        binding.apply {
           Glide.with(requireContext()).load(sharedPrefs.getStudentDataByOne(SharedPrefs.KEY_PHOTO_URI)).transition(DrawableTransitionOptions.withCrossFade()).into(profile)
            name.setText(sharedPrefs.getStudentDataByOne(SharedPrefs.KEY_STUDENT_NAME))
            email.setText(sharedPrefs.getStudentDataByOne(SharedPrefs.KEY_EMAIL))
            mobileNumber.setText(sharedPrefs.getStudentDataByOne(SharedPrefs.KEY_PHONE_NUMBER))
            rollNumber.setText(sharedPrefs.getStudentDataByOne(SharedPrefs.KEY_STUDENT_ID))
            semester.setText(sharedPrefs.getStudentDataByOne(SharedPrefs.KEY_STUDENT_SEMESTER))
            stream.setText(sharedPrefs.getStudentDataByOne(SharedPrefs.KEY_STUDENT_STREAM))
        }
        return binding.root
    }


}