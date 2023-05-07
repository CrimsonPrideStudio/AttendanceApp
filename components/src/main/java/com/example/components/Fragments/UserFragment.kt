package com.example.components.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.components.Adapter.ViewPagerAdapter
import com.example.components.R
import com.example.components.databinding.FragmentUserBinding
import com.sagarkoli.chetanbottomnavigation.chetanBottomNavigation


class UserFragment : Fragment() {


    lateinit var binding:FragmentUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(layoutInflater)
        bottomNavigation()
        return binding.root
    }

    private fun bottomNavigation(){

        binding.apply {

            val fragments = listOf(Announcement(), Dashboard(), Setting())
            viewPager.adapter = ViewPagerAdapter(requireActivity(), fragments)

            navbar.add(chetanBottomNavigation.Model(1, com.example.components.R.drawable.profile))
            navbar.add(
                chetanBottomNavigation.Model(
                    2,
                    com.example.components.R.drawable.dashboard_icon
                )
            )
            navbar.add(
                chetanBottomNavigation.Model(
                    3,
                    com.example.components.R.drawable.setting_icon
                )
            )

            navbar.setCount(4, "10")

            navbar.setOnShowListener { item ->
                when (item?.id) {
                    1 -> changeFragment(0)
                    2 -> changeFragment(1)
                    3 -> changeFragment(2)
                }
            }

            navbar.setOnClickMenuListener {

            }

            navbar.setOnReselectListener {

            }

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    navbar.show(position + 1, true)
                }
            })

            viewPager.currentItem = 1

            navbar.show(2, true)
        }
    }

    private fun changeFragment(position: Int) {
        binding.viewPager.currentItem = position
    }

}