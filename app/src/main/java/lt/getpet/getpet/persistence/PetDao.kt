package lt.getpet.getpet.persistence

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.data.PetChoice

@Dao
interface PetDao {

    @Query("SELECT Pets.* FROM Pets LEFT JOIN PetChoices ON Pets.id = PetChoices.pet_id " +
            "WHERE Pets.is_available = 1 AND (PetChoices.status IS NULL OR PetChoices.status = ${PetChoice.STATUS_PET_DISLIKED}) " +
            "ORDER BY PetChoices.status IS NOT NULL, RANDOM()")
    fun getPetsToSwipe(): Single<List<Pet>>

    @Query("SELECT Pets.* FROM Pets INNER JOIN PetChoices ON Pets.id = PetChoices.pet_id " +
            "AND PetChoices.status IN (${PetChoice.STATUS_PET_FAVORITE}, ${PetChoice.STATUS_PET_WITH_GETPET_REQUEST}) " +
            "WHERE Pets.is_available = 1 " +
            "ORDER BY PetChoices.created_at DESC")
    fun getFavoritePets(): Flowable<List<Pet>>

    @Query("SELECT Pets.* FROM Pets INNER JOIN PetChoices ON Pets.id = PetChoices.pet_id " +
            "AND PetChoices.status = ${PetChoice.STATUS_PET_WITH_GETPET_REQUEST} " +
            "WHERE Pets.is_available = 1 " +
            "ORDER BY PetChoices.created_at DESC")
    fun getPetsWithGetPetRequests(): Flowable<List<Pet>>

    @Query("SELECT Pets.id FROM Pets INNER JOIN PetChoices ON Pets.id = PetChoices.pet_id " +
            "AND PetChoices.status IN (${PetChoice.STATUS_PET_FAVORITE}, ${PetChoice.STATUS_PET_WITH_GETPET_REQUEST})")
    fun getLikedPetIds(): Single<List<Long>>

    @Query("SELECT Pets.id FROM Pets INNER JOIN PetChoices ON Pets.id = PetChoices.pet_id " +
            "AND PetChoices.status = ${PetChoice.STATUS_PET_DISLIKED}")
    fun getDislikedPetIds(): Single<List<Long>>

    @Query("SELECT Pets.id FROM Pets INNER JOIN PetChoices ON Pets.id = PetChoices.pet_id")
    fun getChosenPetIds(): Single<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPetChoice(petChoice: PetChoice): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPets(pets: List<Pet>): List<Long>

    @Update
    fun updatePets(pets: List<Pet>): Int

    @Delete
    fun deletePets(pets: List<Pet>): Int


}