package com.example.components.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.components.CropperActivity
import com.example.components.Fragments.SignUp1Fragment.Companion.studentDataPostModel
import com.example.components.api.ApiService
import com.example.components.databinding.FragmentSignUp4Binding
import com.example.components.model.StudentSignupResponse
import com.example.components.utils.hideKeyboard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class SignUp4Fragment : Fragment() {

    val GALLERY_REQ_CODE = 100
    private lateinit var binding: FragmentSignUp4Binding

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var destUri: String

    companion object {
        lateinit var imageUri: Uri
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUp4Binding.inflate(layoutInflater)
        firebaseStorage = FirebaseStorage.getInstance()




        setOtpTextWatchers()
        // sign in anonymously
        FirebaseAuth.getInstance().signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FIREBASEEEEEEEEEE", "signInAnonymously:success")
                } else {
                    Log.w("FIREBASEEEEEEEEEE", "signInAnonymously:failure", task.exception)
                }
            }


        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    imageUri = data?.data!!

                    if (imageUri != null) {
                        val intent = Intent(requireContext(), CropperActivity::class.java)
                        startActivityForResult(intent, 101)
                    }
                }
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUploadImg.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(intent)
        }
        binding.submit.setOnClickListener {
            studentDataPostModel.Profile = "images/${studentDataPostModel.student_id}"
            binding.apply {
                studentDataPostModel.Passcode =
                    otp1.text.toString() + otp2.text.toString() + otp3.text.toString() + otp4.text.toString() + otp5.text.toString() + otp6.text.toString()
                signUpStudent()
            }

        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri) {
        val compressedImageFile = compressImage(imageUri)

        val storageRef = firebaseStorage.reference
        val imageRef = storageRef.child("images/${studentDataPostModel.student_id}")
        val uploadTask = imageRef.putFile(compressedImageFile)

        uploadTask.addOnSuccessListener {
            // Image upload successful
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Get the public download URL
                val downloadUrl = uri.toString()
                // Use the downloadUrl for your app's functionality

            }
        }.addOnFailureListener {
            Log.e("Upload", it.message.toString())
        }
    }

    private fun setOtpTextWatchers() {
        binding.apply {
            val otpFields = listOf(otp1, otp2, otp3, otp4, otp5, otp6)

            for (i in otpFields.indices) {
                otpFields[i].addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

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

    private fun compressImage(uri: Uri): Uri {
        val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        val compressedData = outputStream.toByteArray()

        val compressedImageFile = File.createTempFile("compressed", "jpg")
        val fileOutputStream = FileOutputStream(compressedImageFile)
        fileOutputStream.write(compressedData)
        fileOutputStream.flush()
        fileOutputStream.close()

        return Uri.fromFile(compressedImageFile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == -1 && requestCode == 101) {
            uploadImageToFirebaseStorage(imageUri)
            binding.btnUploadImg.setImageURI(imageUri)
        }
    }

    private fun signUpStudent() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {
                val call = ApiService.apiInterface.signupStudent(studentDataPostModel)
                call.enqueue(object : Callback<StudentSignupResponse> {
                    override fun onResponse(
                        call: Call<StudentSignupResponse>,
                        response: Response<StudentSignupResponse>
                    ) {
                        if (response.isSuccessful) {
                            val studentDetails = response.body()!!
                            // Process the dashboardData object here
                            if (studentDetails.statusCode == 200) {
                                // findNavController().navigate(R.id.action_signUp4Fragment_to_loginFragment2)
                            }
                        } else {
                            Log.e("DashboardData1", response.toString())
                            // Handle the error here
                        }
                    }

                    override fun onFailure(call: Call<StudentSignupResponse>, t: Throwable) {
                        Log.e("DashboardData", t.toString())
                        if (t is HttpException) {
                            val errorBody = t.response()?.errorBody()?.string()
                            Log.e("DashboardData2", "Error Body: $errorBody")
                        }
                        // Handle the exception here
                    }
                })
            } catch (e: Exception) {
                Log.e("DashboardData3", e.toString())
                // Handle the exception here
            }
        }
    }

}