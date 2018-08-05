package com.theandroiddev.mywins.injection

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

/**
 * Created by jakub on 07.11.17.
 */

@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context

}
