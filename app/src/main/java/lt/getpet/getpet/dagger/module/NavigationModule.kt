package lt.getpet.getpet.dagger.module

import dagger.Module
import dagger.Provides
import lt.getpet.getpet.authentication.AuthenticationManager
import lt.getpet.getpet.navigation.NavigationManager

@Module
class NavigationModule {

    @Provides
    fun provideNavManager(authenticationManager: AuthenticationManager): NavigationManager {
        return NavigationManager(authenticationManager)
    }
}