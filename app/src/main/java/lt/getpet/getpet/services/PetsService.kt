package lt.getpet.getpet.services

import io.reactivex.Single
import lt.getpet.getpet.authentication.AuthenticationManager
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.data.PetChoice
import lt.getpet.getpet.data.PetChoiceRequest
import lt.getpet.getpet.network.PetApiService
import lt.getpet.getpet.persistence.PetDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetsService @Inject constructor(
        private val petApiService: PetApiService,
        private val petsDao: PetDao,
        private val authenticationManager: AuthenticationManager
) {

    fun savePetChoice(pet: Pet, isFavorite: Boolean): Single<Boolean> {
        val petChoice = PetChoice(petId = pet.id, isFavorite = isFavorite)
        val petChoiceRequest = PetChoiceRequest(petId = pet.id, favorite = isFavorite)

        return Single.fromCallable {
            petsDao.insertPetChoice(petChoice)
        }.flatMap {
            if (authenticationManager.isUserLoggedIn()) {
                petApiService.savePetChoice(petChoiceRequest).map { true }
            } else {
                Single.fromCallable { true }
            }
        }
    }

}