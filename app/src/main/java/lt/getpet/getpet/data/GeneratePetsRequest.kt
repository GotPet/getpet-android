package lt.getpet.getpet.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeneratePetsRequest(
        @Json(name = "liked_pets") val likedPets: List<Long>,
        @Json(name = "disliked_pets") val dislikedPets: List<Long>
)