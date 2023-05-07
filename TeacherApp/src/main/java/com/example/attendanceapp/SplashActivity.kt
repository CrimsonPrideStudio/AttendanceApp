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
import androidx.viewpager2.widget.ViewPager2
import com.example.attendanceapp.Adapter.ViewPagerAdapter
import com.example.components.Fragments.Announcement
import com.example.components.Fragments.Dashboard
import com.example.components.Fragments.Setting
import com.example.components.api.NotificationApiService
import com.example.components.model.NotificationDataModel
import com.example.components.model.PushNotificationModel
import com.example.components.utils.GetLocation
import com.example.components.utils.PermissionUtil
import com.sagarkoli.chetanbottomnavigation.chetanBottomNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


const val TOPIC = "/topics/myTOpic"

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private  lateinit var  navController: NavController

    lateinit var navBar: chetanBottomNavigation
    lateinit var viewPager: ViewPager2


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

            bottomNavigation();

        }


    private fun bottomNavigation(){

        navBar = findViewById(R.id.navBar)
        viewPager = findViewById(R.id.viewPager)

        val fragments = listOf(Announcement(), Dashboard(), Setting())
        viewPager.adapter = ViewPagerAdapter(this, fragments)

        navBar.add(chetanBottomNavigation.Model(1, com.example.components.R.drawable.profile))
        navBar.add(chetanBottomNavigation.Model(2,com.example.components.R.drawable.dashboard_icon))
        navBar.add(chetanBottomNavigation.Model(3,com.example.components.R.drawable.setting_icon))

        navBar.setCount(4,"10")

        navBar.setOnShowListener { item ->
            when (item?.id) {
                1 -> changeFragment(0)
                2 -> changeFragment(1)
                3 -> changeFragment(2)
            }
        }

        navBar.setOnClickMenuListener {

        }

        navBar.setOnReselectListener {

        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                navBar.show(position + 1, true)
            }
        })

        viewPager.currentItem=1

        navBar.show(2,true)
    }

    private fun changeFragment(position: Int) {
        viewPager.currentItem = position
    }
}