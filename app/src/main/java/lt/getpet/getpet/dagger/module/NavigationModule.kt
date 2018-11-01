package lt.getpet.getpet.dagger.module

import dagger.Module
import dagger.Provides
import lt.getpet.getpet.navigation.NavigationManager

@Module
class NavigationModule {

    @Provides
    fun provideNavManager(): NavigationManager {
        return NavigationManager()
    }
}