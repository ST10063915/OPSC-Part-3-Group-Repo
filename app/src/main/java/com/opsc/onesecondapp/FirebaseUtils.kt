package com.opsc.onesecondapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUtils {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getUserDocumentRef() = firestore.collection("users").document(getCurrentUserId()!!)
}