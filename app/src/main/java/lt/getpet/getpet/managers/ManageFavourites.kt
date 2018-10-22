package lt.getpet.getpet.managers

import android.content.Context
import android.preference.PreferenceManager
import lt.getpet.getpet.data.Pet

class ManageFavourites(context: Context) {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)!!

    fun store(pet: Pet) {
        val petIds = sharedPreferences.getStringSet(FAVOURITE_PET_PREF_KEY, emptySet()).toMutableSet()
        petIds.add(pet.id.toString())

        val editor = sharedPreferences.edit()
        editor.putStringSet(FAVOURITE_PET_PREF_KEY, petIds)
        editor.apply()
    }

    fun getPetsFromPrefs(): List<Int> {
        return sharedPreferences.getStringSet(FAVOURITE_PET_PREF_KEY, emptySet()).asSequence().map { id -> id.toInt() }.toList()
    }

    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        const val FAVOURITE_PET_PREF_KEY = "favouritePetsSet"
    }
}