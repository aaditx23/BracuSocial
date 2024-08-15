package com.aaditx23.bracusocial.components.models

import android.os.Parcelable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.AutoAwesomeMosaic
import androidx.compose.material.icons.filled.BackupTable
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.outlined.AppRegistration
import androidx.compose.material.icons.outlined.AutoAwesomeMosaic
import androidx.compose.material.icons.outlined.BackupTable
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAddAlt1
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
sealed class BottomNavItem(
    val title: String,
    val selectedIcon: String,
    val unselectedIcon: String,
): Parcelable {

    @Parcelize
    object PrePreReg: BottomNavItem(
        title = "PrePreReg",
        selectedIcon = "filled_app_registration",
        unselectedIcon = "outlined_app_registration",

    )
    @Parcelize
    object SavedRoutine: BottomNavItem(
        title = "Saved Routine",
        selectedIcon = "filled_backup_table",
        unselectedIcon = "outlined_backup_table",
    )
    @Parcelize
    object MyFriends: BottomNavItem(
        title = "My Friends",
        selectedIcon = "filled_groups",
        unselectedIcon = "outlined_groups",
    )
    @Parcelize
    object FriendRequests: BottomNavItem(
        title = "Friend Requests",
        selectedIcon = "filled_person_add_alt_1",
        unselectedIcon = "outlined_person_add_alt_1",
    )
    @Parcelize
    object FindFriend: BottomNavItem(
        title = "Find Friends",
        selectedIcon = "filled_person_search",
        unselectedIcon = "outlined_person_search",
    )
    @Parcelize
    object MyRoutine: BottomNavItem(
        title = "My Routine",
        selectedIcon = "filled_my_routine",
        unselectedIcon =  "outlined_my_routine",
    )
    @Parcelize
    object FriendsRoutine: BottomNavItem(
        title = "Friends' Routine",
        selectedIcon = "filled_friend_routine",
        unselectedIcon = "outlined_friends_routine",
    )

    companion object{
        val PrePreRegItemList = listOf(
            PrePreReg,
            SavedRoutine,
        )
        val friendItemList = listOf(
            MyFriends,
            FriendRequests,
            FindFriend
        )
        val routineItemList = listOf(
            MyRoutine,
            FriendsRoutine
        )
    }



}

