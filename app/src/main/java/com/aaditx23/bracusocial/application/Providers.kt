package com.aaditx23.bracusocial.application

import com.aaditx23.bracusocial.application.Retrofit.RetrofitServer
import com.aaditx23.bracusocial.backend.remote.USISClient
import com.aaditx23.bracusocial.backend.remote.UsisCrawler
import com.aaditx23.bracusocial.backend.remote.api.ConnectService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Providers {
    @Provides
    @Singleton
    fun provideRealm(): Realm {
        return RealmServer.realm
    }

    @Provides
    @Singleton
    fun provideUsis(): UsisCrawler {
        return UsisCrawler()
    }
    @Provides
    @Singleton
    fun provideUsisLogin(): USISClient {
        return USISClient()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit{
        return RetrofitServer.retrofit
    }

    @Provides
    @Singleton
    fun provideRetrofitConnect(): ConnectService{
        return RetrofitServer.Connect
    }
}