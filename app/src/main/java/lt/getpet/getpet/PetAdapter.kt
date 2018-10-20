package lt.getpet.getpet

import android.content.Context
import android.widget.TextView
import com.bumptech.glide.Glide
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import lt.getpet.getpet.data.PetResponse


class PetAdapter(context: Context) : ArrayAdapter<PetResponse>(context, 0) {

    override fun getView(position: Int, contentView: View?, parent: ViewGroup): View {
        var contentView = contentView
        val holder: ViewHolder

        if (contentView == null) {
            val inflater = LayoutInflater.from(context)
            contentView = inflater.inflate(R.layout.pet_card, parent, false)
            holder = ViewHolder(contentView!!)
            contentView!!.tag = holder
        } else {
            holder = contentView!!.tag as ViewHolder
        }

        val spot = getItem(position)

        holder.name.setText(spot!!.name)
        holder.short_description.setText(spot!!.short_description)
        Glide.with(context).load(spot!!.photo).into(holder.image)

        return contentView
    }

    private class ViewHolder(view: View) {
        var name: TextView
        var short_description: TextView
        var image: ImageView

        init {
            this.name = view.findViewById(R.id.pet_name)
            this.short_description = view.findViewById(R.id.pet_short_description)
            this.image = view.findViewById(R.id.pet_image) as ImageView
        }
    }

}
