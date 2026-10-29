package com.example.futboleros.activities

import java.io.Serializable

data class Player(
    val id: Int,
    val name: String,
    val position: String,
    val rating: Double,
    val photoUrl: String,
    val country: String = "Desconocido",
    val age: Int = 0,
    val club: String = "Sin equipo"
) : Serializable
