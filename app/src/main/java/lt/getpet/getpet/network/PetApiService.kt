package lt.getpet.getpet.network

import io.reactivex.Observable
import io.reactivex.Single
import lt.getpet.getpet.data.*
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PetApiService {

    @POST("api/v1/authentication/firebase/connect/")
    fun authenticate(@Body authenticationRequest: AuthenticationRequest): Single<AuthenticationTokenResponse>


    @GET("api/v1/pets/")
    fun getPets(@Query("pet_ids") petIds: List<Long>, page: Int = 1): Single<PetResponse>

    @POST("api/v1/pets/generate/")
    fun generatePets(@Body generatePetsRequest: GeneratePetsRequest): Single<List<Pet>>


    @POST("api/v1/pets/pet/choice/")
    fun savePetChoice(@Body petChoiceRequest: PetChoiceRequest): Observable<ResponseBody>


    @POST("api/v1/pets/pet/shelter/")
    fun shelterPet(@Body shelterPetRequest: ShelterPetRequest): Observable<ResponseBody>

}