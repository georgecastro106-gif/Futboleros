package com.example.futboleros.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.futboleros.R
import com.example.futboleros.adapters.ScreenshotAdapter
import com.example.futboleros.data.DatabaseHelper
import com.example.futboleros.databinding.ActivityDetailBinding
import com.example.futboleros.utils.ImageUtils
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var dbHelper: DatabaseHelper
    private var currentPlayer: Player? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inicializar Base de Datos
        dbHelper = DatabaseHelper(this)

        // 1. Recuperar el objeto player del Intent (con compatibilidad de respaldo)
        currentPlayer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("EXTRA_PLAYER", Player::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("EXTRA_PLAYER") as? Player
        }

        val fallbackName = intent.getStringExtra("PLAYER_NAME") ?: "Erling Haaland"
        val fallbackPosition = intent.getStringExtra("PLAYER_POSITION") ?: "Delantero"
        val fallbackRating = intent.getDoubleExtra("PLAYER_RATING", 91.0)
        val fallbackPhotoUrl = intent.getStringExtra("PLAYER_PHOTO_URL") ?: ""

        val name = currentPlayer?.name ?: fallbackName
        val position = currentPlayer?.position ?: fallbackPosition
        val rating = currentPlayer?.rating ?: fallbackRating
        val photoUrl = currentPlayer?.photoUrl ?: fallbackPhotoUrl

        // 2. Asignar los valores a la interfaz estructurada
        binding.tvDetailName.text = name
        binding.tvDetailRating.text = String.format(Locale.US, "Media: %.1f", rating)

        binding.tvDetailPosition.text = "Posición: $position"
        binding.tvDetailCountry.text = "País: ${currentPlayer?.country ?: "Desconocido"}"
        val age = currentPlayer?.age ?: 0
        binding.tvDetailAge.text = if (age > 0) "Edad: $age años" else "Edad: Desconocida"
        binding.tvDetailClub.text = "Equipo: ${currentPlayer?.club ?: "Sin equipo"}"

        // 3. Cargar la foto principal del jugador
        if (photoUrl.isNotEmpty()) {
            ImageUtils.loadImage(binding.ivDetailPhoto, photoUrl)
        }

        // 4. Extraer la lista de screenshots del jugador (o usar la foto principal como respaldo)
        val screenshotsList = if (!currentPlayer?.screenshots.isNullOrEmpty()) {
            currentPlayer!!.screenshots
        } else if (photoUrl.isNotEmpty()) {
            listOf(photoUrl)
        } else {
            emptyList()
        }

        // 5. Configurar el RecyclerView horizontal de screenshots con vista en pantalla completa al tocar
        val screenshotAdapter = ScreenshotAdapter(screenshotsList) { imageUrl ->
            showFullScreenImageDialog(imageUrl)
        }
        binding.recyclerScreenshots.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerScreenshots.adapter = screenshotAdapter

        // 6. Configurar el botón de compartir de la pantalla
        binding.btnShare.setOnClickListener {
            sharePlayerInfo()
        }

        // 7. Botones de acción (Aceptar / Guardar y Volver)
        binding.btnAccept.setOnClickListener {
            showConfirmationDialog(name, position, rating)
        }

        // Botón de volver específico si existe
        if (::binding.isInitialized && binding.root.findViewById<View>(R.id.btnBack) != null) {
            binding.root.findViewById<View>(R.id.btnBack).setOnClickListener {
                finish()
            }
        }
    }

    private fun sharePlayerInfo() {
        val pName = binding.tvDetailName.text.toString()
        val pPosition = binding.tvDetailPosition.text.toString()
        val pCountry = binding.tvDetailCountry.text.toString()
        val pAge = binding.tvDetailAge.text.toString()
        val pClub = binding.tvDetailClub.text.toString()
        val pRating = binding.tvDetailRating.text.toString()
        val photoUrl = currentPlayer?.photoUrl ?: ""

        val shareText = buildString {
            append("⚽ ¡Mira la ficha de $pName en Futboleros!\n\n")
            append("⭐ $pRating\n")
            append("📍 $pPosition\n")
            append("🚩 $pCountry\n")
            append("🎂 $pAge\n")
            append("🛡️ $pClub\n")
            if (photoUrl.isNotBlank()) {
                append("\n📷 Foto: $photoUrl")
            }
        }

        try {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_SUBJECT, "Ficha de $pName - Futboleros")
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Compartir jugador con:")
            startActivity(shareIntent)
        } catch (_: Exception) {
            Toast.makeText(this, "No se pudo abrir la opción de compartir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showFullScreenImageDialog(imageUrl: String) {
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        val imageView = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.FIT_CENTER
            setBackgroundColor(Color.BLACK)
            setOnClickListener { dialog.dismiss() }
        }
        ImageUtils.loadImage(imageView, imageUrl)
        dialog.setContentView(imageView)
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.action_share -> {
                sharePlayerInfo()
                true
            }

            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmationDialog(name: String, position: String, rating: Double) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title))
            .setMessage(getString(R.string.dialog_message))
            .setPositiveButton(getString(R.string.btn_accept)) { _, _ ->
                dbHelper.addFavorite(name, position, rating)
                finish()
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Cierra la actividad actual y regresa al MainActivity
        return true
    }
}
