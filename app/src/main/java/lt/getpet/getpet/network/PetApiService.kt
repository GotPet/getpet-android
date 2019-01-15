package lt.getpet.getpet.network

import io.reactivex.Single
import lt.getpet.getpet.data.*
import okhttp3.ResponseBody
import retrofit2.http.*

interface PetApiService {

    @POST("api/v1/authentication/firebase/connect/")
    fun authenticate(@Body authenticationRequest: AuthenticationRequest): Single<AuthenticationTokenResponse>


    @GET("api/v1/pets/")
    fun getPets(@Query("pet_ids") petIds: List<Long>, @Query("page") page: Int = 1): Single<PetResponse>

    @POST("api/v1/pets/generate/")
    fun generatePets(@Body generatePetsRequest: GeneratePetsRequest): Single<List<Pet>>


    @PUT("api/v1/pets/pet/choice/")
    fun savePetChoice(@Body petChoiceRequest: PetChoiceRequest): Single<ResponseBody>


    @POST("api/v1/pets/pet/shelter/")
    fun shelterPet(@Body shelterPetRequest: ShelterPetRequest): Single<ResponseBody>

}