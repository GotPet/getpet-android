package lt.getpet.getpet

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.SwipeDirection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import lt.getpet.getpet.data.PetResponse
import lt.getpet.getpet.network.PetApiService
import java.util.*


class MainActivity : AppCompatActivity() {
    private var subscription: Disposable? = null
    private var adapter: PetAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadPets()

        setup()
        reload()
    }

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

    override fun onDestroy() {
        super.onDestroy()
        if (subscription != null) {
            subscription?.dispose()
        }
    }

    fun showPetResponse(petsList: List<PetResponse>) {
        val petMessage = "${petsList[0].id} ${petsList[0].shelter.email}"
    }

    fun showNoPets() {
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.menu_activity_main_reload -> reload()
            R.id.menu_activity_main_add_first -> addFirst()
            R.id.menu_activity_main_add_last -> addLast()
            R.id.menu_activity_main_remove_first -> removeFirst()
            R.id.menu_activity_main_remove_last -> removeLast()
            R.id.menu_activity_main_swipe_left -> swipeLeft()
            R.id.menu_activity_main_swipe_right -> swipeRight()
            R.id.menu_activity_main_reverse -> reverse()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createTouristSpot(): Pet {
        return Pet("Yasaka Shrine", "Kyoto", "https://source.unsplash.com/Xq1ntWruZQI/600x800")
    }

    private fun createTouristSpots(): List<Pet> {
        val spots = ArrayList<Pet>()
        spots.add(Pet("Yasaka Shrine", "Kyoto", "https://source.unsplash.com/Xq1ntWruZQI/600x800"))
        spots.add(Pet("Fushimi Inari Shrine", "Kyoto", "https://source.unsplash.com/NYyCqdBOKwc/600x800"))
        spots.add(Pet("Bamboo Forest", "Kyoto", "https://source.unsplash.com/buF62ewDLcQ/600x800"))
        spots.add(Pet("Brooklyn Bridge", "New York", "https://source.unsplash.com/THozNzxEP3g/600x800"))
        spots.add(Pet("Empire State Building", "New York", "https://source.unsplash.com/USrZRcRS2Lw/600x800"))
        spots.add(Pet("The statue of Liberty", "New York", "https://source.unsplash.com/PeFk7fzxTdk/600x800"))
        spots.add(Pet("Louvre Museum", "Paris", "https://source.unsplash.com/LrMWHKqilUw/600x800"))
        spots.add(Pet("Eiffel Tower", "Paris", "https://source.unsplash.com/HN-5Z6AmxrM/600x800"))
        spots.add(Pet("Big Ben", "London", "https://source.unsplash.com/CdVAUADdqEc/600x800"))
        spots.add(Pet("Great Wall of China", "China", "https://source.unsplash.com/AWh9C-QjhE4/600x800"))
        return spots
    }

    private fun createTouristSpotCardAdapter(): PetAdapter {
        val adapter = PetAdapter(applicationContext)
        adapter.addAll(createTouristSpots())
        return adapter
    }

    private fun setup() {
        activity_main_card_stack_view.setCardEventListener(object : CardStackView.CardEventListener {
            override fun onCardDragging(percentX: Float, percentY: Float) {
                Log.d("CardStackView", "onCardDragging")
            }

            override fun onCardSwiped(direction: SwipeDirection) {
                Log.d("CardStackView", "onCardSwiped: " + direction.toString())
                Log.d("CardStackView", "topIndex: " + activity_main_card_stack_view.topIndex)
                if (activity_main_card_stack_view.topIndex === adapter!!.getCount() - 5) {
                    Log.d("CardStackView", "Paginate: " + activity_main_card_stack_view.topIndex)
                    paginate()
                }
            }

            override fun onCardReversed() {
                Log.d("CardStackView", "onCardReversed")
            }

            override fun onCardMovedToOrigin() {
                Log.d("CardStackView", "onCardMovedToOrigin")
            }

            override fun onCardClicked(index: Int) {
                Log.d("CardStackView", "onCardClicked: $index")
            }
        })
    }

    private fun reload() {
        activity_main_card_stack_view.setVisibility(View.GONE)
        activity_main_progress_bar.setVisibility(View.VISIBLE)
        Handler().postDelayed(Runnable {
            adapter = createTouristSpotCardAdapter()
            activity_main_card_stack_view.setAdapter(adapter)
            activity_main_card_stack_view.setVisibility(View.VISIBLE)
            activity_main_progress_bar.setVisibility(View.GONE)
        }, 1000)
    }

    private fun extractRemainingTouristSpots(): LinkedList<Pet> {
        val spots = LinkedList<Pet>()
        for (i in activity_main_card_stack_view.getTopIndex() until adapter!!.count) {
            spots.add(adapter!!.getItem(i))
        }
        return spots
    }

    private fun addFirst() {
        val spots = extractRemainingTouristSpots()
        spots.addFirst(createTouristSpot())
        adapter!!.clear()
        adapter!!.addAll(spots)
        adapter!!.notifyDataSetChanged()
    }

    private fun addLast() {
        val spots = extractRemainingTouristSpots()
        spots.addLast(createTouristSpot())
        adapter!!.clear()
        adapter!!.addAll(spots)
        adapter!!.notifyDataSetChanged()
    }

    private fun removeFirst() {
        val spots = extractRemainingTouristSpots()
        if (spots.isEmpty()) {
            return
        }

        spots.removeFirst()
        adapter!!.clear()
        adapter!!.addAll(spots)
        adapter!!.notifyDataSetChanged()
    }

    private fun removeLast() {
        val spots = extractRemainingTouristSpots()
        if (spots.isEmpty()) {
            return
        }

        spots.removeLast()
        adapter!!.clear()
        adapter!!.addAll(spots)
        adapter!!.notifyDataSetChanged()
    }

    private fun paginate() {
        activity_main_card_stack_view.setPaginationReserved()
        adapter!!.addAll(createTouristSpots())
        adapter!!.notifyDataSetChanged()
    }

    fun swipeLeft() {
        val spots = extractRemainingTouristSpots()
        if (spots.isEmpty()) {
            return
        }

        val target = activity_main_card_stack_view.getTopView()
        val targetOverlay = activity_main_card_stack_view.getTopView().overlayContainer

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

        activity_main_card_stack_view.swipe(SwipeDirection.Left, cardAnimationSet, overlayAnimationSet)
    }

    fun swipeRight() {
        val spots = extractRemainingTouristSpots()
        if (spots.isEmpty()) {
            return
        }

        val target = activity_main_card_stack_view.getTopView()
        val targetOverlay = activity_main_card_stack_view.getTopView().overlayContainer

        val rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 10f))
        rotation.duration = 200
        val translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 2000f))
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

    private fun reverse() {
        activity_main_card_stack_view.reverse()
    }

}
