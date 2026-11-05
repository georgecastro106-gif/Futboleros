package com.example.futboleros.activities

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.futboleros.R
import com.example.futboleros.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // URL directa (Raw) de la imagen de portada en GitHub
        val urlImagenPortada = "https://raw.githubusercontent.com/georgecastro106-gif/Futboleros/main/FUTBOLEROS.jpg"

        // Cargar la imagen de internet y activar animación de deslizamiento suave
        Glide.with(this)
            .load(urlImagenPortada)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.imgLogo)

        // Animación de deslizamiento automático panorámico al cargar la imagen
        var animatorStarted = false
        binding.imgLogo.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            if (!animatorStarted) {
                val childWidth = binding.imgLogo.width
                val scrollViewWidth = binding.horizontalScrollView.width
                val maxScroll = childWidth - scrollViewWidth
                if (maxScroll > 0) {
                    animatorStarted = true
                    ObjectAnimator.ofInt(
                        binding.horizontalScrollView,
                        "scrollX",
                        0,
                        maxScroll
                    ).apply {
                        duration = 15000 // 15 segundos de deslizamiento panorámico suave
                        repeatCount = ValueAnimator.INFINITE
                        repeatMode = ValueAnimator.REVERSE
                        interpolator = LinearInterpolator()
                        start()
                    }
                }
            }
        }

        // Al hacer clic en "EMPEZAR", pasamos a la pantalla de Login
        binding.btnStart.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
