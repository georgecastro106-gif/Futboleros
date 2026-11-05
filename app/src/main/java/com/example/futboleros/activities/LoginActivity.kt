package com.example.futboleros.activities

import com.example.futboleros.activities.MainActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.futboleros.databinding.ActivityLoginBinding
import kotlin.jvm.java

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

        // Botón de retroceso (icono superior y botón de texto)
        binding.btnBack.setOnClickListener {
            navigateToSplash()
        }

        binding.btnBackToSplash.setOnClickListener {
            navigateToSplash()
        }

        // Manejar el botón de retroceso del sistema
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToSplash()
            }
        })

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

    private fun navigateToSplash() {
        val intent = Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToMain() {
        // Navegar a la actividad principal (MainActivity)
        val intent =Intent(this, MainActivity::class.java)
                startActivity(intent)
        finish()
    }
}


