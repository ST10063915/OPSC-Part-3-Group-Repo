package com.opsc.onesecondapp

import com.opsc.onesecondapp.models.User
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun getUserData(userId: String, callback: (User?) -> Unit) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun saveUserData(user: User, callback: (Boolean) -> Unit) {
        firestore.collection("users").document(user.id).set(user)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }
}