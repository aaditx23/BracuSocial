package com.aaditx23.bracusocial.backend.local

import com.aaditx23.bracusocial.backend.local.LocalServer
import com.aaditx23.bracusocial.backend.remote.USISClient
import com.aaditx23.bracusocial.backend.remote.UsisCrawler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {

    @Provides
    @Singleton
    fun provideRealm(): Realm {
        return LocalServer.realm
    }

    @Provides
    @Singleton
    fun provideUsis(): UsisCrawler{
        return UsisCrawler()
    }
    @Provides
    @Singleton
    fun provideUsisLogin(): USISClient{
        return USISClient()
    }
}