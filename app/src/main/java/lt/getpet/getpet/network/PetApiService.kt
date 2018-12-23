package lt.getpet.getpet.network

import io.reactivex.Observable
import io.reactivex.Single
import lt.getpet.getpet.data.*
import okhttp3.ResponseBody
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


    @POST("api/v1/pets/choice/")
    fun savePetChoice(@Body petChoiceRequest: PetChoiceRequest): Observable<ResponseBody>


}