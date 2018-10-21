package lt.getpet.getpet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.SwipeDirection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_swipe.*
import lt.getpet.getpet.data.PetResponse
import lt.getpet.getpet.managers.ManageFavourites
import lt.getpet.getpet.network.PetApiService

class SwipeFragment : Fragment() {
    private var subscription: Disposable? = null
    lateinit var adapter: PetAdapter
    lateinit var favouritesManager: ManageFavourites

    fun loadPets() {
        subscription = PetApiService.create().getPetResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    showPetResponse(it)
                }, {
                    Log.e("Error", "Error loading pets", it)
                    showNoPets()
                })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        favouritesManager = ManageFavourites(context = activity!!.applicationContext)

        loadPets()

        setup()
    }

    override fun onDetach() {
        super.onDetach()
        subscription?.dispose()

    }


    fun showPetResponse(petsList: List<PetResponse>) {
        adapter = PetAdapter(activity!!.applicationContext)
        adapter.addAll(petsList)
        activity_main_card_stack_view.setAdapter(adapter)
        activity_main_card_stack_view.visibility = View.VISIBLE
        activity_main_progress_bar.visibility = View.GONE
    }

    fun showNoPets() {
    }


    private fun setup() {
        activity_main_card_stack_view.setCardEventListener(object : CardStackView.CardEventListener {
            override fun onCardDragging(percentX: Float, percentY: Float) {
                Log.d("CardStackView", "onCardDragging")
            }

            override fun onCardSwiped(direction: SwipeDirection) {
                Log.d("CardStackView", "onCardSwiped: " + direction.toString())
                val pos = activity_main_card_stack_view.topIndex - 1

                if (pos < 0 || pos >= adapter.count) {
                    return
                }

                val pet = adapter.getItem(pos)
                if (direction == SwipeDirection.Right) {
                    favouritesManager.store(pet)
                }
            }

            override fun onCardReversed() {
                Log.d("CardStackView", "onCardReversed")
            }

            override fun onCardMovedToOrigin() {
                Log.d("CardStackView", "onCardMovedToOrigin")
            }

            override fun onCardClicked(index: Int) {
                val pet = adapter.getItem(index)
                Log.d("CardStackView", "onCardClicked: $index")

                val intent = Intent(context, PetProfileActivity::class.java)
                intent.putExtra("pet", pet)
                startActivity(intent)

            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_swipe, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance() = SwipeFragment()
    }
}
