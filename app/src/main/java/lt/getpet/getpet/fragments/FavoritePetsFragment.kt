package lt.getpet.getpet.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_pet_favorites.*
import kotlinx.android.synthetic.main.pet_favorite_cell.view.*
import lt.getpet.getpet.PetProfileActivity
import lt.getpet.getpet.R
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.persistence.PetsDatabase

/**
 * A placeholder fragment containing a simple view.
 */
class FavoritePetsFragment : androidx.fragment.app.Fragment() {

    private var subscription: Disposable? = null


    private lateinit var recyclerView: RecyclerView
    private lateinit var petsAdapter: PetsAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pet_favorites, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        petsAdapter = PetsAdapter(context!!, emptyList())


        recyclerView = pets_recycler_view.apply {
            setHasFixedSize(true)
            adapter = petsAdapter
        }

        subscription = PetsDatabase.getInstance(context!!).petsDao().getFavoritePets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    showPets(it)
                }, {
                    Toast.makeText(context, "Error loading pets", Toast.LENGTH_SHORT).show()
                })
    }

    override fun onDetach() {
        super.onDetach()
        subscription?.dispose()
    }

    private fun showPets(pets: List<Pet>) {
        petsAdapter.pets = pets
        petsAdapter.notifyDataSetChanged()
    }

    class PetsAdapter(val context: Context, var pets: List<Pet>) :
            RecyclerView.Adapter<PetsAdapter.MyViewHolder>() {

        override fun getItemId(position: Int): Long {
            return pets[position].id
        }

        class MyViewHolder(val context: Context, val view: View) : RecyclerView.ViewHolder(view) {
            fun bindPet(pet: Pet) {
                Glide.with(context).load(pet.photo)
                        .apply(RequestOptions.circleCropTransform())
                        .into(view.pet_photo)

                view.pet_name.text = pet.name
                view.pet_short_description.text = pet.short_description

                view.setOnClickListener {
                    val intent = PetProfileActivity.getStartActivityIntent(context, pet, false)
                    context.startActivity(intent)
                }
            }

        }


        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.pet_favorite_cell, parent, false)

            return MyViewHolder(context, view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val pet = pets[position]
            holder.bindPet(pet)
        }

        override fun getItemCount() = pets.size
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoritePetsFragment()
    }
}


