package com.opsc.onesecondapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        //Standard Login
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if fields are empty
            if (email.isEmpty()) {
                emailEditText.error = "Please enter an email address"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordEditText.error = "Please enter a password"
                return@setOnClickListener
            }

            normalLogin(email, password)

        }

        // Google SSO button
        val googleSSOButton = findViewById<Button>(R.id.googleSSOButton)
        googleSSOButton.setOnClickListener {
            launchSSO()
        }

        // Registration button
        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    // Normal login using email and password
    private fun normalLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    navigateToHome(user)
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Google SSO login
    private fun launchSSO() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val user: FirebaseUser? = auth.currentUser
            if (user != null) {
                saveUserToFirestore(user)
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                // Handle sign-in failure (fix)
            }
        }
    }

    private fun saveUserToFirestore(user: FirebaseUser) {
        val userInfo = hashMapOf(
            "username" to user.displayName,
            "email" to user.email
        )

        db.collection("users").document(user.uid)
            .set(userInfo)
            .addOnSuccessListener {
                // User data successfully saved (fix)
            }
            .addOnFailureListener { e ->
                // Handle failure (fix)
            }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }

    private fun navigateToHome(user: FirebaseUser?) {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
