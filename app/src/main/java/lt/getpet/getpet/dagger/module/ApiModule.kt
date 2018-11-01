package lt.getpet.getpet.dagger.module

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import lt.getpet.getpet.BuildConfig
import lt.getpet.getpet.dagger.IoScheduler
import lt.getpet.getpet.network.PetApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideMoshiFactory(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideApi2Retrofit(
            @IoScheduler ioScheduler: Scheduler,
            moshi: MoshiConverterFactory
    ): Retrofit {
        val clientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
            clientBuilder.addInterceptor(interceptor)
        }

        return Retrofit.Builder()
                .addConverterFactory(moshi)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(ioScheduler))
                .baseUrl("https://www.getpet.lt/")
                .client(clientBuilder.build())
                .build()

    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): PetApiService {
        return retrofit.create(PetApiService::class.java)
    }
}