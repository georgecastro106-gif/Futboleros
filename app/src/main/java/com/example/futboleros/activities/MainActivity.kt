package com.example.futboleros.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.futboleros.BuildConfig
import com.example.futboleros.R
import com.example.futboleros.adapters.PlayerAdapter
import com.example.futboleros.databinding.ActivityMainBinding
import com.example.futboleros.utils.ApiFootballResponse
import com.example.futboleros.utils.FootballApiService
import com.example.futboleros.utils.FootballRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var playerAdapter: PlayerAdapter

    private var fullPlayerList: List<Player> = emptyList()
    private var currentQuery: String = ""
    private var selectedPositionFilter: String = "Todos"
    private var selectedCountryFilter: String = "Todos los países"
    private var selectedSortOption: String = "Mayor valoración"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        binding.recyclerViewPlayers.layoutManager = LinearLayoutManager(this)

        setupFilterListeners()
        fetchPlayers()
    }

    private fun fetchPlayers() {
        // Intentar obtener datos de la API de Football o utilizar el dataset oficial FIFA
        val apiKey = try {
            val field = BuildConfig::class.java.getField("FOOTBALL_API_KEY")
            field.get(null) as? String ?: ""
        } catch (_: Exception) {
            ""
        }

        if (apiKey.isNotEmpty()) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://v3.api-football.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(FootballApiService::class.java)
            // Real Madrid id=541 temporada 2023 como ejemplo
            service.getPlayersByTeam(541, 2023, apiKey).enqueue(object : Callback<ApiFootballResponse> {
                override fun onResponse(call: Call<ApiFootballResponse>, response: Response<ApiFootballResponse>) {
                    val dtos = response.body()?.response
                    if (!dtos.isNullOrEmpty()) {
                        val apiPlayers = dtos.mapNotNull { FootballRepository.mapDtoToPlayer(it) }
                        if (apiPlayers.isNotEmpty()) {
                            onPlayersLoaded(apiPlayers)
                            return
                        }
                    }
                    onPlayersLoaded(FootballRepository.getOfficialFifaPlayers())
                }

                override fun onFailure(call: Call<ApiFootballResponse>, t: Throwable) {
                    onPlayersLoaded(FootballRepository.getOfficialFifaPlayers())
                }
            })
        } else {
            // Cargar dataset oficial completo de jugadores FIFA
            onPlayersLoaded(FootballRepository.getOfficialFifaPlayers())
        }
    }

    private fun onPlayersLoaded(players: List<Player>) {
        fullPlayerList = players

        // Configurar Spinner de Países dinámicamente
        val countries = mutableListOf("Todos los países")
        countries.addAll(players.map { it.country }.filter { it.isNotBlank() && it != "Desconocido" }.distinct().sorted())
        val countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountry.adapter = countryAdapter

        // Configurar Spinner de Ordenación
        val sortOptions = listOf(
            "Mayor valoración",
            "Menor edad",
            "Mayor edad",
            "Nombre A-Z",
            "País"
        )
        val sortAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSort.adapter = sortAdapter

        // Inicializar el adaptador del RecyclerView
        playerAdapter = PlayerAdapter(emptyList()) { selectedPlayer ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                putExtra("EXTRA_PLAYER", selectedPlayer)
            }
            startActivity(intent)
        }
        binding.recyclerViewPlayers.adapter = playerAdapter

        applyFiltersAndSorting()
    }

    private fun setupFilterListeners() {
        // Listener para Chips de Posición
        binding.chipGroupPosition.setOnCheckedStateChangeListener { _, checkedIds ->
            selectedPositionFilter = when {
                checkedIds.contains(R.id.chipForward) -> "Delantero"
                checkedIds.contains(R.id.chipMidfielder) -> "Centrocampista"
                checkedIds.contains(R.id.chipDefender) -> "Defensa"
                checkedIds.contains(R.id.chipGoalkeeper) -> "Portero"
                else -> "Todos"
            }
            applyFiltersAndSorting()
        }

        // Listener para Spinner de País
        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCountryFilter = parent?.getItemAtPosition(position).toString()
                applyFiltersAndSorting()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Listener para Spinner de Ordenación
        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSortOption = parent?.getItemAtPosition(position).toString()
                applyFiltersAndSorting()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun applyFiltersAndSorting() {
        if (!::playerAdapter.isInitialized) return

        var list = fullPlayerList.toList()

        // 1. Filtrar por Búsqueda (nombre, posición, país, club)
        if (currentQuery.isNotEmpty()) {
            list = list.filter { player ->
                player.name.contains(currentQuery, ignoreCase = true) ||
                        player.position.contains(currentQuery, ignoreCase = true) ||
                        player.country.contains(currentQuery, ignoreCase = true) ||
                        player.club.contains(currentQuery, ignoreCase = true)
            }
        }

        // 2. Filtrar por Posición
        if (selectedPositionFilter != "Todos") {
            list = list.filter { player ->
                player.position.equals(selectedPositionFilter, ignoreCase = true) ||
                        (selectedPositionFilter == "Delantero" && player.position.contains("Delantero", ignoreCase = true)) ||
                        (selectedPositionFilter == "Centrocampista" && player.position.contains("Centrocampista", ignoreCase = true)) ||
                        (selectedPositionFilter == "Defensa" && player.position.contains("Defensa", ignoreCase = true)) ||
                        (selectedPositionFilter == "Portero" && player.position.contains("Portero", ignoreCase = true))
            }
        }

        // 3. Filtrar por País
        if (selectedCountryFilter != "Todos los países") {
            list = list.filter { player ->
                player.country.equals(selectedCountryFilter, ignoreCase = true)
            }
        }

        // 4. Ordenación
        list = when (selectedSortOption) {
            "Mayor valoración" -> list.sortedByDescending { it.rating }
            "Menor edad" -> list.sortedBy { if (it.age > 0) it.age else 99 }
            "Mayor edad" -> list.sortedByDescending { it.age }
            "Nombre A-Z" -> list.sortedBy { it.name }
            "País" -> list.sortedBy { it.country }
            else -> list.sortedByDescending { it.rating }
        }

        playerAdapter.updateList(list)
    }

    // Menú en la AppBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentQuery = newText.orEmpty().trim()
                applyFiltersAndSorting()
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                sharedPreferences.edit { clear() }
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
