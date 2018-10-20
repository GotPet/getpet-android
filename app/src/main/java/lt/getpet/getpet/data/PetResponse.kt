package lt.getpet.getpet.data

data class PetResponse(
        val id: Int,
        val name: String,
        val photo: String,
        val shelter: Shelter,
        val short_description: String,
        val description: String
)

data class Shelter(
        val id: Int,
        val name: String,
        val email: String,
        val phone: String
)
