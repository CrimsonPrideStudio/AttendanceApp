package com.example.components.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.components.R
import com.example.components.model.GetAnnouncementResponse

class AnnouncementAdapter(val context: Context, val announcements: GetAnnouncementResponse) :
    RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {

    class AnnouncementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.titleTextView)
        val teacherName = view.findViewById<TextView>(R.id.subtitleTextView)
        val content = view.findViewById<TextView>(R.id.contentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.announcement_card, parent, false)
        return AnnouncementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val data = announcements[position]
        holder.title.text = data.title
        holder.teacherName.text = data.teacher_name
        holder.content.text = data.announcement
    }

    override fun getItemCount(): Int {
        return announcements.size
    }
}