package lt.getpet.getpet.authentication

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FirebaseAPIToken(val token: String)