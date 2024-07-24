package com.aaditx23.bracusocial.ui.screens.Friends

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.bracusocial.backend.local.models.FriendProfile
import com.aaditx23.bracusocial.backend.viewmodels.FriendsVM
import com.aaditx23.bracusocial.ui.theme.palette2DarkRed
import com.aaditx23.bracusocial.ui.theme.palette7Green2
import com.aaditx23.bracusocial.ui.theme.palette7Paste1
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MyFriends(friendvm: FriendsVM){
    val allFriends by friendvm.allFriendProfiles.collectAsState()
    var isEmpty by rememberSaveable {
        mutableStateOf(true)
    }
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    val scope = rememberCoroutineScope()


    LaunchedEffect(allFriends) {
        scope.launch {
            delay(100)
            if (allFriends.isNotEmpty()){
                println(allFriends.size)
                isEmpty = false
            }
            else{
                isEmpty = true
            }
            isLoading = false
        }
    }


    if (isLoading){
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center

        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Checking FriendList")
                CircularProgressIndicator()
            }
        }
    }
    else if (isEmpty){
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center

        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "No Friends Added")
            }
        }
    }
    else{
        LazyColumn(
            modifier = Modifier
                .padding(top = 80.dp)
        ){

            items(allFriends){ friend ->
                FriendRow(friend, friendvm)
            }
        }

    }
}

@Composable
fun FriendRow(friend: FriendProfile, friendvm: FriendsVM){
    val context = LocalContext.current
    ElevatedCard(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        ),
        colors = CardDefaults.cardColors(palette7Paste1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(
                    text = friend.studentName,
                    color = palette7Green2,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = friend.studentId,
                    color = palette7Green2,
                    fontSize = 15.sp
                )
            }
            IconButton(
                onClick = {
                    friendvm.unfriend(friend.studentId)
                    Toast.makeText(context, "${friend.studentName} removed", Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.DeleteForever,
                    contentDescription = "Unfriend",
                    tint = palette2DarkRed
                )
            }
        }
    }
}