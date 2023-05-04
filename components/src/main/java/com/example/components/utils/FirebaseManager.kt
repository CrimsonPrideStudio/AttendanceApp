package com.example.components.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

 class FirebaseManager {
    private var mAuth: FirebaseAuth? = null
    private var mFirestore: FirebaseFirestore? = null

    fun getAuth(): FirebaseAuth? {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance()
        }
        return mAuth
    }

    fun getFirestore(): FirebaseFirestore? {
        if (mFirestore == null) {
            mFirestore = FirebaseFirestore.getInstance()
        }
        return mFirestore
    }


}