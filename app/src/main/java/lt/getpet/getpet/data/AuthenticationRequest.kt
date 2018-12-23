package lt.getpet.getpet.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthenticationRequest(@Json(name = "id_token") val idToken: String)