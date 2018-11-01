package lt.getpet.getpet.persistence


import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.data.PetChoice

@Database(entities = [Pet::class, PetChoice::class], version = 1)
abstract class PetsDatabase : RoomDatabase() {

    abstract fun petsDao(): PetDao
}