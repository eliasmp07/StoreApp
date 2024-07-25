package com.devdroid07.storeapp.store.di

import com.devdroid07.storeapp.core.domain.SessionStorage
import com.devdroid07.storeapp.store.data.remote.StoreApiService
import com.devdroid07.storeapp.store.data.repository.StoreRepositoryImpl
import com.devdroid07.storeapp.store.domain.repository.StoreRepository
import com.devdroid07.storeapp.store.domain.usecases.AddMyCartUseCase
import com.devdroid07.storeapp.store.domain.usecases.GetAllProducts
import com.devdroid07.storeapp.store.domain.usecases.GetMyCartUseCase
import com.devdroid07.storeapp.store.domain.usecases.GetSingleProduct
import com.devdroid07.storeapp.store.domain.usecases.RemoveProductMyCartUseCase
import com.devdroid07.storeapp.store.domain.usecases.StoreUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StoreModule {


    @Provides
    @Singleton
    fun provideStoreApiService(
        retrofit: Retrofit
    ): StoreApiService {
        return retrofit.create(StoreApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideStoreRepository(
        api: StoreApiService,
        sessionStorage: SessionStorage
    ): StoreRepository {
        return StoreRepositoryImpl(
            api = api,
            sessionStorage = sessionStorage
        )
    }

    @Provides
    @Singleton
    fun provideStoreUseCase(
        repository: StoreRepository
    ): StoreUseCases {
        return StoreUseCases(
            getAllProducts = GetAllProducts(repository),
            getSingleProduct = GetSingleProduct(repository),
            getMyCartUseCase = GetMyCartUseCase(repository),
            addMyCartUseCase = AddMyCartUseCase(repository),
            removeProductMyCartUseCase = RemoveProductMyCartUseCase(repository)
        )
    }

}