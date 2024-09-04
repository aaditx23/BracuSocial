package com.aaditx23.bracusocial.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState

import com.aaditx23.bracusocial.backend.local.models.demoProfiles
import com.aaditx23.bracusocial.backend.remote.AccountProxyVM
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import com.aaditx23.bracusocial.backend.viewmodels.CourseVM
import com.aaditx23.bracusocial.backend.viewmodels.SessionVM
import com.aaditx23.bracusocial.components.CircularLoadingBasic
import com.aaditx23.bracusocial.components.NavDrawer
import com.aaditx23.bracusocial.components.TopActionBar
import com.aaditx23.bracusocial.components.models.BottomNavItem
import com.aaditx23.bracusocial.components.models.NavDrawerItem
import com.aaditx23.bracusocial.ui.screens.Account.Logout
import com.aaditx23.bracusocial.ui.screens.CourseScreen
import com.aaditx23.bracusocial.ui.screens.FindRoom
import com.aaditx23.bracusocial.ui.screens.PrePreReg
import com.aaditx23.bracusocial.ui.screens.Profile
import com.aaditx23.bracusocial.ui.screens.SessionInfo
import com.aaditx23.bracusocial.ui.screens.Friends
import com.aaditx23.bracusocial.ui.screens.LiveFeed
import com.aaditx23.bracusocial.ui.screens.Login_Signup
import com.aaditx23.bracusocial.ui.screens.Routine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState")
@Composable
fun Main(){
    val coursevm : CourseVM = hiltViewModel()
    val sessionvm: SessionVM = hiltViewModel()
    val accountvm: AccountVM = hiltViewModel()
    val accountproxyvm: AccountProxyVM = hiltViewModel()
    val courseList by coursevm.allCourses.collectAsState()
    val allSessions by sessionvm.allSessions.collectAsState()
    val allProfiles by accountproxyvm.allProfiles.collectAsState()
    val session by sessionvm.firstSession.collectAsState(initial = null)
    val context = LocalContext.current

    var isSessionReady by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedIndexBotNav by rememberSaveable {
        mutableIntStateOf(0)
    }
    var selectedIndexDrawer by rememberSaveable {
        mutableIntStateOf(3)
    }
    val bottomNavList by rememberSaveable {
        mutableStateOf(BottomNavItem.PrePreRegItemList)
    }
    val navDrawerItemList by rememberSaveable {
        mutableStateOf(NavDrawerItem.navDrawerItems)
    }
    var dbStatus by rememberSaveable {
        mutableStateOf(false)
    }
    var loginStatus by rememberSaveable {
        mutableStateOf(false)
    }

    val navController = rememberNavController()
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var scope = rememberCoroutineScope()
    var scrollState = rememberScrollState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

// Update `selectedIndexDrawer` when the destination changes
    LaunchedEffect(navBackStackEntry?.destination) {
        when (navBackStackEntry?.destination?.route) {
            "Profile" -> selectedIndexDrawer = navDrawerItemList.indexOfFirst { it.title == "Profile" }
            "Routine" -> selectedIndexDrawer = navDrawerItemList.indexOfFirst { it.title == "Routine" }
            "All Courses" -> selectedIndexDrawer = navDrawerItemList.indexOfFirst { it.title == "All Courses" }
            "PrePreReg" -> selectedIndexDrawer = navDrawerItemList.indexOfFirst { it.title == "PrePreReg" }
            "Find Room" -> selectedIndexDrawer = navDrawerItemList.indexOfFirst { it.title == "Find Room" }
            "About App" -> selectedIndexDrawer = navDrawerItemList.indexOfFirst { it.title == "About App" }
            "Signup/Login" -> selectedIndexDrawer = navDrawerItemList.indexOfFirst { it.title == "Signup/Login" }
            "Logout" -> selectedIndexDrawer = navDrawerItemList.indexOfFirst { it.title == "Logout" }
            "Friends" -> selectedIndexDrawer = navDrawerItemList.indexOfFirst { it.title == "Friends" }
            // Add other routes here if needed
        }
    }

    LaunchedEffect(allSessions) {
        println(allSessions)
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            if(allSessions.isEmpty()){
                sessionvm.createSession()

            }
            else{
                dbStatus = allSessions[0].dbStatus
                loginStatus = allSessions[0].loginStatus
            }
            if(allProfiles.isEmpty()){
                demoProfiles.forEachIndexed { _, data ->
                    accountproxyvm.createProfile(data)
                }
            }
            isSessionReady = true
        }

    }


    if( isSessionReady ){
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    NavDrawer(
                        scrollState = scrollState,
                        selectedIndex = selectedIndexDrawer,
                        onClick = { item ->
                            navController.navigate(item.title)
                            selectedIndexBotNav = -1
                            scope.launch {
                                drawerState.close()
                            }
                            selectedIndexDrawer = navDrawerItemList.indexOf(item)

                        },
                        isLogin = session?.loginStatus ?: false

                    )
                }
            },
            gesturesEnabled = true
        ) {

            Scaffold(

                topBar = { TopActionBar(drawerState = drawerState, scope = scope) }
            ) {
                NavHost(navController = navController, startDestination = "All Courses (Offline)") {
                    // Routes
                    composable("Profile") {
                        Profile(accountvm, accountproxyvm, navController)
                    }
                    composable("Routine"){
                        Routine(navController)
                    }
                    composable("All Courses (Offline)") {
                        CourseScreen(dbStatus)

                    }
                    composable("All Courses (Online)"){
                        LiveFeed()
                    }
                    composable("PrePreReg") {
                        PrePreReg()
                    }
                    composable("AddCourseFromProfile") {
                        PrePreReg(true)
                    }

                    composable("Find Room") {
                        FindRoom()
                    }
                    composable("About App") {
                        SessionInfo(sessionvm)
                    }
                    composable("Signup/Login"){
                        fun login(){
                            selectedIndexDrawer = 0
                            navController.navigate("Profile")
                        }
                        Login_Signup(
                            accountvm = accountvm,
                            success = {
                                login()
                            }
                        )


                    }
                    composable("Logout"){
                        Logout(
                            accountvm,
                            logoutSuccess = {
                                selectedIndexDrawer = 8
                                accountvm.clearFriends()
                                navController.navigate("Signup/Login")

                                Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    composable("Friends"){
                        Friends()
                    }
                }
            }

        }
    }
    else{
        CircularLoadingBasic("Checking Session...")
    }
}