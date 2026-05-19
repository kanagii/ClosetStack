package com.example.closetstack

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Sign up button - to be implemented
        findViewById<MaterialButton>(R.id.btnSignUp).setOnClickListener {
            Toast.makeText(this, "Sign Up will be implemented soon!", Toast.LENGTH_SHORT).show()
        }

        // Login link - navigate back to LoginActivity
        findViewById<TextView>(R.id.tvLogin).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Forgot password - to be implemented
        findViewById<TextView>(R.id.tvForgotPassword).setOnClickListener {
            Toast.makeText(this, "Forgot Password will be implemented soon!", Toast.LENGTH_SHORT).show()
        }
    }
}
