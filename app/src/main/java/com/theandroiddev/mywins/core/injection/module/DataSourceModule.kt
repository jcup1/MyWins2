package com.theandroiddev.mywins.core.injection.module

import com.theandroiddev.mywins.data.successes.repository.SuccessImagesLocalDataSource
import com.theandroiddev.mywins.data.successes.source.SuccessImagesLocalDataSourceImpl
import com.theandroiddev.mywins.data.successes.repository.SuccessesLocalDataSource
import com.theandroiddev.mywins.data.successes.source.SuccessesLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideSuccessesDataSource(successesDataSourceImpl: SuccessesLocalDataSourceImpl):
            SuccessesLocalDataSource = successesDataSourceImpl

    @Provides
    @Singleton
    fun provideSuccessImagesDataSource(successImagesDataSourceImpl: SuccessImagesLocalDataSourceImpl):
            SuccessImagesLocalDataSource = successImagesDataSourceImpl

}
