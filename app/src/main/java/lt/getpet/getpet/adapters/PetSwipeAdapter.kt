package lt.getpet.getpet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.pet_card.view.*
import lt.getpet.getpet.R
import lt.getpet.getpet.data.Pet


class PetSwipeAdapter(private val context: Context,
                      private val onPetClickedListener: OnPetClickedListener,
                      private val pets: List<Pet>
) : RecyclerView.Adapter<PetSwipeAdapter.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.pet_card, parent, false))
    }

    override fun getItemCount(): Int = pets.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getPet(position), onPetClickedListener)
    }

    fun getPet(position: Int): Pet {
        return pets[position]
    }

    override fun getItemId(position: Int): Long {
        return getPet(position).id
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pet: Pet, onPetClickedListener: OnPetClickedListener) = with(itemView) {
            pet_name.text = pet.name
            pet_short_description.text = pet.shortDescription
            Glide.with(context).load(pet.photo).into(pet_image)
            itemView.setOnClickListener {
                onPetClickedListener.onPetClicked(pet)
            }
        }
    }

    interface OnPetClickedListener {
        fun onPetClicked(pet: Pet)
    }

}
