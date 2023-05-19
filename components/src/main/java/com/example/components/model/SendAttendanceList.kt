package com.example.components.model

data class SendAttendanceList(
    val Present: Boolean,
    val SubjectName: String,
    val presentStudentList: List<String>,
    val subject_id: String
)