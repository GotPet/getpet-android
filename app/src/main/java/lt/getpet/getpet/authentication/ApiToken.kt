package lt.getpet.getpet.authentication

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiToken(val token: String)