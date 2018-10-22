package lt.getpet.getpet.fragments

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_pet_favorites.*
import kotlinx.android.synthetic.main.pet_favorite_cell.view.*
import lt.getpet.getpet.PetProfileActivity
import lt.getpet.getpet.R
import lt.getpet.getpet.managers.ManageFavourites
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.persistence.PetsDatabase

/**
 * A placeholder fragment containing a simple view.
 */
class PetFavoritesActivityFragment : androidx.fragment.app.Fragment() {

    private var subscription: Disposable? = null


    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var favouritesManager: ManageFavourites


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pet_favorites, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favouritesManager = ManageFavourites(context = activity!!.applicationContext)


        recyclerView = pets_recycler_view.apply {
            setHasFixedSize(true)
        }

        subscription = PetsDatabase.getInstance(context!!).petsDao().getRemainingPets()
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
        val petIds = favouritesManager.getPetsFromPrefs()

        val filteredPets = pets.filter { pet -> petIds.any { id -> id.toLong() == pet.id } }

        val adapter = PetsAdapter(filteredPets)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    class PetsAdapter(private val pets: List<Pet>) :
            RecyclerView.Adapter<PetsAdapter.MyViewHolder>() {

        class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            fun bindPet(pet: Pet) {
                Glide.with(view.context).load(pet.photo).into(view.pet_photo)

                view.pet_name.text = pet.name
                view.pet_short_description.text = pet.short_description

                view.setOnClickListener {
                    val intent = Intent(view.context, PetProfileActivity::class.java)
                    intent.putExtra("pet", pet)
                    view.context.startActivity(intent)
                }
            }

        }


        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.pet_favorite_cell, parent, false)

            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val pet = pets[position]
            holder.bindPet(pet)
        }

        override fun getItemCount() = pets.size
    }

    companion object {
        @JvmStatic
        fun newInstance() = PetFavoritesActivityFragment()
    }
}


