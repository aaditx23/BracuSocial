package com.aaditx23.bracusocial.backend.local.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


class Session: RealmObject {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var dbStatus: Boolean = false
    var loginStatus: Boolean = false

}