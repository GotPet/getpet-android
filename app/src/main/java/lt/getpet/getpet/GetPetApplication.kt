package lt.getpet.getpet

import android.app.Application
import timber.log.Timber

class GetPetApplication : Application() {
    override fun onCreate() {
        super.onCreate()


        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
        }
    }

}