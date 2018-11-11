package lt.getpet.getpet.dagger.module

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lt.getpet.getpet.dagger.DbScheduler
import lt.getpet.getpet.dagger.IoScheduler
import lt.getpet.getpet.dagger.UiScheduler
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    @IoScheduler
    fun provideIoScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Singleton
    @UiScheduler
    fun provideUiScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    @Singleton
    @DbScheduler
    fun provideDbScheduler(): Scheduler {
        return Schedulers.single()
    }

    @Provides
    @Singleton
    fun providesMoshi(): Moshi {
        return Moshi.Builder().build()
    }
}