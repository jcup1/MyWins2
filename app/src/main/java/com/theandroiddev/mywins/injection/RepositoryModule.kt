package com.theandroiddev.mywins.injection

import android.content.Context
import com.theandroiddev.mywins.data.repositories.DatabaseSuccessesRepository
import com.theandroiddev.mywins.data.repositories.SuccessesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(context: Context): SuccessesRepository = DatabaseSuccessesRepository(context)

}

