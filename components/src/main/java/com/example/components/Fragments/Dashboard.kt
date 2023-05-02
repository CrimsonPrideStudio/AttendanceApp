package com.example.components.Fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.components.DashboardAdapter
import com.example.components.api.ApiService
import com.example.components.model.DashboardData
import com.example.components.databinding.FragmentDashboardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Dashboard : Fragment(),AdapterView.OnItemSelectedListener{

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var dashboardData: DashboardData
    private lateinit var binding :FragmentDashboardBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = FragmentDashboardBinding.inflate(layoutInflater)

   getDashboardData()
        return binding.root
    }
  private fun getDashboardData(){

    val scope = CoroutineScope(Dispatchers.IO)
    scope.launch {
        try {

            val response = ApiService.apiInterface.getDashboardData()
            if (response.isSuccessful) {
                dashboardData = response.body()!!
                Log.e("DashboardData",dashboardData.toString())
                withContext(Dispatchers.Main) {
                    binding.apply {
                        layoutManager = LinearLayoutManager(activity)
                        recycleViewHome.layoutManager = layoutManager
                        val adapter = DashboardAdapter(requireContext(),dashboardData)
                        recycleViewHome.adapter = adapter
                        adapter.setOnItemClickListener(object : DashboardAdapter.OnnItemClickListener{
                            override fun onItemClick(position: Int) {
                                Log.e("Added","Clicked")
                            }

                        })
                    }
                }
                // Process the dashboardData object here
            } else {
                Log.e("DashboardData","Error")
                // Handle the error here
            }
        } catch (e: Exception) {
            Log.e("DashboardData",e.toString())
            // Handle the exception here
        }
    }
}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}

