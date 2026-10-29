package com.example.futboleros.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.futboleros.R
import com.example.futboleros.activities.Player
import com.example.futboleros.databinding.ItemPlayerBinding
import com.example.futboleros.utils.ImageUtils
import java.util.Locale

class PlayerAdapter(
    private var players: List<Player>,
    private val onClick: (Player) -> Unit
) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    inner class PlayerViewHolder(val binding: ItemPlayerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = players[position]
        holder.binding.tvPlayerName.text = player.name
        
        val ageText = if (player.age > 0) "${player.age} años" else ""
        val countryAndAge = listOf(player.country, ageText).filter { it.isNotEmpty() }.joinToString(" • ")
        holder.binding.tvPlayerSubDetails.text = countryAndAge
        
        val posAndClub = listOf(player.position, player.club).filter { it.isNotEmpty() }.joinToString(" | ")
        holder.binding.tvPlayerPosition.text = posAndClub

        holder.binding.tvPlayerRating.text = String.format(Locale.US, "%.1f", player.rating)

        // Cargar imagen con ImageUtils (incluye cabecera User-Agent para evitar errores 403)
        ImageUtils.loadImage(holder.binding.ivPlayerPhoto, player.photoUrl)

        // Capturar clic en la celda usando lambda
        holder.itemView.setOnClickListener {
            onClick(player)
        }
    }

    override fun getItemCount() = players.size

    fun updateList(newList: List<Player>) {
        players = newList
        notifyDataSetChanged()
    }
}
