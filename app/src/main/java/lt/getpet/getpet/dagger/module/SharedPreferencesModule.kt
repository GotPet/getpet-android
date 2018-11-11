package lt.getpet.getpet.dagger.module

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import lt.getpet.getpet.App
import lt.getpet.getpet.preferences.AppPreferences
import javax.inject.Singleton

@Module
class SharedPreferencesModule {

    @Provides
    @Singleton
    fun providesAppPreferences(application: App): SharedPreferences {
        return application.getSharedPreferences(COMMON_PREFERENCES, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppPreferences(
            preference: SharedPreferences,
            moshi: Moshi
            ): AppPreferences {
        return AppPreferences(preference, moshi)
    }

    companion object {
        private const val COMMON_PREFERENCES = "GETPET_COMMON_PREFERENCES"
    }
}