package com.example.agriscan.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.agriscan.databinding.ActivitySignInBinding
import com.example.agriscan.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    private val PREFS_NAME = "MyPrefs"
    private val KEY_REMEMBER_ME = "remember_me"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        playAnimation()

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean(KEY_REMEMBER_ME, false)) {
            goToMainActivity()
        }

        val emailFromSignUp = intent.getStringExtra("email")
        val passwordFromSignUp = intent.getStringExtra("password")

        if (!emailFromSignUp.isNullOrEmpty() && !passwordFromSignUp.isNullOrEmpty()) {
            binding.etEmail.setText(emailFromSignUp)
            binding.etPassword.setText(passwordFromSignUp)
            Toast.makeText(
                this,
                "Account created successfully. Please sign in.",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.etEmail.error = "Email is required"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.etPassword.error = "Password is required"
                return@setOnClickListener
            }

            showLoading(true)

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    if (binding.cbRememberMe.isChecked) {
                        sharedPreferences.edit()
                            .putBoolean(KEY_REMEMBER_ME, true)
                            .apply()
                    }
                    goToMainActivity()
                } else {
                    val errorMessage = when {
                        task.exception?.message?.contains("There is no user record") == true -> "Email is not registered"
                        task.exception?.message?.contains("The password is invalid") == true -> "Incorrect password"
                        else -> task.exception.toString()
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun playAnimation() {
        val email = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(200)
        val password = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(200)
        val rememberMe = ObjectAnimator.ofFloat(binding.cbRememberMe, View.ALPHA, 1f).setDuration(200)
        val forgotPassword = ObjectAnimator.ofFloat(binding.tvForgotPassword, View.ALPHA, 1f).setDuration(200)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(200)
        val signUp = ObjectAnimator.ofFloat(binding.tvSignUp, View.ALPHA, 1f).setDuration(200)


        AnimatorSet().apply {
            playSequentially( email, password, rememberMe, forgotPassword, login, signUp)
            start()
        }
    }

    private fun showForgotPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reset Password")
        builder.setMessage("Masukkan email Anda dengan benar untuk menerima tautan reset password. Pastikan Anda memasukkan email yang terdaftar dan jangan typo.")

        val input = android.widget.EditText(this)
        input.hint = "Email"
        input.inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        builder.setView(input)

        builder.setPositiveButton("Kirim") { _, _ ->
            val email = input.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Email tidak boleh kosong.", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Format email tidak valid.", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            sendResetPasswordEmail(email)
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun sendResetPasswordEmail(email: String) {
        showLoading(true)

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { resetTask ->
                showLoading(false)
                if (resetTask.isSuccessful) {
                    Toast.makeText(this, "Tautan reset password telah dikirim ke email.", Toast.LENGTH_LONG).show()
                } else {
                    val exceptionMessage = resetTask.exception?.message
                    Log.e("ResetPasswordError", "Error: $exceptionMessage")
                    Toast.makeText(this, "Gagal mengirim tautan reset password. Coba lagi.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.etEmail.isEnabled = !isLoading
        binding.etPassword.isEnabled = !isLoading
        binding.btnLogin.isEnabled = !isLoading
        binding.tvForgotPassword.isEnabled = !isLoading
        binding.tvSignUp.isEnabled = !isLoading
        binding.cbRememberMe.isEnabled = !isLoading
    }
}