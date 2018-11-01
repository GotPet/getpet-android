package lt.getpet.getpet.network

import io.reactivex.Single
import lt.getpet.getpet.BuildConfig
import lt.getpet.getpet.data.Pet
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface PetApiService {

    @GET("api/v1/pets")
    fun getPets(): Single<List<Pet>>
}