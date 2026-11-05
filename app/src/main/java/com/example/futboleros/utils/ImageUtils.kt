package com.example.futboleros.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.futboleros.R

object ImageUtils {

    fun loadImage(imageView: ImageView, url: String?) {
        if (url.isNullOrBlank()) {
            imageView.setImageResource(R.drawable.ic_launcher_background)
            return
        }

        // Convertir automáticamente URLs de vista previa de GitHub (/blob/) a imágenes raw
        val formattedUrl = if (url.contains("github.com") && url.contains("/blob/")) {
            url.replace("github.com", "raw.githubusercontent.com")
                .replace("/blob/", "/")
        } else {
            url
        }

        val headers = LazyHeaders.Builder()
            .addHeader("User-Agent", "FutbolerosApp/1.0 (AndroidEducationalApp; contacto@futboleros.com)")
            .addHeader("Referer", "https://commons.wikimedia.org")
            .addHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
            .build()

        val glideUrl = GlideUrl(formattedUrl, headers)

        Glide.with(imageView.context)
            .load(glideUrl)
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(imageView)
    }
}
