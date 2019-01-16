package lt.getpet.getpet.adapters

import android.content.Context
import lt.getpet.getpet.R
import lt.getpet.getpet.adapters.delegates.DelegatingAdapter
import lt.getpet.getpet.adapters.delegates.FavoritePetsDelegateAdapter
import lt.getpet.getpet.adapters.delegates.LabelDelegateAdapter
import lt.getpet.getpet.data.Pet

class FavoritePetsListAdapter(
        context: Context,
        listener: FavoritePetsDelegateAdapter.PetClickedListener
) : DelegatingAdapter(
        LabelDelegateAdapter(),
        FavoritePetsDelegateAdapter(context, listener)
) {
    fun bindData(pets: List<Pet>) {
        items = listOf()


        // items = items.plus(LabelDelegateAdapter.LabelItem(R.string.my_get_pet_requests))

        items = items.plus(LabelDelegateAdapter.LabelItem(R.string.my_favorite_pets))
                .plus(pets)
    }

}