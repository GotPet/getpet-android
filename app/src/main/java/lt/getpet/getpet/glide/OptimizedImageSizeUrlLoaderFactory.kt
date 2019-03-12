package lt.getpet.getpet.glide

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import java.io.InputStream


class OptimizedImageSizeUrlLoaderFactory : ModelLoaderFactory<OptimizedImageSizeUrl, InputStream> {

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<OptimizedImageSizeUrl, InputStream> {
        val modelLoader =
                multiFactory.build<GlideUrl, InputStream>(GlideUrl::class.java, InputStream::class.java)
        return OptimizedImageSizeUrlLoader(modelLoader, null)
    }

    override fun teardown() {}
}