package com.devdroid07.storeapp.auth.di

import com.devdroid07.storeapp.auth.data.network.api.AuthApiService
import com.devdroid07.storeapp.auth.data.repository.AuthRepositoryImpl
import com.devdroid07.storeapp.auth.domain.repository.AuthRepository
import com.devdroid07.storeapp.core.di.CoreRetrofit
import com.devdroid07.storeapp.core.domain.SessionStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApiService,
        sessionStorage: SessionStorage,
    ): AuthRepository {
        return AuthRepositoryImpl(
            api = api,
            sessionStorage = sessionStorage
        )
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        @CoreRetrofit retrofit: Retrofit,
    ): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}