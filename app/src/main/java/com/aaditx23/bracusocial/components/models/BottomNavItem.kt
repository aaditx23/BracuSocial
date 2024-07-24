package com.aaditx23.bracusocial.components.models

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

sealed class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    badge: Boolean,
    badgeCount: Int = 0,
) {
    var badge = mutableStateOf(badge)
    val badgeCount = mutableIntStateOf(badgeCount)


    object PrePreReg: BottomNavItem(
        title = "PrePreReg",
        selectedIcon = Icons.Filled.AppRegistration,
        unselectedIcon = Icons.Outlined.AppRegistration,
        badge = false
    )
    object SavedRoutine: BottomNavItem(
        title = "Saved Routine",
        selectedIcon = Icons.Filled.BackupTable,
        unselectedIcon = Icons.Outlined.BackupTable,
        badge = false
    )
    object MyFriends: BottomNavItem(
        title = "My Friends",
        selectedIcon = Icons.Filled.Groups,
        unselectedIcon = Icons.Outlined.Groups,
        badge = false
    )
    object FriendRequests: BottomNavItem(
        title = "Friend Requests",
        selectedIcon = Icons.Filled.PersonAddAlt1,
        unselectedIcon = Icons.Outlined.PersonAddAlt1,
        badge = false
    )
    object FindFriend: BottomNavItem(
        title = "Find Friends",
        selectedIcon = Icons.Filled.PersonSearch,
        unselectedIcon = Icons.Outlined.PersonSearch,
        badge = false
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
    }



}

