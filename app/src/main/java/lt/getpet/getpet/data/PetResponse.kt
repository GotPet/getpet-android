package lt.getpet.getpet.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PetResponse(
        val id: Long,
        val name: String,
        val photo: String,
        val shelter_id: Long,
        val short_description: String,
        val description: String
) : Parcelable

@Parcelize
data class ShelterResponse(
        val id: Long,
        val name: String,
        val email: String,
        val phone: String
) : Parcelable
