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
import com.example.components.R
import com.example.components.api.ApiService
import com.example.components.model.DashboardData
import com.example.components.databinding.FragmentDashboardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Dashboard : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var dashboardData: DashboardData
    private lateinit var binding: FragmentDashboardBinding
    private var stream:String =  "All"
    private var semester:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        initializeSpinners()
        hideFabButtonOnStudentApp();



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDashboardData()
    }
    private fun initializeSpinners() {
        val streamAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.streams,
            android.R.layout.simple_spinner_item
        )
        val yearAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.semesters,
            android.R.layout.simple_spinner_item
        )
        streamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.yearHome.adapter = yearAdapter
        binding.yearHome.onItemSelectedListener = this
        binding.streamHome.adapter = streamAdapter
        binding.streamHome.onItemSelectedListener = this


    }

    private fun hideFabButtonOnStudentApp() {
        if (requireContext().packageName.contains("student")) {
            binding.apply {
                addClassFloatingButton.hide()
            }
        }
    }

    private fun getDashboardData() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {

                val response = ApiService.apiInterface.getDashboardData(semester, stream)
                if (response.isSuccessful) {
                    dashboardData = response.body()!!
                    Log.e("DashboardData", dashboardData.toString())
                    withContext(Dispatchers.Main) {
                        binding.apply {
                            layoutManager = LinearLayoutManager(activity)
                            recycleViewHome.layoutManager = layoutManager
                            val adapter = DashboardAdapter(requireContext(), dashboardData)
                            recycleViewHome.adapter = adapter
                            adapter.setOnItemClickListener(object :
                                DashboardAdapter.OnnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    Log.e("Added", "Clicked")
                                }

                            })
                        }
                    }
                    // Process the dashboardData object here
                } else {
                    Log.e("DashboardData", response.toString())
                    // Handle the error here
                }
            } catch (e: Exception) {
                Log.e("DashboardData", e.toString())
                // Handle the exception here
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent.toString().contains("yearHome")){
            semester = position
        }
        stream = binding.streamHome.selectedItem.toString()
        getDashboardData()

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.e("This", "Nothing Selected")
    }
}

