package com.example.futboleros.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.futboleros.activities.Player

@Dao
interface PlayerDao {

    // Función para insertar o actualizar un jugador en la base de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Player)

    // Función opcional para consultar todos los jugadores guardados
    @Query("SELECT * FROM players_table")
    suspend fun getAllSavedPlayers(): List<Player>

    @Delete
    suspend fun deletePlayer(player: Player)


}