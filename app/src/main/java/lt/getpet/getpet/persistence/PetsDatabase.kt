package lt.getpet.getpet.persistence


import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import lt.getpet.getpet.data.Pet

@Database(entities = [Pet::class], version = 1)
abstract class PetsDatabase : RoomDatabase() {

    abstract fun petsDao(): PetDao

    companion object {

        @Volatile
        private var INSTANCE: PetsDatabase? = null

        fun getInstance(context: Context): PetsDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        PetsDatabase::class.java, "Pets.db")
                        .build()
    }
}