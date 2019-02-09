package com.theandroiddev.mywins.core.injection

import android.app.Application
import com.theandroiddev.mywins.core.injection.module.ActivityBuildersModule
import com.theandroiddev.mywins.core.injection.module.AppModule
import com.theandroiddev.mywins.core.injection.module.CommonModule
import com.theandroiddev.mywins.core.injection.module.DataSourceModule
import com.theandroiddev.mywins.core.injection.module.DatabaseModule
import com.theandroiddev.mywins.core.injection.module.FragmentBuildersModule
import com.theandroiddev.mywins.core.injection.module.ServicesModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton

/**
 * Created by jakub on 07.11.17.
 */

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
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

