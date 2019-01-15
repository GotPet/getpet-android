package lt.getpet.getpet.data

data class PetIdsRequest(val petIds: List<Long>) {
    override fun toString(): String {
        return petIds.joinToString(separator = ",")
    }
}