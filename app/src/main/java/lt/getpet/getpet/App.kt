package lt.getpet.getpet

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import lt.getpet.getpet.utils.RemoteCrashReportingTree
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Fabric.with(this, Crashlytics())
            Timber.plant(RemoteCrashReportingTree())
        }
    }

}