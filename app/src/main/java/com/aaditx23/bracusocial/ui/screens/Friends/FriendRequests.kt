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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.PersonAddDisabled
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.remote.AccountProxyVM
import com.aaditx23.bracusocial.backend.remote.ProfileProxy
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import com.aaditx23.bracusocial.backend.viewmodels.FriendsVM
import com.aaditx23.bracusocial.ui.theme.palette7Blue1
import com.aaditx23.bracusocial.ui.theme.palette7Green2
import com.aaditx23.bracusocial.ui.theme.palette7Paste1
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun FriendRequests(friendvm: FriendsVM){
    val accountproxyvm: AccountProxyVM = hiltViewModel()
    val accountvm: AccountVM = hiltViewModel()
    val profile by accountvm.allProfiles.collectAsState()

    val scope = rememberCoroutineScope()

    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    var requests by rememberSaveable {
        mutableStateOf("")
    }
    var trigger by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(requests, trigger) {
        println("$requests triggered")
        scope.launch {
            delay(100)
            if(profile.isNotEmpty()){
                isLoading = false
                requests = profile[0].friendRequests
            }
        }
    }

    if(requests == ""){
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center

        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Button(onClick = {
                    friendvm.sendRequest(
                        "21201617"
                    )
                    friendvm.sendRequest(
                        "21201616"
                    )
                    friendvm.sendRequest(
                        "21201615"
                    )

                }) {
                    Text(text = "ADD MANUALLY")
                }
                Text(text = "No Friends Added")

            }
        }
    }
    else{

        LazyColumn(
            modifier = Modifier
                .padding(top = 70.dp)
        ) {
            items(requests.split(",")){ s ->
                if(s!=""){
                    println("$s is friend")
                    accountproxyvm.getProfile(s)
                        ?.let { RequestRow(friend = it, friendvm = friendvm, trigger = { trigger = ! trigger } ) }
                }
            }
        }

    }

}

@Composable
fun RequestRow(friend: ProfileProxy, friendvm: FriendsVM, trigger: () -> Unit){
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
                    friendvm.addFriend(friend.studentId)
                    Toast.makeText(context, "${friend.studentName} Added", Toast.LENGTH_SHORT).show()
                    trigger()
                },
            ) {
                    Icon(
                        imageVector = Icons.Filled.PersonAdd,
                        contentDescription = "Add",
                        tint = palette7Blue1
                    )
            }
            IconButton(
                onClick = {
                    friendvm.cancelRequest(friend.studentId)
                    Toast.makeText(context, "${friend.studentName} Removed", Toast.LENGTH_SHORT).show()
                    trigger()
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.PersonAddDisabled,
                    contentDescription = "Cancel Request",
                    tint = palette7Blue1
                )
            }
        }
    }
}