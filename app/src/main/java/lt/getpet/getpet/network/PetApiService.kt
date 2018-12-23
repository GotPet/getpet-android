package lt.getpet.getpet.network

import io.reactivex.Single
import lt.getpet.getpet.data.AuthenticationRequest
import lt.getpet.getpet.data.AuthenticationTokenResponse
import lt.getpet.getpet.data.GeneratePetsRequest
import lt.getpet.getpet.data.Pet
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PetApiService {

    @POST("api/v1/authentication/firebase/connect/")
    fun authenticate(@Body authenticationRequest: AuthenticationRequest): Single<AuthenticationTokenResponse>


    @GET("api/v1/pets/")
    fun getPets(): Single<List<Pet>>

    @POST("api/v1/pets/generate/")
    fun generatePets(@Body generatePetsRequest: GeneratePetsRequest): Single<List<Pet>>

}