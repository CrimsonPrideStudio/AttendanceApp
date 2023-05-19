package com.example.components.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.components.R
import com.example.components.databinding.FragmentSettingBinding
import com.example.components.utils.SharedPrefs


class Setting : Fragment() {

    lateinit var binding:FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutBtn.setOnClickListener {
            SharedPrefs(requireContext()).clearSharedPref()
            findNavController().navigate(R.id.action_userFragment_to_loginFragment)
            SharedPrefs(requireContext()).getStudentDataByOne(SharedPrefs.KEY_STUDENT_NAME)
                ?.let { it1 -> Log.e("SettingFragment", it1) }
        }
        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_userFragment_to_profile2)
        }
    }


}