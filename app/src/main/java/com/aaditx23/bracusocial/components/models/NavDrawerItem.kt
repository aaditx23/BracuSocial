package com.aaditx23.bracusocial.components.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Room
import androidx.compose.material.icons.outlined.SupervisedUserCircle
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavDrawerItem(
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null,
    val title: String,
    val isDivider: Boolean = false,
    val isHeader: Boolean = false,
    val index: Int? = null
){
    object FindRoom: NavDrawerItem(
        selectedIcon = Icons.Filled.Room,
        unselectedIcon = Icons.Outlined.Room,
        title = "Find Room",
        index = 0
    )
    object AboutClub: NavDrawerItem(
        selectedIcon = Icons.Filled.Apartment,
        unselectedIcon = Icons.Outlined.Apartment,
        title = "About the App",
        index = 1
    )

    object Divider: NavDrawerItem(
        title = "Divider",
        isDivider = true
    )

    companion object{
        val navDrawerItems = listOf(
            FindRoom,
            AboutClub
        )
    }
}