package lt.getpet.getpet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.SwipeDirection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_swipe.*
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.managers.ManageFavourites
import lt.getpet.getpet.persistence.PetsDatabase

class SwipeFragment : Fragment() {
    private var subscription: Disposable? = null
    lateinit var adapter: PetAdapter
    lateinit var favouritesManager: ManageFavourites

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favouritesManager = ManageFavourites(context = activity!!.applicationContext)
        setup()
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


    private fun showPets(petsList: List<Pet>) {
        adapter = PetAdapter(context!!)
        adapter.addAll(petsList)
        activity_main_card_stack_view.setAdapter(adapter)

        if (petsList.isNotEmpty()) {
            changeState(State.CONTENT)
        } else {
            showNoPets()
        }
    }

    private fun changeState(state: State) {
        when (state) {
            SwipeFragment.State.LOADING -> {
                no_content.visibility = View.GONE
                activity_main_card_stack_view.visibility = View.GONE
                activity_main_progress_bar.visibility = View.VISIBLE
            }
            SwipeFragment.State.NO_CONTENT -> {
                no_content.visibility = View.VISIBLE
                activity_main_card_stack_view.visibility = View.GONE
                activity_main_progress_bar.visibility = View.GONE
            }
            SwipeFragment.State.CONTENT -> {
                no_content.visibility = View.GONE
                activity_main_card_stack_view.visibility = View.VISIBLE
                activity_main_progress_bar.visibility = View.GONE
            }
        }
    }

    fun showNoPets() {
        changeState(State.NO_CONTENT)
    }


    private fun setup() {
        changeState(State.LOADING)

        activity_main_card_stack_view.setCardEventListener(object : CardStackView.CardEventListener {
            override fun onCardDragging(percentX: Float, percentY: Float) {
                Log.d("CardStackView", "onCardDragging")
            }

            override fun onCardSwiped(direction: SwipeDirection) {
                Log.d("CardStackView", "onCardSwiped: " + direction.toString())
                val pos = activity_main_card_stack_view.topIndex - 1

                if (pos == adapter.count - 1) {
                    showNoPets()
                }

                val pet = adapter.getItem(pos)
                if (direction == SwipeDirection.Right && pet != null) {
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


    private enum class State {
        LOADING, NO_CONTENT, CONTENT
    }

    companion object {
        @JvmStatic
        fun newInstance() = SwipeFragment()
    }
}
