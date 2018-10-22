package lt.getpet.getpet.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import lt.getpet.getpet.data.Pet

@Dao
interface PetDao {

    @Query("SELECT * FROM Pets")
    fun getRemainingPets(): Single<List<Pet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPets(pets: List<Pet>): List<Long>

}