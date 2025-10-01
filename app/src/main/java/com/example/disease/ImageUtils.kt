package com.example.disease



import android.content.Context
import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest

object ImageUtils {
    fun loadSvgFromAssets(context: Context, imageView: ImageView, fileName: String) {
        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()

        val request = ImageRequest.Builder(context)
            .data("file:///android_asset/$fileName")
            .target(imageView)
            .build()

        imageLoader.enqueue(request)
    }
}