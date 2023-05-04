package com.example.attendanceapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.components.Fragments.Dashboard
import com.example.components.api.NotificationApiService
import com.example.components.model.NotificationDataModel
import com.example.components.model.PushNotificationModel
import com.example.components.utils.GetLocation
import com.example.components.utils.PermissionUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


const val TOPIC = "/topics/myTOpic"

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private  lateinit var  navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        if(PermissionUtil.requestLocationPermission(this)){
            GetLocation.getCurrentLocation(this, this) { location ->
               Toast.makeText(this,location,Toast.LENGTH_SHORT).show()
            }
        }

        PushNotificationModel(NotificationDataModel("HIIII", "HHEHEH"), TOPIC).also {
           // sendNotification(it)
        }

    }


    private fun sendNotification(notification: PushNotificationModel) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = NotificationApiService.notificationAPi.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d("MainACTIVITY", "Response ${response}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (errorBody.isNullOrEmpty()) {
                        response.message()
                    } else {
                        errorBody
                    }
                    Log.e("MainACTIVITY", "Error: ${response.code()}, Message: $errorMessage")
                }
            } catch (e: java.lang.Exception) {
                Log.e("MainACTIVITY", e.toString())
            }
        }
}