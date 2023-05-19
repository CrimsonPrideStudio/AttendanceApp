package com.example.components.Adapter

import android.content.Context
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.components.R
import com.example.components.model.GetAnnouncementResponse
import com.example.components.model.StudentAttendance
import de.hdodenhof.circleimageview.CircleImageView

class AttendanceAdapter(val context: Context,val presentList:ArrayList<StudentAttendance>):RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val studentName :TextView = view.findViewById(R.id.studentName)
        val profile:CircleImageView = view.findViewById(R.id.profile)
    }

    val drawables = arrayOf(
        ContextCompat.getDrawable(context, R.drawable.avatar_1),
        ContextCompat.getDrawable(context, R.drawable.avatar_2),
        ContextCompat.getDrawable(context, R.drawable.avatar_3),
        ContextCompat.getDrawable(context, R.drawable.avatar_4)
    )
    val layerDrawable = LayerDrawable(drawables)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attendance_cardview,parent,false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = presentList[position]
        holder.studentName.text = value.name;

        holder.profile.setImageDrawable(layerDrawable)

    }

    override fun getItemCount(): Int {
        return presentList.size
    }
}