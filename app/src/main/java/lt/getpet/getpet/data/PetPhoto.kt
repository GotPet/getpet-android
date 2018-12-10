package lt.getpet.getpet.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PetPhoto(
        val photo: String
) : Parcelable