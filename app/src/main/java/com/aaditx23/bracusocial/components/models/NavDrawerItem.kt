package com.aaditx23.bracusocial.components.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.AutoAwesomeMosaic
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.AppRegistration
import androidx.compose.material.icons.outlined.AutoAwesomeMosaic
import androidx.compose.material.icons.outlined.CalendarViewWeek
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Room
import androidx.compose.material.icons.outlined.SupervisedUserCircle
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavDrawerItem(
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null,
    val title: String,
    val isDivider: Boolean = false,
    val isHeader: Boolean = false,
){
    object Profile: NavDrawerItem(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        title = "Profile"
    )
    object Friends: NavDrawerItem(
        selectedIcon = Icons.Filled.Group,
        unselectedIcon = Icons.Outlined.Group,
        title = "Friends"
    )

    object AllCourses: NavDrawerItem(
        selectedIcon = Icons.Filled.AutoAwesomeMosaic,
        unselectedIcon = Icons.Outlined.AutoAwesomeMosaic,
        title = "All Courses"
    )
    object PrePreReg: NavDrawerItem(
        selectedIcon = Icons.Filled.AppRegistration,
        unselectedIcon = Icons.Outlined.AppRegistration,
        title = "PrePreReg"
    )
    object FindRoom: NavDrawerItem(
        selectedIcon = Icons.Filled.Room,
        unselectedIcon = Icons.Outlined.Room,
        title = "Find Room"
    )

    object Login: NavDrawerItem(
        selectedIcon = Icons.AutoMirrored.Filled.Login,
        unselectedIcon = Icons.AutoMirrored.Outlined.Login,
        title = "Signup/Login"
    )
    object Logout: NavDrawerItem(
        selectedIcon = Icons.AutoMirrored.Filled.Logout,
        unselectedIcon = Icons.AutoMirrored.Outlined.Logout,
        title = "Logout"
    )
    object AboutApp: NavDrawerItem(
        selectedIcon = Icons.Filled.Apartment,
        unselectedIcon = Icons.Outlined.Apartment,
        title = "About App"
    )

    object Divider: NavDrawerItem(
        title = "Divider",
        isDivider = true
    )





    companion object{

        val navDrawerItems = listOf(
            Profile,
            Friends,
            AllCourses,
            PrePreReg,
            FindRoom,
            AboutApp,
            Divider,
            Login,
            Logout
        )

    }
}