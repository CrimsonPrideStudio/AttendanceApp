package com.example.components.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.components.R
import com.example.components.databinding.FragmentSignUp1Binding
import com.example.components.model.StudentDataPostModel
import java.util.regex.Pattern


class SignUp1Fragment : Fragment() {


    lateinit var binding: FragmentSignUp1Binding

    companion object {
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

        if (validateInput()) {

            binding.signupNextButton.setOnClickListener {
                getStudentDetailsFromUser()
                findNavController().navigate(R.id.action_signUp1Fragment_to_signUp2Fragment)
            }
        }


        binding.login.setOnClickListener {
            // findNavController().navigate(R.id.action_signUpFragment_to_loginFragment2)
        }
    }

    private fun validateInput(): Boolean {
        var firstNameValid = false;
        var lastNameValid = false;
        var emailValid = false;
        var mobileNumberValid = false;
        binding.apply {
            etFirstName.doOnTextChanged { text, _, _, _ ->
                if (text!!.isNotEmpty()) {
                    fullNameLayout.error = null
                    firstNameValid = true
                } else {
                    fullNameLayout.error = "Required*"
                    firstNameValid = false
                }
            }
            etLastName.doOnTextChanged { text, _, _, _ ->
                if (text!!.isNotEmpty()) {
                    lastNameLayout.error = null
                    lastNameValid = true
                } else {
                    lastNameLayout.error = "Required*"
                    lastNameValid = false
                }
            }
            etEmail.doOnTextChanged { text, _, _, _ ->
                if (isValidEmailId(text.toString())) {
                    emailLayout.error = null
                    emailValid = true
                } else {
                    emailLayout.error = "Invalid Email Format*"
                    emailValid = false
                }
            }

            etMobileNumber.doOnTextChanged { text, _, _, _ ->
                if (text!!.length == 10) {
                    fullNameLayout.error = null
                    mobileNumberValid = true
                } else {
                    fullNameLayout.error = "Required*"
                    mobileNumberValid = false
                }
            }
        }
        return firstNameValid && lastNameValid && emailValid && mobileNumberValid
    }

    private fun isValidEmailId(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    @SuppressLint("HardwareIds")
    private fun getStudentDetailsFromUser() {
        binding.apply {
            studentDataPostModel.Name =
                etFirstName.text.toString() + " " + etLastName.text.toString()
            studentDataPostModel.Email = etEmail.text.toString()
            studentDataPostModel.MobileNumber = etMobileNumber.text.toString()
            studentDataPostModel.mobileId = Settings.Secure.getString(
                requireContext().contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }
    }

}