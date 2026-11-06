package com.example.futboleros.activities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "players_table")
data class Player(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val position: String,
    val rating: Double,
    val photoUrl: String,
    val country: String = "Desconocido",
    val age: Int = 0,
    val club: String = "Sin equipo",
    val screenshots: List<String> = emptyList(),
) : Serializable

