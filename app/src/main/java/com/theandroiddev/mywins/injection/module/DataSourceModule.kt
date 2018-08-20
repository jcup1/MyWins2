package com.theandroiddev.mywins.injection.module

import com.theandroiddev.mywins.data.data_source.SuccessesDataSource
import com.theandroiddev.mywins.data.data_source.SuccessesDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideSuccessesDataSource(successesDataSourceImpl: SuccessesDataSourceImpl):
            SuccessesDataSource = successesDataSourceImpl

}
