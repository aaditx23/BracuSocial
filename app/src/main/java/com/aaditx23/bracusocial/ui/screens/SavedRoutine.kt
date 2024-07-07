package com.aaditx23.bracusocial.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.viewmodels.SessionVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SavedRoutine(){
    val sessionvm: SessionVM = hiltViewModel()
    val allSession by sessionvm.allSessions.collectAsState()

    val session by sessionvm.firstSession.collectAsState(initial = null)

    Column(
        modifier = Modifier.padding(top = 100.dp)
    ){
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    sessionvm.createSession()
                }
            }) {
            Text(text = "Create Session")
        }

            if (session != null){
                Text(text = session!!._id.toString())
                Text(text = session!!.dbStatus.toString())
                Text(text = "${allSession.size}")

                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        sessionvm.deleteSession(session!!._id)
                    }
                }) {
                    Text(text = "Delete")
                }
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        sessionvm.updateSessionDbStatus(session!!._id, !session!!.dbStatus)
                    }
                }) {
                    Text(text = "Update DB")
                }
            }
        else{
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            sessionvm.createSession()
                        }
                    }) {
                    Text(text = "Create Session")
                }
            }
        }



}