package lt.getpet.getpet.fragments

import dagger.android.support.DaggerFragment
import io.reactivex.Scheduler
import lt.getpet.getpet.dagger.DbScheduler
import lt.getpet.getpet.dagger.IoScheduler
import lt.getpet.getpet.dagger.UiScheduler
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {
    @field:[Inject IoScheduler]
    lateinit var ioScheduler: Scheduler
    @field:[Inject UiScheduler]
    lateinit var uiScheduler: Scheduler
    @field:[Inject DbScheduler]
    lateinit var dbScheduler: Scheduler

}