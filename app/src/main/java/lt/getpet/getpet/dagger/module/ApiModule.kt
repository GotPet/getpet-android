package lt.getpet.getpet.dagger.module

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import lt.getpet.getpet.BuildConfig
import lt.getpet.getpet.dagger.IoScheduler
import lt.getpet.getpet.network.ApiHeadersInterceptor
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
    fun provideOkHttpClient(
            apiHeadersInterceptor: ApiHeadersInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(apiHeadersInterceptor)
                .apply {
                    if (BuildConfig.DEBUG) {
                        addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    }
                }
                .build()
    }


    @Provides
    fun providesRetrofit(
            @IoScheduler ioScheduler: Scheduler,
            okHttpClient: OkHttpClient,
            moshi: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(moshi)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(ioScheduler))
                .baseUrl("https://www.getpet.lt/")
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun providePetApiService(
            retrofit: Retrofit
    ): PetApiService {
        return retrofit.create(PetApiService::class.java)
    }
}