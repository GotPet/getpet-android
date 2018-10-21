package lt.getpet.getpet

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_pet_favorites.*
import kotlinx.android.synthetic.main.pet_favorite_cell.view.*
import lt.getpet.getpet.data.PetResponse
import lt.getpet.getpet.managers.ManageFavourites
import lt.getpet.getpet.network.PetApiService

/**
 * A placeholder fragment containing a simple view.
 */
class PetFavoritesActivityFragment : Fragment() {

    private var subscription: Disposable? = null


    private lateinit var recyclerView: RecyclerView
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

        subscription = PetApiService.create().getPetResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    showPetResponse(it)
                }, {
                    Log.e("Error", "Error loading pets", it)
                })

    }

    override fun onDestroy() {
        super.onDestroy()

        subscription?.dispose()
    }

    private fun showPetResponse(pets: List<PetResponse>) {
        val petIds = favouritesManager.getPetsFromPrefs()

        val filteredPets = pets.filter { pet -> petIds.any { id -> id == pet.id } }

        Log.d("petIds", petIds.toString())
        Log.d("FilteredPets", filteredPets.toString())

        recyclerView.adapter = PetsAdapter(filteredPets)
        recyclerView.adapter.notifyDataSetChanged()
    }

    class PetsAdapter(private val pets: List<PetResponse>) :
            RecyclerView.Adapter<PetsAdapter.MyViewHolder>() {

        class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            fun bindPet(pet: PetResponse) {
                Glide.with(view.context).load(pet.photo).into(view.pet_photo)

                view.pet_name.text = pet.name
                view.pet_short_description.text = pet.short_description

                view.setOnClickListener {
                    Log.d("Pet clicked", pet.toString())
                }
            }

        }


        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): PetsAdapter.MyViewHolder {
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


