package com.example.studentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.components.Fragments.Announcement
import com.example.components.Fragments.Dashboard
import com.example.components.Fragments.Setting
import com.example.studentapp.Adapter.ViewPagerAdapter
import com.google.firebase.messaging.FirebaseMessaging
import com.sagarkoli.chetanbottomnavigation.chetanBottomNavigation

const val TOPIC = "/topics/myTOpic"
class SplashActivity : AppCompatActivity() {
    private  lateinit var  navController:NavController

    lateinit var navBar: chetanBottomNavigation
    lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("TOKEN", it.result)
            } else {
                Log.e("Token Details", "Token Failed To receive")

            }
        }



    }



}