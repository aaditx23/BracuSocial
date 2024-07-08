package com.aaditx23.bracusocial.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.backend.local.viewmodels.CourseVM
import com.aaditx23.bracusocial.components.BottomNavigation
import com.aaditx23.bracusocial.components.NavDrawer
import com.aaditx23.bracusocial.components.TopActionBar
import com.aaditx23.bracusocial.components.models.BottomNavItem
import com.aaditx23.bracusocial.ui.screens.CourseScreen
import com.aaditx23.bracusocial.ui.screens.FindRoom
import com.aaditx23.bracusocial.ui.screens.PrePreReg
import com.aaditx23.bracusocial.ui.screens.SavedRoutine
import org.json.JSONArray
import org.json.JSONObject


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState")
@Composable
fun Main(){
    val coursevm : CourseVM = hiltViewModel()
    val courseList by coursevm.allCourses.collectAsState()
    val context = LocalContext.current

    var selectedIndexBotNav by rememberSaveable {
        mutableIntStateOf(0)
    }
    var selectedIndexDrawer by rememberSaveable {
        mutableIntStateOf(-1)
    }
    val bottomNavList by rememberSaveable {
        mutableStateOf(BottomNavItem.bottomNavItemList)
    }
    val selectedMap by remember {
        mutableStateOf(mutableMapOf<String, Boolean>())
    }

    var allCourses by remember {
        mutableStateOf(courseList)
    }
    var availableCourses by remember {
        mutableStateOf(courseList)
    }
    var selectedCourses by remember {
        mutableStateOf(mutableListOf<Course>())
    }

    val navController = rememberNavController()
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var scope = rememberCoroutineScope()
    var scrollState = rememberScrollState()


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
                    println("Selected index $index size ${bottomNavList.size}")
                    selectedIndexBotNav = index
                    selectedIndexDrawer = -1
                    println("$selectedIndexDrawer $selectedIndexBotNav")
                    navController.navigate(bottomNavList[index].title)

                }
            },
            topBar = { TopActionBar(drawerState = drawerState, scope = scope ) }
        ){
            NavHost(navController = navController, startDestination = "All Courses" ){
                // Routes
                composable("All Courses"){
                    CourseScreen()
                }
                composable("PrePreReg"){
                    Box(

                    ){
                        PrePreReg(
                            selectedCourseList = selectedCourses,
                            selectedMap = selectedMap,
                            addCourse = {course ->
                                if (selectedMap[course.courseName] != true){
                                    selectedMap[course.courseName] = true
                                    selectedCourses = selectedCourses.toMutableList().apply{ add(course) }
                                }
                                else{
                                    Toast.makeText(context, "${course.courseName} Already Added", Toast.LENGTH_SHORT).show()
                                }

                            },
                            removeCourse = {course->
                                selectedMap.remove(course.courseName)
                                selectedCourses = selectedCourses.toMutableList().apply{ remove(course) }


                            }
                        )
                    }
                }
                composable("Saved Routine"){
                    SavedRoutine()

                }
                composable("Find Room"){
                    FindRoom()
                }
                composable("About BUCC"){

                }
            }
        }

    }
}