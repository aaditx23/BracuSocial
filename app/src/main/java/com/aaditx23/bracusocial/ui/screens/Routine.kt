package com.aaditx23.bracusocial.ui.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.viewmodels.RoutineVM
import com.aaditx23.bracusocial.ui.screens.Routine.MyRoutine

@Composable
fun Routine(){
    val routinevm: RoutineVM = hiltViewModel()
    MyRoutine(routinevm)
}