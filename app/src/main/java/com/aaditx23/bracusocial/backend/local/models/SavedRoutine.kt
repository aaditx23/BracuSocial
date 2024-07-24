package com.aaditx23.bracusocial.backend.local.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class SavedRoutine: RealmObject{
    @PrimaryKey var _id: ObjectId = BsonObjectId()
    var courseKeyList: String = ""
}
