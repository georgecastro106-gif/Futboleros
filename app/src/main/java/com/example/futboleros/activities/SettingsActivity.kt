package com.example.futboleros.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.futboleros.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Título en la barra superior
        supportActionBar?.title = "Ajustes"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inicializar el estado del switch según el modo actual
        val isNightMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        binding.switchDarkMode.isChecked = isNightMode

        // Lógica del Switch para cambiar el Modo Oscuro / Claro
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Lógica para el botón de "Acerca de"
        binding.tvAbout.setOnClickListener {
            Toast.makeText(this, "Futboleros App - Creado con Kotlin y RecyclerView", Toast.LENGTH_LONG).show()
        }
    }

    // Permitir que la flecha hacia atrás de la barra funcione
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
