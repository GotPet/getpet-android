package lt.getpet.getpet.utils

import android.util.Log

import com.crashlytics.android.Crashlytics

import timber.log.Timber

class RemoteCrashReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        val messageWithTag = "$tag: $message"

        Crashlytics.log(messageWithTag)

        if (t != null) {
            Crashlytics.logException(t)
        } else if (priority > Log.WARN) {
            val throwable = Throwable(messageWithTag)

            Crashlytics.logException(throwable)
        }
    }
}