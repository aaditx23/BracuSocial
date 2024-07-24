package com.aaditx23.bracusocial.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
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
import com.aaditx23.bracusocial.components.BottomNavigation
import com.aaditx23.bracusocial.components.models.BottomNavItem
import com.aaditx23.bracusocial.ui.screens.PrePreReg.MakeRoutine
import com.aaditx23.bracusocial.ui.screens.PrePreReg.SavedRoutine

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState")
@Composable
fun PrePreReg(){
    var selectedIndexBotNav by rememberSaveable {
        mutableIntStateOf(0)
    }
    val bottomNavList by rememberSaveable {
        mutableStateOf(BottomNavItem.PrePreRegItemList)
    }
    var selectedCourses by remember {
        mutableStateOf(mutableListOf<Course>())
    }
    var selectedMap by remember {
        mutableStateOf(mutableMapOf<String, Boolean>())
    }
    val coursevm : CourseVM = hiltViewModel()
    val courseList by coursevm.allCourses.collectAsState()
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
        NavHost(navController = navController, startDestination = "PrePreReg" ){
            composable("PrePreReg"){
                Box(
                ){
                    MakeRoutine(
                        coursevm = coursevm,
                        courseList = courseList,
                        selectedCourseList = selectedCourses,
                        selectedMap = selectedMap,
                        addCourse = { course ->
                            if (selectedMap[course.courseName] != true) {
                                selectedMap[course.courseName] = true
                                selectedCourses =
                                    selectedCourses.toMutableList().apply { add(course) }
                            } else {
                                Toast.makeText(
                                    context,
                                    "${course.courseName} Already Added",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        removeCourse = { course ->
                            selectedMap.remove(course.courseName)
                            selectedCourses =
                                selectedCourses.toMutableList().apply { remove(course) }
                        },
                        clearRoutine = {
                            selectedMap = mutableMapOf<String, Boolean>()
                            selectedCourses = mutableListOf<Course>()
                        }
                    )
                }
            }
            composable("Saved Routine"){
                SavedRoutine(coursevm)

            }
        }

    }
}