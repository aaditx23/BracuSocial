package com.aaditx23.bracusocial.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.viewmodels.CourseVM
import com.aaditx23.bracusocial.backend.viewmodels.FriendsVM
import com.aaditx23.bracusocial.components.BottomNavigation
import com.aaditx23.bracusocial.components.models.BottomNavItem
import com.aaditx23.bracusocial.ui.screens.Friends.FindFriends
import com.aaditx23.bracusocial.ui.screens.Friends.FriendRequests
import com.aaditx23.bracusocial.ui.screens.Friends.MyFriends
import com.aaditx23.bracusocial.ui.screens.PrePreReg.MakeRoutine
import com.aaditx23.bracusocial.ui.screens.PrePreReg.SavedRoutine

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Friends(){
    val friendvm: FriendsVM = hiltViewModel()

    var selectedIndexBotNav by rememberSaveable {
        mutableIntStateOf(0)
    }
    val bottomNavList by rememberSaveable {
        mutableStateOf(BottomNavItem.friendItemList)
    }
    val navController = rememberNavController()
    var scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomNavigation(
                items = bottomNavList,
                selectedIndex = selectedIndexBotNav
            ) { index ->
                println("Selected index $index size ${bottomNavList.size}")
                selectedIndexBotNav = index
                navController.navigate(bottomNavList[index].title)

            }
        }
    ) {
        NavHost(navController = navController, startDestination = "My Friends" ){
            composable("My Friends"){
                MyFriends(friendvm = friendvm)
            }
            composable("Friend Requests"){
                FriendRequests(friendvm = friendvm)
            }
            composable("Find Friends"){
                FindFriends(friendsvm = friendvm)
            }
        }

    }

}