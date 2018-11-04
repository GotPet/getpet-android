package lt.getpet.getpet.dagger.component

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import lt.getpet.getpet.App
import lt.getpet.getpet.dagger.module.*
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            ActivityModule::class,
            AuthenticationModule::class,
            FragmentModule::class,
            NavigationModule::class,
            DatabaseModule::class,
            ApiModule::class
        ]
)
interface ApplicationComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}
