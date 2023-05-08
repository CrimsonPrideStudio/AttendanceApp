package com.example.components.model

data class GetAnnouncementResponseItem(
    val announcement: String,
    val announcement_id: String,
    val semester: Int,
    val stream: String,
    val teacher_name: String,
    val time: String,
    val title: String
)