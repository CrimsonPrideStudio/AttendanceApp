package com.example.components.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.components.model.StudentDataResponse

class SharedPrefs(context: Context) {
    private val prefName = "user_preference"
    private var pref: SharedPreferences = context.getSharedPreferences(
        prefName,
        Context.MODE_PRIVATE
    )
    private var editor: SharedPreferences.Editor? = null

    companion object {
        private const val PRIVATE_MODE = 0

        //keys
        const val KEY_LOGGED_IN = "loggedIn"
        const val KEY_STUDENT_ID = "studentId"
        const val KEY_STUDENT_NAME = "studentName"
        const val KEY_PHOTO_URI = "photoUri"
        const val KEY_PHONE_NUMBER = "phoneNumber"
        const val KEY_EMAIL = "email"
        const val KEY_STUDENT_STREAM = "studentStream"
        const val KEY_STUDENT_SEMESTER = "studentSemester"
        const val KEY_ENROLLMENT_NUMBER = "enrollmentNumber"
        const val KEY_STUDENT_DOB = "studentDOB"
        const val KEY_STUDENT_GENDER = "studentGender"
    }

    init {
        editor = pref.edit()
    }

    fun setStudentDetails(studentDataResponse: StudentDataResponse) {
        editor!!.putBoolean(
            KEY_LOGGED_IN, true
        )
        editor!!.putString(KEY_STUDENT_ID, studentDataResponse.student_id)
        editor!!.putString(KEY_STUDENT_NAME, studentDataResponse.Name)
        editor!!.putString(KEY_PHOTO_URI, studentDataResponse.Profile)
        editor!!.putString(KEY_PHONE_NUMBER, studentDataResponse.MobileNumber)
        editor!!.putString(KEY_EMAIL, studentDataResponse.Email)
        editor!!.putString(KEY_STUDENT_STREAM, studentDataResponse.Stream)
        editor!!.putString(KEY_STUDENT_SEMESTER, studentDataResponse.semester.toString())
        editor!!.putString(KEY_ENROLLMENT_NUMBER, studentDataResponse.EnrollNumber)
        editor!!.putString(KEY_STUDENT_DOB, studentDataResponse.DOB)
        editor!!.putString(KEY_STUDENT_GENDER, studentDataResponse.Gender)
        editor!!.commit()
    }

    fun clearSharedPref() {
        editor!!.clear()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(KEY_LOGGED_IN, false)
    }

    fun getStudentDataByOne(key: String): String? {
        return pref.getString(key, "")
    }

}
