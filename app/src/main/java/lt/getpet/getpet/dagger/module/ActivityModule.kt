package lt.getpet.getpet.dagger.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import lt.getpet.getpet.*

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector()
    abstract fun getPetActivity(): GetPetActivity

    @ContributesAndroidInjector()
    abstract fun splashActivity(): SplashActivity

    @ContributesAndroidInjector()
    abstract fun onboardingActivity(): OnboardingActivity

    @ContributesAndroidInjector()
    abstract fun petProfileActivity(): PetProfileActivity

}