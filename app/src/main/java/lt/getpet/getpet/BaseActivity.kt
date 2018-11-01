package lt.getpet.getpet

import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Scheduler
import lt.getpet.getpet.dagger.DbScheduler
import lt.getpet.getpet.dagger.IoScheduler
import lt.getpet.getpet.dagger.UiScheduler
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {
    @field:[Inject IoScheduler]
    lateinit var ioScheduler: Scheduler
    @field:[Inject UiScheduler]
    lateinit var uiScheduler: Scheduler
}