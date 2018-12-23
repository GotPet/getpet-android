package lt.getpet.getpet.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PetResponse(val results: List<Pet>)