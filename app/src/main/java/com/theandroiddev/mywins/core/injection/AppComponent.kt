package com.theandroiddev.mywins.core.injection

import android.app.Application
import com.theandroiddev.mywins.core.injection.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by jakub on 07.11.17.
 */

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityBuildersModule::class,
    AppModule::class,
    DataSourceModule::class,
    DatabaseModule::class,
    ServicesModule::class,
    CommonModule::class,
    FragmentBuildersModule::class])

interface AppComponent : AndroidInjector<DaggerApplication> {

    override fun inject(application: DaggerApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent

    }

}

