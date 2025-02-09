package com.aaditx23.bracusocial.application

import android.app.Application
import com.aaditx23.bracusocial.application.Retrofit.RetrofitServer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BracuSocialApp: Application() {

    override fun onCreate() {
        super.onCreate()
        RealmServer.initRealm()
        RetrofitServer.initRetrofit()
    }

}