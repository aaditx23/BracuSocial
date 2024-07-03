package com.aaditx23.bracusocial.backend.local.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class Course: RealmObject{
    @PrimaryKey var _id: ObjectId = BsonObjectId()
    var courseName: String = ""
    var section: String = ""
    var classDay: String? = null
    var classTime: String? = null
    var classRoom: String? = null
    var labDay: String? = null
    var labTime: String? = null
    var labRoom: String? = null
}
