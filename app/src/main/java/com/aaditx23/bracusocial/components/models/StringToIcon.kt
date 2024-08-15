package com.aaditx23.bracusocial.components.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.AutoAwesomeMosaic
import androidx.compose.material.icons.filled.BackupTable
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.AppRegistration
import androidx.compose.material.icons.outlined.AutoAwesomeMosaic
import androidx.compose.material.icons.outlined.BackupTable
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAddAlt1
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material.icons.outlined.Room
import androidx.compose.material.icons.outlined.StackedBarChart
import androidx.compose.material.icons.outlined.TableChart
import androidx.compose.ui.graphics.vector.ImageVector

val map = mutableMapOf(
    "filled_app_registration" to Icons.Filled.AppRegistration,
    "filled_backup_table" to Icons.Filled.BackupTable,
    "filled_groups" to Icons.Filled.Groups,
    "filled_person_add_alt_1" to Icons.Filled.PersonAddAlt1,
    "filled_person_search" to Icons.Filled.PersonSearch,
    "filled_person" to Icons.Filled.Person,
    "filled_group" to Icons.Filled.Group,
    "filled_auto_awesome_mosaic" to Icons.Filled.AutoAwesomeMosaic,
    "filled_room" to Icons.Filled.Room,
    "filled_apartment" to Icons.Filled.Apartment,
    "auto_mirrored_filled_login" to Icons.AutoMirrored.Filled.Login,
    "auto_mirrored_filled_logout" to Icons.AutoMirrored.Filled.Logout,
    "filled_routine" to Icons.Filled.CalendarMonth,
    "filled_my_routine" to Icons.Filled.TableChart,
    "filled_friend_routine" to Icons.Filled.StackedBarChart,

    "outlined_app_registration" to Icons.Outlined.AppRegistration,
    "outlined_backup_table" to Icons.Outlined.BackupTable,
    "outlined_groups" to Icons.Outlined.Groups,
    "outlined_person_add_alt_1" to Icons.Outlined.PersonAddAlt1,
    "outlined_person_search" to Icons.Outlined.PersonSearch,
    "outlined_person" to Icons.Outlined.Person,
    "outlined_group" to Icons.Outlined.Group,
    "outlined_auto_awesome_mosaic" to Icons.Outlined.AutoAwesomeMosaic,
    "outlined_room" to Icons.Outlined.Room,
    "outlined_apartment" to Icons.Outlined.Apartment,
    "auto_mirrored_outlined_login" to Icons.AutoMirrored.Outlined.Login,
    "auto_mirrored_outlined_logout" to Icons.AutoMirrored.Outlined.Logout,
    "outlined_routine" to Icons.Outlined.CalendarMonth,
    "outlined_my_routine" to Icons.Outlined.TableChart,
    "outlined_friends_routine" to Icons.Outlined.StackedBarChart
)

fun IconMap(icon: String): ImageVector{
    val value = map[icon]
    if (value!= null){
        return value
    }
    else{
        return Icons.Outlined.Error
    }
}
