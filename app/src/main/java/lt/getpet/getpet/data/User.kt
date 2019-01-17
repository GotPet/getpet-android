package lt.getpet.getpet.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

enum class Provider {
    GOOGLE,
    FACEBOOK
}

@Parcelize
data class User(
        val name: String,
        val email: String?,
        val photo_url: String?,
        val provider: Provider
) : Parcelable

