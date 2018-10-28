package lt.getpet.getpet

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import lt.getpet.getpet.utils.RemoteCrashReportingTree
import timber.log.Timber
import com.crashlytics.android.core.CrashlyticsCore


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val crashlyticsKit = Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()

        Fabric.with(this, crashlyticsKit)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(RemoteCrashReportingTree())
        }
    }

}