package com.example.attendanceapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.components.utils.GetLocation
import com.example.components.utils.PermissionUtil


class SplashActivity : AppCompatActivity() {
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (PermissionUtil.requestLocationPermission(this)) {
            GetLocation.getCurrentLocation(this, this) { location ->
                Toast.makeText(this, location, Toast.LENGTH_SHORT).show()
            }
        }


    }


}