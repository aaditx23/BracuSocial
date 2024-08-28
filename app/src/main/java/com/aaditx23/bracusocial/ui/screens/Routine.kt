package com.aaditx23.bracusocial.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aaditx23.bracusocial.backend.viewmodels.RoutineVM
import com.aaditx23.bracusocial.components.BottomNavigation
import com.aaditx23.bracusocial.components.models.BottomNavItem
import com.aaditx23.bracusocial.ui.screens.Routine.FriendRoutine
import com.aaditx23.bracusocial.ui.screens.Routine.MyRoutine

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Routine(navControllerOld: NavHostController) {
    val routinevm: RoutineVM = hiltViewModel()


    var selectedIndexBotNav by rememberSaveable {
        mutableIntStateOf(0)
    }
    val bottomNavList by rememberSaveable {
        mutableStateOf(BottomNavItem.routineItemList)
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
        NavHost(navController = navController, startDestination = "My Routine" ){
            composable("My Routine"){
                MyRoutine(routinevm, navControllerOld)
            }
            composable("Friends' Routine"){
                FriendRoutine(routinevm = routinevm)
            }
        }
    }
}