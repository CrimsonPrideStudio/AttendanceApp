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
            val photoUrl = "https://firebasestorage.googleapis.com/v0/b/attandenceapp-4084c.appspot.com/o/images%2F${SharedPrefs.KEY_STUDENT_ID}?alt=media&token=37dbd09b-a56b-4243-b454-d10e1642d84a&_gl=1*dw42pg*_ga*MTU2MDQ1NTczNy4xNjgyOTU0NDQ5*_ga_CW55HF8NVT*MTY4NjQ4Mzk0MS4xMC4xLjE2ODY0ODM5NTUuMC4wLjA."
           Glide.with(requireContext()).load(sharedPrefs.getStudentDataByOne(photoUrl)).transition(DrawableTransitionOptions.withCrossFade()).into(profile)
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