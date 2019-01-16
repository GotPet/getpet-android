package lt.getpet.getpet.adapters.delegates

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.pet_favorite_cell.view.*
import lt.getpet.getpet.R
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.extensions.inflate

class FavoritePetsDelegateAdapter(val context: Context, private val listener: PetClickedListener) :
        DelegateAdapter<Pet, FavoritePetsDelegateAdapter.FavoritePetViewHolder>(Pet::class.java) {

    override fun areItemsTheSame(oldItem: Pet, newItem: Pet): Boolean {
        return oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup): FavoritePetViewHolder {
        return FavoritePetViewHolder(parent, context, listener)
    }

    override fun onBindViewHolder(holder: FavoritePetViewHolder, item: Pet) {
        holder.bind(item)
    }

    class FavoritePetViewHolder(
            parent: ViewGroup,
            val context: Context,
            private val listener: PetClickedListener
    ) : RecyclerView.ViewHolder(parent.inflate(R.layout.pet_favorite_cell)) {
        fun bind(pet: Pet) {
            itemView.run {
                Glide.with(context).load(pet.photo)
                        .apply(RequestOptions.circleCropTransform())
                        .into(pet_photo)

                pet_name.text = pet.name
                pet_short_description.text = pet.shortDescription

                setOnClickListener {
                    listener.onPetClicked(pet)
                }
            }

        }

    }

    interface PetClickedListener {
        fun onPetClicked(pet: Pet)
    }
}
