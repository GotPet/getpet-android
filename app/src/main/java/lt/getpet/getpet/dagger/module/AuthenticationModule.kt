package lt.getpet.getpet.dagger.module

import dagger.Module
import dagger.Provides
import lt.getpet.getpet.authentication.AuthenticationManager
import javax.inject.Singleton

@Module
class AuthenticationModule {

    @Provides
    @Singleton
    fun provideAuthenticationManager(): AuthenticationManager {
        return AuthenticationManager()
    }
}