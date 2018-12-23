package lt.getpet.getpet.dagger.module

import dagger.Module
import dagger.Provides
import lt.getpet.getpet.authentication.AuthenticationManager
import lt.getpet.getpet.network.PetApiService
import lt.getpet.getpet.preferences.AppPreferences
import javax.inject.Singleton

@Module
class AuthenticationModule {

    @Provides
    @Singleton
    fun provideAuthenticationManager(
            petApiService: PetApiService,
            appPreferences: AppPreferences
    ): AuthenticationManager {
        return AuthenticationManager(petApiService, appPreferences)
    }
}