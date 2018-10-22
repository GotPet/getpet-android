package lt.getpet.getpet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.pet_card.view.*
import lt.getpet.getpet.R
import lt.getpet.getpet.data.Pet


class PetAdapter(context: Context) : ArrayAdapter<Pet>(context, 0) {

    override fun getView(position: Int, contentView: View?, parent: ViewGroup): View {
        val view = contentView
                ?: LayoutInflater.from(context).inflate(R.layout.pet_card, parent, false)

        val pet = getItem(position)!!
        view.pet_name.text = pet.name
        view.pet_short_description.text = pet.short_description
        Glide.with(context).load(pet.photo).into(view.pet_image)

        return view
    }

}
