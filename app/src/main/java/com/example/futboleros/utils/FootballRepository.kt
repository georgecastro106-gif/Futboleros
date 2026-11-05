package com.example.futboleros.utils

import com.example.futboleros.activities.Player

object FootballRepository {

    /**
     * Lista oficial de jugadores con imágenes alojadas en GitHub Raw.
     */
    fun getOfficialFifaPlayers(): List<Player> {
        val baseUrl = "https://raw.githubusercontent.com/georgecastro106-gif/Futboleros/main/"
        return listOf(
            Player(1, "Erling Haaland", "Delantero", 91.0, "${baseUrl}erling-haaland.jpg", "Noruega", 23, "Manchester City", listOf("${baseUrl}erling-haaland.jpg", "${baseUrl}Jude.jpg", "${baseUrl}DeBruyne.jpg")),
            Player(2, "Lionel Messi", "Delantero", 90.0, "${baseUrl}Messi.jpg", "Argentina", 36, "Inter Miami", listOf("${baseUrl}Messi.jpg", "${baseUrl}Pedri.jpeg", "${baseUrl}CR%3A.jpg.jpg")),
            Player(3, "Cristiano Ronaldo", "Delantero", 86.0, "${baseUrl}CR%3A.jpg.jpg", "Portugal", 39, "Al-Nassr", listOf("${baseUrl}CR%3A.jpg.jpg", "${baseUrl}Vini.jpg", "${baseUrl}Mbappe.jpg")),
            Player(4, "Jude Bellingham", "Centrocampista", 90.0, "${baseUrl}Jude.jpg", "Inglaterra", 20, "Real Madrid", listOf("${baseUrl}Jude.jpg", "${baseUrl}Valverde.jpeg", "${baseUrl}Modric.jpeg")),
            Player(5, "Thibaut Courtois", "Portero", 90.0, "${baseUrl}Courtois.jpg", "Bélgica", 31, "Real Madrid", listOf("${baseUrl}Courtois.jpg", "${baseUrl}images-6.jpeg")),
            Player(6, "Kylian Mbappé", "Delantero", 91.0, "${baseUrl}Mbappe.jpg", "Francia", 25, "Real Madrid", listOf("${baseUrl}Mbappe.jpg", "${baseUrl}Vini.jpg", "${baseUrl}Jude.jpg")),
            Player(7, "Kevin De Bruyne", "Centrocampista", 91.0, "${baseUrl}DeBruyne.jpg", "Bélgica", 32, "Manchester City", listOf("${baseUrl}DeBruyne.jpg", "${baseUrl}Rodri.jpg", "${baseUrl}kane.jpg")),
            Player(8, "Harry Kane", "Delantero", 90.0, "${baseUrl}kane.jpg", "Inglaterra", 30, "Bayern München", listOf("${baseUrl}kane.jpg", "${baseUrl}DeBruyne.jpg")),
            Player(9, "Vinícius Jr.", "Delantero", 89.0, "${baseUrl}Vini.jpg", "Brasil", 23, "Real Madrid", listOf("${baseUrl}Vini.jpg", "${baseUrl}Mbappe.jpg", "${baseUrl}Rodri.jpg")),
            Player(10, "Rodri", "Centrocampista", 89.0, "${baseUrl}Rodri.jpg", "España", 27, "Manchester City", listOf("${baseUrl}Rodri.jpg", "${baseUrl}DeBruyne.jpg", "${baseUrl}Pedri.jpeg")),
            Player(11, "Virgil van Dijk", "Defensa", 89.0, "${baseUrl}Van%20Djick.jpg", "Países Bajos", 32, "Liverpool", listOf("${baseUrl}Van%20Djick.jpg", "${baseUrl}Salah.jpg")),
            Player(12, "Mohamed Salah", "Delantero", 89.0, "${baseUrl}Salah.jpg", "Egipto", 31, "Liverpool", listOf("${baseUrl}Salah.jpg", "${baseUrl}Van%20Djick.jpg")),
            Player(13, "Marc-André ter Stegen", "Portero", 89.0, "${baseUrl}images-6.jpeg", "Alemania", 31, "FC Barcelona", listOf("${baseUrl}images-6.jpeg", "${baseUrl}Pedri.jpeg", "${baseUrl}Lamine.jpg")),
            Player(14, "Antoine Griezmann", "Delantero", 88.0, "${baseUrl}Griezman.jpg", "Francia", 33, "Atlético de Madrid", listOf("${baseUrl}Griezman.jpg", "${baseUrl}Lautaro.jpg")),
            Player(15, "Federico Valverde", "Centrocampista", 88.0, "${baseUrl}Valverde.jpeg", "Uruguay", 25, "Real Madrid", listOf("${baseUrl}Valverde.jpeg", "${baseUrl}Jude.jpg", "${baseUrl}Modric.jpeg")),
            Player(16, "Luka Modrić", "Centrocampista", 87.0, "${baseUrl}Modric.jpeg", "Croacia", 38, "Real Madrid", listOf("${baseUrl}Modric.jpeg", "${baseUrl}Valverde.jpeg", "${baseUrl}zidane.jpg")),
            Player(17, "Lautaro Martínez", "Delantero", 87.0, "${baseUrl}Lautaro.jpg", "Argentina", 26, "Inter de Milán", listOf("${baseUrl}Lautaro.jpg", "${baseUrl}Messi.jpg")),
            Player(18, "Pedri", "Centrocampista", 86.0, "${baseUrl}Pedri.jpeg", "España", 21, "FC Barcelona", listOf("${baseUrl}Pedri.jpeg", "${baseUrl}Lamine.jpg", "${baseUrl}images-6.jpeg")),
            Player(19, "Achraf Hakimi", "Defensa", 84.0, "${baseUrl}Hakim.jpg", "Marruecos", 25, "Paris Saint-Germain", listOf("${baseUrl}Hakim.jpg", "${baseUrl}Mbappe.jpg")),
            Player(20, "Zinedine Zidane", "Centrocampista", 90.0, "${baseUrl}zidane.jpg", "Francia", 51, "Leyenda", listOf("${baseUrl}zidane.jpg", "${baseUrl}Modric.jpeg")),
            Player(21, "Lamine Yamal", "Delantero", 91.0, "${baseUrl}Lamine.jpg", "España", 17, "FC Barcelona", listOf("${baseUrl}Lamine.jpg", "${baseUrl}Pedri.jpeg", "${baseUrl}Messi.jpg"))
        )
    }

    /**
     * Mapea un DTO de la API-Football al modelo local Player.
     */
    fun mapDtoToPlayer(dto: PlayerItemDto): Player? {
        val playerDto = dto.player ?: return null
        val stats = dto.statistics?.firstOrNull()
        val posRaw = stats?.games?.position ?: "Desconocida"
        
        val position = when (posRaw.lowercase()) {
            "attacker", "forward" -> "Delantero"
            "midfielder" -> "Centrocampista"
            "defender" -> "Defensa"
            "goalkeeper" -> "Portero"
            else -> posRaw
        }

        val rating = stats?.games?.rating?.toDoubleOrNull() ?: 80.0
        val photo = playerDto.photo ?: "https://raw.githubusercontent.com/georgecastro106-gif/Futboleros/main/Messi.jpg"

        return Player(
            id = playerDto.id,
            name = playerDto.name ?: "Desconocido",
            position = position,
            rating = rating,
            photoUrl = photo,
            country = playerDto.nationality ?: "Desconocido",
            age = playerDto.age ?: 0,
            club = stats?.team?.name ?: "Sin equipo",
            screenshots = listOf(photo)
        )
    }
}

