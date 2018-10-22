package lt.getpet.getpet.network

import io.reactivex.Single
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.data.Shelter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface PetApiService {

    @GET("api/v1/pets")
    fun getPets(): Single<List<Pet>>

    @GET("api/v1/shelters")
    fun getShelters(): Single<List<Shelter>>


    companion object {
        fun create(): PetApiService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("https://www.getpet.lt/")
                    .client(client)
                    .build()

            return retrofit.create(PetApiService::class.java)
        }
    }
}