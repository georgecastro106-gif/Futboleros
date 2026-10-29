package com.example.futboleros.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.futboleros.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)

        // Verificar si la sesión ya está activa
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            navigateToMain()
            return
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            if (username.isNotEmpty()) {
                // Guardar el nombre de usuario en SharedPreferences
                sharedPreferences.edit().apply {
                    putString("username", username)
                    putBoolean("isLoggedIn", true)
                    apply()
                }
                navigateToMain()
            }
        }
    }

    private fun navigateToMain() {
        // Navegar a la actividad principal (MainActivity)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}


