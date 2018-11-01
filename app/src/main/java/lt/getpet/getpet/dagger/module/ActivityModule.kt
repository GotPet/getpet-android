package lt.getpet.getpet.dagger.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import lt.getpet.getpet.GetPetActivity
import lt.getpet.getpet.MainActivity
import lt.getpet.getpet.PetProfileActivity
import lt.getpet.getpet.SplashActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector()
    abstract fun getPetActivity(): GetPetActivity

    @ContributesAndroidInjector()
    abstract fun splashActivity(): SplashActivity

    @ContributesAndroidInjector()
    abstract fun petProfileActivity(): PetProfileActivity

}