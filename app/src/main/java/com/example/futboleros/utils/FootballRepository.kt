package com.example.futboleros.utils

import com.example.futboleros.activities.Player

object FootballRepository {

    /**
     * Lista oficial de jugadores de la FIFA con imágenes reales públicas de Wikimedia Commons.
     */
    fun getOfficialFifaPlayers(): List<Player> {
        return listOf(
            Player(1, "Kylian Mbappé", "Delantero", 91.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/5/57/2019-01-19_FC_Bayer_04_Leverkusen_vs._FC_Bayern_M%C3%BCnchen_%28Kylian_Mbapp%C3%A9%29_by_Sven_Mandel-001.jpg/500px-2019-01-19_FC_Bayer_04_Leverkusen_vs._FC_Bayern_M%C3%BCnchen_%28Kylian_Mbapp%C3%A9%29_by_Sven_Mandel-001.jpg", "Francia", 25, "Real Madrid"),
            Player(2, "Erling Haaland", "Delantero", 91.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Erling_Haaland_2023.jpg/500px-Erling_Haaland_2023.jpg", "Noruega", 23, "Manchester City"),
            Player(3, "Kevin De Bruyne", "Centrocampista", 91.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/Kevin_De_Bruyne_2018.jpg/500px-Kevin_De_Bruyne_2018.jpg", "Bélgica", 32, "Manchester City"),
            Player(4, "Lionel Messi", "Delantero", 90.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Lionel_Messi_20180626.jpg/500px-Lionel_Messi_20180626.jpg", "Argentina", 36, "Inter Miami"),
            Player(5, "Jude Bellingham", "Centrocampista", 90.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/2/23/Jude_Bellingham_2023.jpg/500px-Jude_Bellingham_2023.jpg", "Inglaterra", 20, "Real Madrid"),
            Player(6, "Vinícius Jr.", "Delantero", 89.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Vinicius_Jr_2021.jpg/500px-Vinicius_Jr_2021.jpg", "Brasil", 23, "Real Madrid"),
            Player(7, "Harry Kane", "Delantero", 90.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/2/22/Harry_Kane_2018.jpg/500px-Harry_Kane_2018.jpg", "Inglaterra", 30, "Bayern München"),
            Player(8, "Rodri", "Centrocampista", 89.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/8/86/Rodri_2023.jpg/500px-Rodri_2023.jpg", "España", 27, "Manchester City"),
            Player(9, "Thibaut Courtois", "Portero", 90.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c4/Thibaut_Courtois_2018.jpg/500px-Thibaut_Courtois_2018.jpg", "Bélgica", 31, "Real Madrid"),
            Player(10, "Virgil van Dijk", "Defensa", 89.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/Virgil_van_Dijk_2018.jpg/500px-Virgil_van_Dijk_2018.jpg", "Países Bajos", 32, "Liverpool"),
            Player(11, "Pedri", "Centrocampista", 86.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/8/81/Pedri_2021.jpg/500px-Pedri_2021.jpg", "España", 21, "FC Barcelona"),
            Player(12, "Cristiano Ronaldo", "Delantero", 86.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8c/Cristiano_Ronaldo_2018.jpg/500px-Cristiano_Ronaldo_2018.jpg", "Portugal", 39, "Al-Nassr"),
            Player(13, "Mohamed Salah", "Delantero", 89.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/Mohamed_Salah_2018.jpg/500px-Mohamed_Salah_2018.jpg", "Egipto", 31, "Liverpool"),
            Player(14, "Luka Modrić", "Centrocampista", 87.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e9/Luka_Modric_2018.jpg/500px-Luka_Modric_2018.jpg", "Croacia", 38, "Real Madrid"),
            Player(15, "Ruben Dias", "Defensa", 89.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/R%C3%BAben_Dias_2021.jpg/500px-R%C3%BAben_Dias_2021.jpg", "Portugal", 26, "Manchester City"),
            Player(16, "Marc-André ter Stegen", "Portero", 89.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Marc-Andr%C3%A9_ter_Stegen_2018.jpg/500px-Marc-Andr%C3%A9_ter_Stegen_2018.jpg", "Alemania", 31, "FC Barcelona"),
            Player(17, "Antoine Griezmann", "Delantero", 88.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Antoine_Griezmann_2018.jpg/500px-Antoine_Griezmann_2018.jpg", "Francia", 33, "Atlético de Madrid"),
            Player(18, "Federico Valverde", "Centrocampista", 88.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Federico_Valverde_2022.jpg/500px-Federico_Valverde_2022.jpg", "Uruguay", 25, "Real Madrid"),
            Player(19, "Achraf Hakimi", "Defensa", 84.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b3/Achraf_Hakimi_2021.jpg/500px-Achraf_Hakimi_2021.jpg", "Marruecos", 25, "Paris Saint-Germain"),
            Player(20, "Lautaro Martínez", "Delantero", 87.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/Lautaro_Mart%C3%ADnez_2018.jpg/500px-Lautaro_Mart%C3%ADnez_2018.jpg", "Argentina", 26, "Inter de Milán")
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

        return Player(
            id = playerDto.id,
            name = playerDto.name ?: "Desconocido",
            position = position,
            rating = rating,
            photoUrl = playerDto.photo ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Lionel_Messi_20180626.jpg/500px-Lionel_Messi_20180626.jpg",
            country = playerDto.nationality ?: "Desconocido",
            age = playerDto.age ?: 0,
            club = stats?.team?.name ?: "Sin equipo"
        )
    }
}
