package lt.getpet.getpet.preferences

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.vinted.preferx.PreferxSerializer
import com.vinted.preferx.objectPreference
import lt.getpet.getpet.authentication.FirebaseAPIToken
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

    val firebaseApiToken by lazy {
        preferences.objectPreference(
                PREFERENCE_FIREBASE_API_TOKEN,
                FirebaseAPIToken(token = "123"),
                MoshiPreferencesSerializer(moshi),
                FirebaseAPIToken::class.java
        )
    }

    companion object {
        private const val PREFERENCE_FIREBASE_API_TOKEN = "FIREBASE_API_TOKEN"
    }


}