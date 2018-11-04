package lt.getpet.getpet.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


sealed class User

@Parcelize
data class GuestUser(
        val userId: String
) : User(), Parcelable


enum class Provider {
    GOOGLE,
    FACEBOOK
}

@Parcelize
data class RegularUser(
        val name: String,
        val email: String,
        val photo_url: String?,
        val provider: Provider
) : User(), Parcelable

