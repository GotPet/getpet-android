package lt.getpet.getpet.glide

import android.net.Uri

class OptimizedImageSizeUrl(private val imageUrl: String) {

    fun optimizedImageSizeUrl(width: Int, height: Int): String {
        return Uri.parse(imageUrl).buildUpon()
                .appendQueryParameter("w", width.toString())
                .appendQueryParameter("h", height.toString())
                .toString()
    }
}