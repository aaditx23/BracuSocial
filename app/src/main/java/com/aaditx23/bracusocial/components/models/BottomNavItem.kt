package com.aaditx23.bracusocial.components.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.AutoAwesomeMosaic
import androidx.compose.material.icons.filled.BackupTable
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AppRegistration
import androidx.compose.material.icons.outlined.AutoAwesomeMosaic
import androidx.compose.material.icons.outlined.BackupTable
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    badge: Boolean,
    badgeCount: Int = 0,
    val idx: Int
) {
    var badge = mutableStateOf(badge)
    val badgeCount = mutableIntStateOf(badgeCount)

    object AllCourses: BottomNavItem(
        title = "All Courses",
        selectedIcon = Icons.Filled.AutoAwesomeMosaic,
        unselectedIcon = Icons.Outlined.AutoAwesomeMosaic,
        badge = false,
        idx = 0
    )
    object PrePreReg: BottomNavItem(
        title = "PrePreReg",
        selectedIcon = Icons.Filled.AppRegistration,
        unselectedIcon = Icons.Outlined.AppRegistration,
        badge = false,
        idx = 1
    )
    object SavedRoutine: BottomNavItem(
        title = "Saved Routine",
        selectedIcon = Icons.Filled.BackupTable,
        unselectedIcon = Icons.Outlined.BackupTable,
        badge = false,
        idx = 2
    )
    object CourseHandler: BottomNavItem(
        title = "Course Handler",
        selectedIcon = Icons.Filled.Code,
        unselectedIcon = Icons.Outlined.Code,
        badge = false,
        idx = 3
    )
    companion object{
        val bottomNavItemList = listOf(
            AllCourses,
            PrePreReg,
            SavedRoutine,
            CourseHandler
        )
    }



}

