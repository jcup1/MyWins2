package com.theandroiddev.mywins.core.injection.module

import com.theandroiddev.mywins.data.repository.SuccessImagesLocalDataSource
import com.theandroiddev.mywins.data.source.SuccessImagesLocalDataSourceImpl
import com.theandroiddev.mywins.data.repository.SuccessesLocalDataSource
import com.theandroiddev.mywins.data.source.SuccessesLocalDataSourceImpl
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
