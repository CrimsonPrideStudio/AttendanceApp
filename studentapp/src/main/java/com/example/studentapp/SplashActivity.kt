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
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("TOKEN", it.result)
            } else {
                Log.e("Token Details", "Token Failed To receive")

            }
        }

        bottomNavigation()

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