package lt.getpet.getpet.network

import io.reactivex.Single
import lt.getpet.getpet.data.PetResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface PetApiService {

    @GET("api/v1/pets")
    fun getPetResponse(): Single<List<PetResponse>>


    companion object {
        fun create(): PetApiService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("https://getpet.vycius.lt/")
                    .client(client)
                    .build()

            return retrofit.create(PetApiService::class.java)
        }
    }
}