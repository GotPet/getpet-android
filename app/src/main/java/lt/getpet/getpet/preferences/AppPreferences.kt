package lt.getpet.getpet.preferences

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.vinted.preferx.PreferxSerializer
import com.vinted.preferx.booleanPreference
import com.vinted.preferx.objectPreference
import lt.getpet.getpet.authentication.ApiToken
import java.lang.reflect.Type

class AppPreferences(private val preferences: SharedPreferences, private val moshi: Moshi) {
    class MoshiPreferencesSerializer<T : Any>(private val moshi: Moshi) : PreferxSerializer<T> {
        override fun fromString(string: String, type: Type): T {
            return moshi.adapter<T>(type).fromJson(string)!!
        }

        override fun toString(value: T, type: Type): String {
            return moshi.adapter(Any::class.java).toJson(value)
        }
    }

    val apiToken by lazy {
        preferences.objectPreference(
                PREFERENCE_API_TOKEN,
                ApiToken(token = ""),
                MoshiPreferencesSerializer(moshi),
                ApiToken::class.java
        )
    }
    val onboardingShown by lazy {
        preferences.booleanPreference(
                PREFERENCE_ONBOARDING_SHOWN,
                false
        )
    }

    companion object {
        private const val PREFERENCE_API_TOKEN = "API_TOKEN"
        private const val PREFERENCE_ONBOARDING_SHOWN = "ONBOARDING_SHOWN"
    }


}