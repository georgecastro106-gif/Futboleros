package com.example.futboleros.utils

import com.example.futboleros.activities.Player
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

data class ApiFootballResponse(
    @SerializedName("response") val response: List<PlayerItemDto>?
)

data class PlayerItemDto(
    @SerializedName("player") val player: PlayerDto?,
    @SerializedName("statistics") val statistics: List<StatisticDto>?
)

data class PlayerDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("age") val age: Int?,
    @SerializedName("nationality") val nationality: String?,
    @SerializedName("photo") val photo: String?
)

data class StatisticDto(
    @SerializedName("team") val team: TeamDto?,
    @SerializedName("games") val games: GamesDto?
)

data class TeamDto(
    @SerializedName("name") val name: String?
)

data class GamesDto(
    @SerializedName("position") val position: String?,
    @SerializedName("rating") val rating: String?
)

interface FootballApiService {

    @GET("v3/players")
    fun getPlayersByTeam(
        @Query("team") teamId: Int,
        @Query("season") season: Int = 2023,
        @Header("x-apisports-key") apiKey: String
    ): Call<ApiFootballResponse>

    @GET("v3/players")
    fun searchPlayers(
        @Query("search") searchName: String,
        @Header("x-apisports-key") apiKey: String
    ): Call<ApiFootballResponse>

    @GET("players.json")
    fun getPlayers(): Call<List<Player>>
}
