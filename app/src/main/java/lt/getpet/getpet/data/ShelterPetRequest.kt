package lt.getpet.getpet.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShelterPetRequest(
        @Json(name = "pet") val petId: Long
)