package com.opsc.onesecondapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)

        val loginTextButton = findViewById<Button>(R.id.loginTextButton)

        // Register button click
        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            // Check if fields are empty
            if (username.isEmpty()) {
                usernameEditText.error = "Please enter a username"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                emailEditText.error = "Please enter an email address"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Please enter a password"
                return@setOnClickListener
            }

            registerUser(username, email, password)
        }

        loginTextButton.setOnClickListener {
            navigateToLogin()
        }

        loginTextButton.setTextColor(getColor(R.color.loginTextColor))
    }

    // Register user using email and password
    private fun registerUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    saveUserToFirestore(user?.uid ?: "", username, email)
                    navigateToLogin()
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Save user information in Firestore
    private fun saveUserToFirestore(userId: String, username: String, email: String) {
        val userInfo = hashMapOf(
            "username" to username,
            "email" to email
        )

        db.collection("users").document(userId)
            .set(userInfo)
            .addOnSuccessListener {
                // User data successfully saved (fix)
            }
            .addOnFailureListener { e ->
                // Handle failure (fix)
            }
    }

    // Navigate to LoginActivity
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
