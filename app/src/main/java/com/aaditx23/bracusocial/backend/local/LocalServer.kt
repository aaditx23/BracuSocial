package com.aaditx23.bracusocial.backend.local

import android.app.Application
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.models.FriendProfile
import com.aaditx23.bracusocial.backend.local.models.Profile
import com.aaditx23.bracusocial.backend.local.models.SavedRoutine
import com.aaditx23.bracusocial.backend.local.models.Session
import com.aaditx23.bracusocial.backend.remote.ProfileProxy
import dagger.hilt.android.HiltAndroidApp
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

@HiltAndroidApp
class LocalServer: Application() {
    // register models and store it in a variable for accessing all over the application
    companion object{
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()

        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(     // register models here
                    Session::class,
                    Course::class,
                    Profile::class,
                    SavedRoutine::class,
                    ProfileProxy::class,
                    FriendProfile::class
                )
            )
        )
    }

}