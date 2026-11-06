package com.example.futboleros.activities
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.futboleros.R
import com.example.futboleros.adapters.PlayerAdapter
import com.example.futboleros.data.PlayerDataSource
import com.example.futboleros.databinding.ActivityMainBinding
import com.example.futboleros.utils.RetrofitClient
import com.example.futboleros.utils.ApiFootballResponse

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PlayerAdapter
    private var fullPlayerList: List<Player> = emptyList()

    private var currentPositionFilter: String = ""
    private var currentCountryFilter: String = "Todos los países"
    private var currentSortOption: String = "Mayor valoración"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Listener para los Chips de posición
        binding.chipGroupPosition.setOnCheckedChangeListener { _, checkedId ->
            currentPositionFilter = when (checkedId) {
                R.id.chipForward -> "Delantero"
                R.id.chipMidfielder -> "Centrocampista"
                R.id.chipDefender -> "Defensa"
                R.id.chipGoalkeeper -> "Portero"
                else -> "" // "Todos"
            }
            applyFiltersAndSort()
        }

        // Configurar los Spinners de País y Ordenación
        val countries = arrayOf(
            "Todos los países",
            "Argentina",
            "Portugal",
            "Inglaterra",
            "Bélgica",
            "Francia",
            "España",
            "Brasil",
            "Países Bajos",
            "Egipto",
            "Alemania",
            "Uruguay",
            "Croacia",
            "Marruecos"
        )
        val countryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            countries
        )
        binding.spinnerCountry.adapter = countryAdapter

        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentCountryFilter = countries[position]
                applyFiltersAndSort()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val sortOptions = arrayOf("Mayor valoración", "Menor valoración", "Nombre (A-Z)")
        val sortAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            sortOptions
        )
        binding.spinnerSort.adapter = sortAdapter

        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentSortOption = sortOptions[position]
                applyFiltersAndSort()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Configurar la Toolbar
        setSupportActionBar(binding.toolbar)

        // Habilitar el botón de retroceso (flecha hacia atrás)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Futboleros"

        // Configurar el LayoutManager del RecyclerView
        binding.recyclerViewPlayers.layoutManager = LinearLayoutManager(this)

        // Cargar los jugadores desde la API (con fallback al JSON local)
        fetchPlayers()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.queryHint = "Buscar jugador..."

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText.orEmpty().lowercase()

                val filteredList = fullPlayerList.filter { player ->
                    player.name.lowercase().contains(query) ||
                            player.club.lowercase().contains(query) ||
                            player.position.lowercase().contains(query) ||
                            player.country.lowercase().contains(query)
                }

                if (::adapter.isInitialized) {
                    adapter.filterList(filteredList)
                }
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareAppOrPlayers()
                true
            }

            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_saved_players -> {
                val intent = Intent(this, SavedPlayersActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareAppOrPlayers() {
        val topPlayer = fullPlayerList.maxByOrNull { it.rating }
        val shareText = buildString {
            append("⚽ ¡Descubre la app Futboleros y su ranking de futbolistas!\n\n")
            if (topPlayer != null) {
                append("🏆 Jugador destacado: ${topPlayer.name} (Media ${topPlayer.rating})\n")
                append("🛡️ Club: ${topPlayer.club} | ${topPlayer.position}\n\n")
            }
            append("📊 Explora las fichas, estadísticas y fotos de los mejores jugadores del mundo.\n\n")
            append("🔗 Ver más en: https://github.com/georgecastro106-gif/Futboleros")
        }

        try {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_SUBJECT, "Futboleros - Clasificación de Futbolistas")
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Compartir Futboleros con:")
            startActivity(shareIntent)
        } catch (_: Exception) {
            Toast.makeText(this, "No se pudo abrir la opción de compartir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchPlayers() {
        // 1. CARGA INMEDIATA DE SEGURIDAD: Mostramos tus datos locales (JSON/GitHub) al instante
        // para que la interfaz nunca se quede vacía ni dependa de que el servidor responda.
        fullPlayerList = PlayerDataSource.players.sortedBy { it.name }

        adapter = PlayerAdapter(fullPlayerList) { player ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                putExtra("EXTRA_PLAYER", player)
            }
            startActivity(intent)
        }

        binding.recyclerViewPlayers.adapter = adapter
        applyFiltersAndSort()

        // 2. INTENTO OPCIONAL DE LA API: En segundo plano intentamos conectar con la API.
        // Si la API responde correctamente en algún momento, actualizará la lista de forma dinámica.
        val apiKey = "e991c381ddeaa988abc587e7828c8ac1"

        RetrofitClient.apiService.searchPlayers(searchName = "a", apiKey = apiKey)
            .enqueue(object : retrofit2.Callback<ApiFootballResponse> {
                override fun onResponse(
                    call: retrofit2.Call<ApiFootballResponse>,
                    response: retrofit2.Response<ApiFootballResponse>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        val playerItems = apiResponse?.response

                        val apiPlayerList = playerItems?.mapNotNull { item ->
                            val p = item.player
                            val stat = item.statistics?.firstOrNull()

                            if (p != null) {
                                Player(
                                    name = p.name ?: "Desconocido",
                                    position = stat?.games?.position ?: "N/A",
                                    country = p.nationality ?: "Desconocido",
                                    age = p.age ?: 0,
                                    club = stat?.team?.name ?: "Sin equipo",
                                    rating = stat?.games?.rating?.toDoubleOrNull() ?: 0.0,
                                    photoUrl = p.photo ?: ""
                                )
                            } else null
                        } ?: emptyList()

                        // Si la API devuelve datos con éxito, actualizamos el adaptador en caliente
                        if (apiPlayerList.isNotEmpty()) {
                            fullPlayerList = apiPlayerList.sortedBy { it.name }
                            runOnUiThread {
                                if (::adapter.isInitialized) {
                                    applyFiltersAndSort()
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: retrofit2.Call<ApiFootballResponse>, t: Throwable) {
                    // Si falla la red, no hacemos nada disruptivo; la app se queda con los datos locales tranquilamente.
                    android.util.Log.d("API_FALLBACK", "Usando datos locales por fallo de red: ${t.message}")
                }
            })
    }

    private fun filterByPosition(positionName: String) {
        val filteredList = if (positionName.isEmpty()) {
            fullPlayerList
        } else {
            fullPlayerList.filter { player ->
                player.position.equals(positionName, ignoreCase = true)
            }
        }
        if (::adapter.isInitialized) {
            adapter.filterList(filteredList)
        }
    }

    private fun applyFiltersAndSort() {
        var filteredList = fullPlayerList

        // 1. Filtrar por posición
        if (currentPositionFilter.isNotEmpty()) {
            filteredList = filteredList.filter {
                it.position.equals(currentPositionFilter, ignoreCase = true)
            }
        }

        // 2. Filtrar por país
        if (currentCountryFilter != "Todos los países") {
            filteredList = filteredList.filter {
                it.country.equals(currentCountryFilter, ignoreCase = true)
            }
        }

        // 3. Ordenar
        filteredList = when (currentSortOption) {
            "Mayor valoración" -> filteredList.sortedByDescending { it.rating }
            "Menor valoración" -> filteredList.sortedBy { it.rating }
            "Nombre (A-Z)" -> filteredList.sortedBy { it.name }
            else -> filteredList
        }

        // 4. Actualizar el adaptador
        if (::adapter.isInitialized) {
            adapter.filterList(filteredList)
        }
    }
}