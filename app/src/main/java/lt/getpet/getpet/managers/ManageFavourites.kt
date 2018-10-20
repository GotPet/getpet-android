package lt.getpet.getpet.managers

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import lt.getpet.getpet.data.PetResponse

class ManageFavourites(context: Context) {

    private val gson: Gson = Gson()

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)!!;

    fun store(pet: PetResponse) {

        val oldPetsList = getPetsFromPrefs()
        val recentPetsList = generateRecentPetsList(pet, oldPetsList)

        if (recentPetsList.isNotEmpty()) {
            val favouritePetsJson: String = gson.toJson(recentPetsList)
            sharedPreferences.edit().putString(FAVOURITE_PET_PREF_KEY, favouritePetsJson).apply()
        }
    }

    fun getPetsFromPrefs(): List<Int> {

        val recentPetsPrefs = if (!sharedPreferences.contains(FAVOURITE_PET_PREF_KEY)) {
            sharedPreferences.edit().putString(FAVOURITE_PET_PREF_KEY, FAVOURITE_PET_PREF_EMPTY).apply()
            FAVOURITE_PET_PREF_EMPTY
        } else {
            sharedPreferences.getString(FAVOURITE_PET_PREF_KEY, FAVOURITE_PET_PREF_EMPTY)
        }

        if (recentPetsPrefs.isEmpty()) return emptyList()
        return gson.fromJson<List<Int>>(recentPetsPrefs, object : TypeToken<List<Long>>() {}.type)
    }

    fun clear() {
        if (sharedPreferences.contains(FAVOURITE_PET_PREF_KEY)) {
            sharedPreferences.edit().putString(FAVOURITE_PET_PREF_KEY, FAVOURITE_PET_PREF_EMPTY).apply()
        }
    }

    private fun generateRecentPetsList(
            pet: PetResponse,
            oldPetsList: List<Int>
    ): List<Int> {

        return if (oldPetsList.contains(pet.id)) {
            oldPetsList
        } else {
            val recentPetsList = mutableListOf<Int>()
            recentPetsList.addAll(oldPetsList)
            recentPetsList.add(pet.id)
            recentPetsList
        }
    }

    companion object {
        const val FAVOURITE_PET_PREF_KEY = "favouritePets"
        const val FAVOURITE_PET_PREF_EMPTY = ""
    }
}