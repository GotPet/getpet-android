package lt.getpet.getpet.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.SwipeDirection
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_swipe.*
import lt.getpet.getpet.R
import lt.getpet.getpet.adapters.PetAdapter
import lt.getpet.getpet.constants.ActivityConstants.Companion.PET_FAVORITE
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.data.PetChoice
import lt.getpet.getpet.navigation.NavigationManager
import lt.getpet.getpet.persistence.PetDao
import timber.log.Timber
import javax.inject.Inject


class PetSwipeFragment : BaseFragment() {

    @Inject
    lateinit var petsDao: PetDao

    @Inject
    lateinit var navigationManager: NavigationManager

    private var subscriptions: CompositeDisposable = CompositeDisposable()
    lateinit var adapter: PetAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setup()

        val disposable = petsDao.getRemainingPets()
                .subscribeOn(dbScheduler)
                .observeOn(uiScheduler)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PET_FAVORITE && resultCode == RESULT_OK) {
            swipeRight()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
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

    private fun swipeRight() {
        val target = activity_main_card_stack_view.topView
        val targetOverlay = activity_main_card_stack_view.topView.overlayContainer

        val rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", -10f))
        rotation.duration = 200
        val translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, -2000f))
        val translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f))
        translateX.startDelay = 100
        translateY.startDelay = 100
        translateX.duration = 500
        translateY.duration = 500
        val cardAnimationSet = AnimatorSet()
        cardAnimationSet.playTogether(rotation, translateX, translateY)

        val overlayAnimator = ObjectAnimator.ofFloat(targetOverlay, "alpha", 0f, 1f)
        overlayAnimator.duration = 200
        val overlayAnimationSet = AnimatorSet()
        overlayAnimationSet.playTogether(overlayAnimator)

        activity_main_card_stack_view.swipe(SwipeDirection.Right, cardAnimationSet, overlayAnimationSet)
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
                .subscribeOn(dbScheduler)
                .observeOn(uiScheduler)
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

                if (pos >= adapter.count - 1) {
                    showNoPets()
                    return
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
                val pet = adapter.getItem(index)!!

                navigationManager.navigateToPetProfileActivity(activity!!, pet, true)
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
