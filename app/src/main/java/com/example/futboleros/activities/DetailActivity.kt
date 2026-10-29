package com.example.futboleros.activities

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.futboleros.R
import com.example.futboleros.data.DatabaseHelper
import com.example.futboleros.databinding.ActivityDetailBinding
import com.example.futboleros.utils.ImageUtils
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        // Habilitar la flecha de retroceso en la barra superior (ActionBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_detail)

        @Suppress("DEPRECATION")
        val player = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("EXTRA_PLAYER", Player::class.java)
        } else {
            intent.getSerializableExtra("EXTRA_PLAYER") as? Player
        }

        val name = player?.name ?: intent.getStringExtra("EXTRA_NAME") ?: ""
        val rating = player?.rating ?: intent.getDoubleExtra("EXTRA_RATING", 0.0)
        val photoUrl = player?.photoUrl ?: intent.getStringExtra("EXTRA_IMAGE") ?: intent.getStringExtra("EXTRA_PHOTOURL") ?: ""
        val position = player?.position ?: intent.getStringExtra("EXTRA_POSITION") ?: "Desconocida"
        val country = player?.country ?: intent.getStringExtra("EXTRA_COUNTRY") ?: "Desconocido"
        val age = player?.age ?: intent.getIntExtra("EXTRA_AGE", 0)
        val club = player?.club ?: intent.getStringExtra("EXTRA_CLUB") ?: "Sin equipo"

        binding.tvDetailName.text = name
        binding.tvDetailRating.text = String.format(Locale.US, "Media: %.1f", rating)
        binding.tvDetailPosition.text = "Posición: $position"
        binding.tvDetailCountry.text = "País: $country"
        binding.tvDetailAge.text = if (age > 0) "Edad: $age años" else "Edad: Desconocida"
        binding.tvDetailClub.text = "Equipo: $club"

        ImageUtils.loadImage(binding.ivDetailPhoto, photoUrl)

        binding.btnSave.setOnClickListener {
            showConfirmationDialog(name, rating)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmationDialog(name: String, rating: Double) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title))
            .setMessage(getString(R.string.dialog_message))
            .setPositiveButton(getString(R.string.btn_accept)) { _, _ ->
                dbHelper.addFavorite(name, rating)
                finish()
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }
}
