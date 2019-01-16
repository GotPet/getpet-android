package lt.getpet.getpet.dagger.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import lt.getpet.getpet.fragments.FavoritePetsListFragment
import lt.getpet.getpet.fragments.OnBoardingFragment
import lt.getpet.getpet.fragments.PetSwipeFragment
import lt.getpet.getpet.fragments.UserProfileFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun favoritePetsFragment(): FavoritePetsListFragment

    @ContributesAndroidInjector
    abstract fun petSwipeFragment(): PetSwipeFragment

    @ContributesAndroidInjector
    abstract fun userProfileFragment(): UserProfileFragment

    @ContributesAndroidInjector
    abstract fun onboardingFragment(): OnBoardingFragment
}
