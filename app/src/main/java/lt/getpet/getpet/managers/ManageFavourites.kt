package lt.getpet.getpet.managers

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import lt.getpet.getpet.data.PetResponse

class ManageFavourites(context: Context) {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)!!

    fun store(pet: PetResponse) {
        val petIds = sharedPreferences.getStringSet(FAVOURITE_PET_PREF_KEY, emptySet()).toMutableSet()
        petIds.add(pet.id.toString())

        val editor = sharedPreferences.edit()
        editor.putStringSet(FAVOURITE_PET_PREF_KEY, petIds)
        editor.apply()
    }

    fun getPetsFromPrefs(): List<Int> {
        return sharedPreferences.getStringSet(FAVOURITE_PET_PREF_KEY, emptySet()).asSequence().map { id -> id.toInt() }.toList()
    }

    companion object {
        const val FAVOURITE_PET_PREF_KEY = "favouritePetsSet"
    }
}