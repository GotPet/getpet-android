package lt.getpet.getpet.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PetResponse(
        val id: Int,
        val name: String,
        val photo: String,
        val shelter: Shelter,
        val short_description: String,
        val description: String
): Parcelable

@Parcelize
data class Shelter(
        val id: Int,
        val name: String,
        val email: String,
        val phone: String
):Parcelable
