package lt.getpet.getpet.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_pet_swipe.*
import lt.getpet.getpet.R
import lt.getpet.getpet.adapters.PetSwipeAdapter
import lt.getpet.getpet.constants.ActivityConstants.Companion.PET_FAVORITE
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.navigation.NavigationManager
import lt.getpet.getpet.services.PetsService
import timber.log.Timber
import javax.inject.Inject


class PetSwipeFragment : BaseFragment(), CardStackListener, PetSwipeAdapter.OnPetClickedListener {

    @Inject
    lateinit var navigationManager: NavigationManager

    @Inject
    lateinit var petsService: PetsService

    private var subscriptions: CompositeDisposable = CompositeDisposable()

    private lateinit var swipeAdapter: PetSwipeAdapter

    private val cardStackLayoutManager: CardStackLayoutManager by lazy {
        CardStackLayoutManager(context, this)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setup()

        val disposable = petsService.getPetsToSwipe()
                .subscribeOn(dbScheduler)
                .observeOn(uiScheduler)
                .subscribe({ it ->
                    showPets(it)
                }, {
                    Toast.makeText(context, "Error loading pets", Toast.LENGTH_SHORT).show()
                    Timber.w(it)
                })
        subscriptions.add(disposable)
    }

    override fun onDetach() {
        super.onDetach()
        subscriptions.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PET_FAVORITE && resultCode == RESULT_OK) {
            swipeAndLikePet()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showPets(petsList: List<Pet>) {
        swipeAdapter = PetSwipeAdapter(context!!, this, petsList)
        card_stack_view.adapter = swipeAdapter

        if (petsList.isNotEmpty()) {
            changeState(State.CONTENT)
        } else {
            changeState(State.NO_CONTENT)
        }
    }

    private fun swipeCard(direction: Direction) {
        val setting = SwipeAnimationSetting.Builder()
                .setDirection(direction)
                .setDuration(200)
                .setInterpolator(AccelerateInterpolator())
                .build()
        cardStackLayoutManager.setSwipeAnimationSetting(setting)
        card_stack_view.swipe()
    }

    private fun swipeAndLikePet() {
        swipeCard(Direction.Right)
    }

    private fun swipeAndDislikePet() {
        swipeCard(Direction.Left)
    }

    private fun changeState(state: State) {
        when (state) {
            State.LOADING -> {
                no_content.visibility = View.GONE
                card_stack_layout.visibility = View.GONE
                activity_main_progress_bar.visibility = View.VISIBLE
            }
            State.NO_CONTENT -> {
                no_content.visibility = View.VISIBLE
                card_stack_layout.visibility = View.GONE
                activity_main_progress_bar.visibility = View.GONE
            }
            State.CONTENT -> {
                no_content.visibility = View.GONE
                card_stack_layout.visibility = View.VISIBLE
                activity_main_progress_bar.visibility = View.GONE
            }
        }
    }

    private fun savePetChoice(pet: Pet, isFavorite: Boolean) {
        val disposable = petsService.savePetChoice(pet, isFavorite)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe({
                    Timber.d("Saved pet ${pet.id} choice")
                }, {
                    Timber.w(it)
                })
        subscriptions.add(disposable)
    }


    private fun setup() {
        changeState(State.LOADING)
        card_stack_view.layoutManager = cardStackLayoutManager

        pet_dislike_button.setOnClickListener {
            swipeAndDislikePet()
        }

        pet_like_button.setOnClickListener {
            swipeAndLikePet()
        }

        pet_rewind_button.setOnClickListener {
            card_stack_view.rewind()
        }
    }

    override fun onPetClicked(pet: Pet) {
        navigationManager.navigateToPetProfileActivity(this, pet, false)
    }

    override fun onCardSwiped(direction: Direction?) {
        val position = cardStackLayoutManager.topPosition - 1

        val pet = swipeAdapter.getPet(position)
        when (direction) {
            Direction.Right -> savePetChoice(pet, true)
            Direction.Left -> savePetChoice(pet, false)
            else -> {
                Timber.d("Swiped to another direction $direction")
            }
        }

        if (position + 1 == swipeAdapter.itemCount) {
            changeState(State.NO_CONTENT)
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) {}

    override fun onCardDragging(direction: Direction?, ratio: Float) {}

    override fun onCardCanceled() {}

    override fun onCardAppeared(view: View?, position: Int) {
        pet_rewind_button.isEnabled = cardStackLayoutManager.topPosition != 0
    }

    override fun onCardRewound() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pet_swipe, container, false)
    }


    private enum class State {
        LOADING, NO_CONTENT, CONTENT
    }

    companion object {
        @JvmStatic
        fun newInstance() = PetSwipeFragment()
    }
}
