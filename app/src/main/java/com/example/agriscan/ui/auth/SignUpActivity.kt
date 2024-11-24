package com.example.agriscan.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agriscan.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        playAnimation()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.etEmail.error = "Email is required"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.etPassword.error = "Password is required"
                return@setOnClickListener
            }
            if (confirmPassword.isEmpty()) {
                binding.etConfirmPassword.error = "Confirm Password is required"
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.etConfirmPassword.error = "Passwords do not match"
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                    intent.putExtra("email", email)
                    intent.putExtra("password", password)
                    startActivity(intent)
                    finish()
                } else {
                    if (task.exception?.message?.contains("The email address is already in use") == true) {
                        Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.tvSignIn.setOnClickListener {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun playAnimation() {
        val email = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(200)
        val password = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(200)
        val confirmPassword = ObjectAnimator.ofFloat(binding.etConfirmPassword, View.ALPHA, 1f).setDuration(200)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(200)
        val signIn = ObjectAnimator.ofFloat(binding.tvSignIn, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(email, password, confirmPassword, login, signIn)
            start()
        }
    }
}
