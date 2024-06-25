package com.aaditx23.bracusocial.ui

import android.annotation.SuppressLint

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.components.BottomNavigation
import com.aaditx23.bracusocial.components.NavDrawer
import com.aaditx23.bracusocial.components.TopActionBar


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Main(){
    var selectedIndexBotNav by rememberSaveable {
        mutableIntStateOf(0)
    }
    var selectedIndexDrawer by rememberSaveable {
        mutableIntStateOf(-1)
    }
    val navController = rememberNavController()
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var scope = rememberCoroutineScope()
    var scrollState = rememberScrollState()

    // server check



    //-------------------------------


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { ModalDrawerSheet {
            NavDrawer(
                scrollState = scrollState,
                selectedIndex = selectedIndexDrawer,
                onClick = {item ->
                    navController.navigate(item.title)
                    selectedIndexBotNav = -1
                    scope.launch {
                        drawerState.close()
                    }
                    selectedIndexDrawer = item.index!!

                },

                )
        }
        },
        gesturesEnabled = true
    ) {

        Scaffold(
            bottomBar = {
                BottomNavigation(selectedIndex = selectedIndexBotNav) { index ->
                    selectedIndexBotNav = index
                    selectedIndexDrawer = -1
                    println("$selectedIndexDrawer $selectedIndexBotNav")
                    when (index) {
                        0 -> navController.navigate("PrePreReg")
                        1 -> navController.navigate("Saved Routine")
                    }

                }
            },
            topBar = { TopActionBar(drawerState = drawerState, scope = scope ) }
        ){
            NavHost(navController = navController, startDestination = "PrePreReg" ){
                // Routes
                composable("PrePreReg"){

                }
                composable("Saved Routine"){

                }
                composable("About Us"){

                }
                composable("About BUCC"){

                }
            }
        }

    }
}