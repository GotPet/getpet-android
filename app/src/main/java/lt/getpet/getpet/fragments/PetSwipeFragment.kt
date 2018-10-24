package lt.getpet.getpet.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.SwipeDirection
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_swipe.*
import lt.getpet.getpet.PetProfileActivity
import lt.getpet.getpet.R
import lt.getpet.getpet.adapters.PetAdapter
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.data.PetChoice
import lt.getpet.getpet.persistence.PetDao
import lt.getpet.getpet.persistence.PetsDatabase
import timber.log.Timber

class PetSwipeFragment : Fragment() {
    private var subscriptions: CompositeDisposable = CompositeDisposable()
    lateinit var adapter: PetAdapter
    private lateinit var petsDao: PetDao

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        petsDao = PetsDatabase.getInstance(context!!).petsDao()

        setup()

        val disposable = petsDao.getRemainingPets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    showPets(it)
                }, {
                    Toast.makeText(context, "Error loading pets", Toast.LENGTH_SHORT).show()
                })
        subscriptions.add(disposable)
    }

    override fun onDetach() {
        super.onDetach()
        subscriptions.clear()
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
            State.LOADING -> {
                no_content.visibility = View.GONE
                activity_main_card_stack_view.visibility = View.GONE
                activity_main_progress_bar.visibility = View.VISIBLE
            }
            State.NO_CONTENT -> {
                no_content.visibility = View.VISIBLE
                activity_main_card_stack_view.visibility = View.GONE
                activity_main_progress_bar.visibility = View.GONE
            }
            State.CONTENT -> {
                no_content.visibility = View.GONE
                activity_main_card_stack_view.visibility = View.VISIBLE
                activity_main_progress_bar.visibility = View.GONE
            }
        }
    }

    fun showNoPets() {
        changeState(State.NO_CONTENT)
    }

    fun savePetChoice(pet: Pet, isFavorite: Boolean) {
        val petChoice = PetChoice(petId = pet.id, isFavorite = isFavorite)

        val disposable = Single.fromCallable {
            petsDao.insertPetChoice(petChoice)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Saved pet choice: $petChoice")
                }, {
                    Timber.w(it)
                    Toast.makeText(context, "Error saving pet", Toast.LENGTH_SHORT).show()
                })
        subscriptions.add(disposable)
    }


    private fun setup() {
        changeState(State.LOADING)

        activity_main_card_stack_view.setCardEventListener(object : CardStackView.CardEventListener {
            override fun onCardDragging(percentX: Float, percentY: Float) {
            }

            override fun onCardSwiped(direction: SwipeDirection) {
                val pos = activity_main_card_stack_view.topIndex - 1

                if (pos == adapter.count - 1) {
                    showNoPets()
                }

                val pet = adapter.getItem(pos)
                if (direction == SwipeDirection.Right && pet != null) {
                    savePetChoice(pet, true)
                } else if (direction == SwipeDirection.Left && pet != null) {
                    savePetChoice(pet, false)
                }
            }

            override fun onCardReversed() {
            }

            override fun onCardMovedToOrigin() {
            }

            override fun onCardClicked(index: Int) {
                val pet = adapter.getItem(index)
                val intent = Intent(context, PetProfileActivity::class.java)
                intent.putExtra("pet", pet)
                startActivity(intent)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_swipe, container, false)
    }


    private enum class State {
        LOADING, NO_CONTENT, CONTENT
    }

    companion object {
        @JvmStatic
        fun newInstance() = PetSwipeFragment()
    }
}