package lt.getpet.getpet.dagger.module

import android.os.Debug
import androidx.room.Room
import dagger.Module
import dagger.Provides
import lt.getpet.getpet.App
import lt.getpet.getpet.persistence.PetsDatabase
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providePetsDatabase(app: App): PetsDatabase {
        val builder = Room.databaseBuilder(
                app.applicationContext,
                PetsDatabase::class.java,
                "Pets.db"
        ).fallbackToDestructiveMigration()

        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun providePetsDao(db: PetsDatabase) = db.petsDao()
}