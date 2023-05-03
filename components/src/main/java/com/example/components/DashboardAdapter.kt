package com.example.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.components.model.DashboardData

class DashboardAdapter(val context: Context, private val subjects: DashboardData): RecyclerView.Adapter<DashboardAdapter.ClassViewHolder>() {
    private  lateinit var  mListener: OnnItemClickListener
    interface OnnItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnnItemClickListener){
        mListener = listener
    }
    class ClassViewHolder(view: View,listener: OnnItemClickListener):RecyclerView.ViewHolder(view){
        val subjectAndSemText: TextView = view.findViewById(R.id.subjectAndSemText)
        val teacherNameText: TextView = view.findViewById(R.id.teacherName)
        val lastModifiedText: TextView = view.findViewById(R.id.lastUpdated)
        val totalStudents: TextView = view.findViewById(R.id.totalStudents)
        val presentStudents: TextView = view.findViewById(R.id.presentStudent)
        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_card_view,parent,false)
        return ClassViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val data = subjects[position]
        holder.subjectAndSemText.text = data.Subject + data.semester
        holder.teacherNameText.text = data.teacher
        holder.lastModifiedText.text = data.Update_Date
        holder.totalStudents.text = data.Total_Student.toString()
        holder.presentStudents.text = data.Present.toString()
    }

    override fun getItemCount(): Int {
       return subjects.size
    }
}