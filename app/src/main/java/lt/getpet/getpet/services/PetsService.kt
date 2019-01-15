package lt.getpet.getpet.services

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import lt.getpet.getpet.authentication.AuthenticationManager
import lt.getpet.getpet.data.*
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

    fun generatePetsToSwipe(): Single<List<Long>> {
        return Single.zip(petsDao.getLikedPetIds(), petsDao.getDislikedPetIds(),
                BiFunction { likedPetIds: List<Long>, dislikedPetIds: List<Long> ->
                    GeneratePetsRequest(likedPets = likedPetIds, dislikedPets = dislikedPetIds)
                })
                .flatMap {
                    petApiService.generatePets(it)
                }.map { pets ->
                    petsDao.deletePets(pets)
                    petsDao.insertPets(pets)
                }
    }

    // TODO follow pagination for getPets
    fun updatePetsData(): Single<Int> {
        return petsDao.getLikedPetIds()
                .flatMap { petIds ->
                    if (petIds.isNotEmpty()) {
                        petApiService.getPets(PetIdsRequest(petIds))
                    } else {
                        Single.just(PetResponse(results = emptyList()))
                    }
                }.map { petsResponse ->
                    val pets = petsResponse.results
                    if (pets.isNotEmpty()) {
                        petsDao.updatePets(petsResponse.results)
                    } else {
                        0
                    }
                }
    }

}