package com.example.futboleros.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import com.example.futboleros.data.AppDatabase
import com.example.futboleros.activities.Player
import com.example.futboleros.adapters.PlayerAdapter
import com.example.futboleros.databinding.ActivitySavedPlayersBinding
import kotlinx.coroutines.launch

class SavedPlayersActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedPlayersBinding
    private lateinit var adapter: PlayerAdapter // El adaptador que ya usas para los jugadores

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar la barra superior y la flecha de retroceso
        setSupportActionBar(binding.toolbarSaved)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Configurar RecyclerView
        binding.recyclerSavedPlayers.layoutManager = LinearLayoutManager(this)

        // Cargar los jugadores guardados de la base de datos
        loadSavedPlayers()
    }

    private fun loadSavedPlayers() {
        val database = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val savedList = database.playerDao().getAllSavedPlayers()

            // Inicializamos el adaptador con los datos de Room
            adapter = PlayerAdapter(savedList) { player ->
                // Opcional: Al hacer clic en un jugador guardado, puedes abrir detalles o permitir eliminarlo
                deletePlayerConfirmation(player)
            }
            binding.recyclerSavedPlayers.adapter = adapter
        }
    }

    private fun deletePlayerConfirmation(player: Player) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Eliminar jugador")
            .setMessage("¿Deseas eliminar a ${player.name} de tus guardados?")
            .setPositiveButton("Eliminar") { _, _ ->
                val database = AppDatabase.getDatabase(this)
                lifecycleScope.launch {
                    database.playerDao().deletePlayer(player)
                    loadSavedPlayers() // Recargar la lista tras borrar
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}