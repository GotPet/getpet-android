package lt.getpet.getpet.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import java.io.InputStream


class OptimizedImageSizeUrlLoader(
        concreteLoader: ModelLoader<GlideUrl, InputStream>,
        modelCache: ModelCache<OptimizedImageSizeUrl, GlideUrl>?
) : BaseGlideUrlLoader<OptimizedImageSizeUrl>(concreteLoader, modelCache) {

    override fun getUrl(model: OptimizedImageSizeUrl, width: Int, height: Int, options: Options?): String {
        return model.optimizedImageSizeUrl(width, height)
    }

    override fun handles(model: OptimizedImageSizeUrl): Boolean {
        return true
    }

}
