package lt.getpet.getpet.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PetChoiceRequest(
        @Json(name = "pet") val petId: Long,
        @Json(name = "is_favorite") val favorite: Boolean
)