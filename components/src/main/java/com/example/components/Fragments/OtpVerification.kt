package com.example.components.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.components.R
import com.example.components.databinding.FragmentOtpVerificationBinding
import com.example.components.utils.FirebaseManager
import com.example.components.utils.SharedPrefs
import com.example.components.utils.SharedPrefs.Companion.KEY_PHONE_NUMBER
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.components.Fragments.LoginFragment.Companion.studentDetails
import com.example.components.utils.hideKeyboard


class OtpVerification : Fragment() {


    //Firebase Auth
    lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var firebaseManager: FirebaseManager


    lateinit var binding: FragmentOtpVerificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpVerificationBinding.inflate(layoutInflater)
       // firebaseManager = FirebaseManager()
       // firebaseManager.getAuth()?.firebaseAuthSettings?.setAppVerificationDisabledForTesting(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.send.setOnClickListener {
            verifyPasscode()
        }
        setOtpTextWatchers();
    }

    private fun changeFragment(path: Int) {
        findNavController().navigate(path)
    }

    private fun setOtpTextWatchers() {
        binding.apply{
        val otpFields = listOf(otp1, otp2, otp3, otp4, otp5, otp6)

        for (i in otpFields.indices) {
            otpFields[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrEmpty() && s.length == 1) {
                        if (i < otpFields.lastIndex) {
                            otpFields[i + 1].requestFocus()
                        } else {
                            otpFields.last().clearFocus()
                          hideKeyboard()

                        }
                    } else if (s.isNullOrEmpty()) {
                        if (i > 0) {
                            otpFields[i - 1].requestFocus()
                        }
                    }
                }
            })
        }
        }
    }

    private fun verifyPasscode(){
               binding.apply {
                   val code: String =
                       otp1.text.toString() + otp2.text.toString() + otp3.text.toString() + otp4.text.toString() + otp5.text.toString() + otp6.text.toString()
                   Toast.makeText(
                       requireContext(),
                       code + " : " + studentDetails.Passcode,
                       Toast.LENGTH_SHORT
                   ).show()
                   if (studentDetails.Passcode == code) {
                       findNavController().navigate(R.id.action_otpVerification_to_userFragment)
                   }
               }



    }

    private fun sendOTP() {
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                binding.apply {
                    otp1.setText(p0.smsCode?.get(0).toString())
                    otp2.setText(p0.smsCode?.get(1).toString())
                    otp3.setText(p0.smsCode?.get(2).toString())
                    otp4.setText(p0.smsCode?.get(3).toString())
                    otp5.setText(p0.smsCode?.get(4).toString())
                    otp6.setText(p0.smsCode?.get(5).toString())
                    signInWithPhoneAuthCredential(p0)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(requireContext(),p0.toString(),Toast.LENGTH_SHORT).show()

            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                binding.send.setOnClickListener {
                    binding.apply {
                        val code: String =
                            otp1.text.toString() + otp2.text.toString() + otp3.text.toString() + otp4.text.toString() + otp5.text.toString() + otp6.text.toString()

                        val credential: PhoneAuthCredential =
                            PhoneAuthProvider.getCredential(p0, code)
                        signInWithPhoneAuthCredential(credential)
                    }
                }
            }
        }
        val options = SharedPrefs(requireContext()).getStudentDataByOne(KEY_PHONE_NUMBER)?.let {
            firebaseManager.getAuth()?.let { it1 ->
                PhoneAuthOptions.newBuilder(it1)
                    .setPhoneNumber(it)       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(requireActivity())                 // Activity (for callback binding)
                    .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                    .build()
            }
        }
        options?.let { PhoneAuthProvider.verifyPhoneNumber(it) }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        firebaseManager.getAuth()?.signInWithCredential(credential)?.addOnCompleteListener {
            if (it.isSuccessful) {
//                changeFragment(R.id.action_otpVerification_to_dashboard)
            } else {
                Log.e("FIREBASE_ERROR", it.result.toString())
                Toast.makeText(requireContext(),it.result.toString(),Toast.LENGTH_SHORT).show()
            }
        }


    }

}