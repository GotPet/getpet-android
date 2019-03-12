package lt.getpet.getpet

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import lt.getpet.getpet.glide.OptimizedImageSizeUrl
import lt.getpet.getpet.glide.OptimizedImageSizeUrlLoaderFactory
import java.io.InputStream


@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(OptimizedImageSizeUrl::class.java, InputStream::class.java, OptimizedImageSizeUrlLoaderFactory())
    }
}

