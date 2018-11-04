package lt.getpet.getpet

import android.content.Intent
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import lt.getpet.getpet.authentication.AuthenticationManager
import lt.getpet.getpet.constants.ActivityConstants.Companion.RC_SIGN_IN
import lt.getpet.getpet.dagger.IoScheduler
import lt.getpet.getpet.dagger.UiScheduler
import timber.log.Timber
import javax.inject.Inject


abstract class BaseActivity : DaggerAppCompatActivity() {


    @field:[Inject IoScheduler]
    lateinit var ioScheduler: Scheduler
    @field:[Inject UiScheduler]
    lateinit var uiScheduler: Scheduler

    @Inject
    lateinit var authenticationManager: AuthenticationManager

    protected var subscriptions: CompositeDisposable = CompositeDisposable()


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_SIGN_IN -> {
                subscriptions.add(
                        authenticationManager.onActivityResult(resultCode, data)
                                .subscribeOn(uiScheduler)
                                .observeOn(uiScheduler)
                                .subscribe({
                                    Timber.i("Authentication activity result $it}")
                                }, {
                                    Timber.w(it, "Authentication activity result error")
                                })
                )
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun onStop() {
        super.onStop()

        subscriptions.clear()
    }

}