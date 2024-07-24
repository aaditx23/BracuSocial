package com.aaditx23.bracusocial.backend.local.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


class Profile: RealmObject{
    @PrimaryKey var studentId: String = ""
    var password: String = ""
    var studentName: String = ""
    var enrolledCourses: String = ""
    var addedFriends: String = ""
    var friendRequests: String = ""
}
