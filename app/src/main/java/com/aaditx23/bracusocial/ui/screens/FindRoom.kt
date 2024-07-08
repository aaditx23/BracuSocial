package com.aaditx23.bracusocial.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.viewmodels.RoomVM

@Composable
fun FindRoom(){
    val roomvm: RoomVM = hiltViewModel()

    val allClass = roomvm.allClassRooms.collectAsState().value.toList()
    val allLab = roomvm.allLabRooms.collectAsState().value.toList()

    Row{
        LazyColumn(
            modifier = Modifier
                .padding(top = 100.dp, bottom = 150.dp, start = 30.dp)
        ) {
            items(allClass.size){ index ->
                Text(text = "${index + 1}. ${allClass[index]}")
            }

        }
        LazyColumn(
            modifier = Modifier
                .padding(top = 100.dp, bottom = 150.dp, start = 30.dp)
        ) {
            items(allLab.size){ index ->
                Text(text = "${index + 1}. ${allLab[index]}")
            }

        }
    }
}