package com.aaditx23.bracusocial.application

import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.models.FriendProfile
import com.aaditx23.bracusocial.backend.local.models.Profile
import com.aaditx23.bracusocial.backend.local.models.SavedRoutine
import com.aaditx23.bracusocial.backend.local.models.Session
import com.aaditx23.bracusocial.backend.remote.ProfileProxy
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object RealmServer {
    lateinit var realm: Realm
        private set


    fun initRealm(){
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