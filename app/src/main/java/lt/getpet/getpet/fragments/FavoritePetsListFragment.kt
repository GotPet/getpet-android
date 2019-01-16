package lt.getpet.getpet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_pet_favorites.*
import lt.getpet.getpet.R
import lt.getpet.getpet.adapters.FavoritePetsListAdapter
import lt.getpet.getpet.adapters.delegates.FavoritePetsDelegateAdapter
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.navigation.NavigationManager
import lt.getpet.getpet.persistence.PetDao
import timber.log.Timber
import javax.inject.Inject

class FavoritePetsListFragment : BaseFragment(), FavoritePetsDelegateAdapter.PetClickedListener {

    @Inject
    lateinit var petsDao: PetDao

    @Inject
    lateinit var navigationManager: NavigationManager

    private var subscription: Disposable? = null


    private lateinit var recyclerView: RecyclerView
    private lateinit var favoritePetsListAdapter: FavoritePetsListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pet_favorites, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favoritePetsListAdapter = FavoritePetsListAdapter(context!!, this)


        recyclerView = pets_recycler_view.apply {
            adapter = favoritePetsListAdapter
        }

        subscription = Flowable.zip(petsDao.getFavoritePets(), petsDao.getPetsWithGetPetRequests(),
                BiFunction { favoritePets: List<Pet>, petsWithGetPetRequest: List<Pet> ->
                    Pair(favoritePets, petsWithGetPetRequest)
                })
                .subscribeOn(dbScheduler)
                .observeOn(uiScheduler)
                .subscribe({ it ->
                    showPets(it.first, it.second)
                }, {
                    Timber.w(it)
                    Toast.makeText(context, "Error loading pets", Toast.LENGTH_SHORT).show()
                })
    }

    override fun onPetClicked(pet: Pet) {
        navigationManager.navigateToPetProfileActivity(this, pet, true)
    }

    override fun onDetach() {
        super.onDetach()
        subscription?.dispose()
    }

    private fun showPets(pets: List<Pet>, petsWithGetPetRequest: List<Pet>) {
        favoritePetsListAdapter.bindData(pets, petsWithGetPetRequest)
        if (pets.isEmpty()) {
            no_content.visibility = VISIBLE
            pets_recycler_view.visibility = GONE
        } else {
            no_content.visibility = GONE
            pets_recycler_view.visibility = VISIBLE
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = FavoritePetsListFragment()
    }


}


