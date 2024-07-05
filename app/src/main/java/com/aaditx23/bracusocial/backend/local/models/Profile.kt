package com.aaditx23.bracusocial.backend.local.models

import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class Profile: RealmObject{
    @PrimaryKey var studentId: String = ""
    var studentName: String = ""
    var enrolledCourses = mutableListOf<Course>().toRealmList()
}
