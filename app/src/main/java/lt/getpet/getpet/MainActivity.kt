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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadPets()

        setup()
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
        val adapter = PetAdapter(applicationContext)
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
                Log.d("CardStackView", "topIndex: " + activity_main_card_stack_view.topIndex)
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

}
