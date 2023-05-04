package com.example.components.api

import com.example.components.utils.FirebaseManager
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot

object FirebaseQueries {
    private  val firebaseManager = FirebaseManager()

    fun getStudentDetails(rollNumber: String): Task<DocumentSnapshot> {
        val docRef = firebaseManager.getFirestore()?.collection("Students")?.document(rollNumber)
        return docRef?.get() ?: Tasks.forException(Exception("Firestore not initialized"))
    }
}