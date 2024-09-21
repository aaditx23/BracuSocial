package com.aaditx23.bracusocial.ui.screens.Friends

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.material.icons.outlined.Cancel
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.aaditx23.bracusocial.backend.remote.AccountProxyVM
import com.aaditx23.bracusocial.backend.remote.RemoteProfile
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import com.aaditx23.bracusocial.backend.viewmodels.FriendsVM
import com.aaditx23.bracusocial.checkInternetConnection
import com.aaditx23.bracusocial.components.CircularLoadingBasic
import com.aaditx23.bracusocial.components.DropDownCard
import com.aaditx23.bracusocial.components.Pic_Name_ID
import com.aaditx23.bracusocial.components.SearchBar
import com.aaditx23.bracusocial.ui.theme.palette7Blue1
import com.aaditx23.bracusocial.ui.theme.paletteBlue6
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FindFriends(friendsvm: FriendsVM){
    val accountproxyvm : AccountProxyVM = hiltViewModel()
    val accountvm: AccountVM = hiltViewModel()
    val context = LocalContext.current
    val allAccounts by accountproxyvm.allProfiles.collectAsState()
    val profiles by accountvm.allProfiles.collectAsState()

    var isLoading by remember { mutableStateOf(true) }

//    val profiles = remember { mutableStateOf(emptyList<Profile>()) }
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var filteredProfileList by remember { mutableStateOf(emptyList<RemoteProfile>()) }
    var isFiltering by remember { mutableStateOf(false) }
    var hasInternet by remember{
        mutableStateOf(checkInternetConnection(context))
    }
    val filter = listOf(
        "ID",
        "Name",
        "Course Section",
    )
    var currentFilter by rememberSaveable {
        mutableStateOf(filter[0])
    }
    LaunchedEffect(Unit) {
        scope.launch {
            delay(200)
            if(profiles.isNotEmpty()){
                isLoading = false
            }

        }
    }

    LaunchedEffect(searchQuery, currentFilter) {
        println("Current filter friends $currentFilter")

        scope.launch {
            if(searchQuery.text.isNotEmpty()){
                isFiltering = true
                when (currentFilter) {
                    filter[0] -> friendsvm.filterProfilesByID(
                        searchQuery = searchQuery.text,
                        onResult = { list ->
                            filteredProfileList = list
                        }
                    )

                    filter[1] -> friendsvm.filterProfilesByName(
                        searchQuery = searchQuery.text,
                        onResult = { list ->
                            filteredProfileList = list
                        }
                    )

                    filter[2] -> friendsvm.filterProfilesByCourse(
                        searchQuery = searchQuery.text.uppercase(),
                        onResult = { list ->
                            println("Filtering by course")
                            filteredProfileList = list
                        }
                    )

                    else -> emptyList<RemoteProfile>()
                }


                isFiltering = false
            }
            else{
                filteredProfileList = emptyList()
            }
        }


    }
    if(isLoading){
        CircularLoadingBasic("Loading Users...")
    }
    else if(isFiltering){
        CircularLoadingBasic("Searching ${searchQuery.text}")
    }
    else{
        val me = profiles[0]

        Column(
            modifier = Modifier.
            padding(top = 70.dp)
        ) {
            Row{
                SearchBar(
                    action = { query ->
                        searchQuery = query
                    },
                    width = (LocalConfiguration.current.screenWidthDp - 150).dp,
                    height = 40.dp,
                    textSize = 16.sp,
                    text = "Search Users...",
                )
                DropDownCard(
                    dropdownItems = filter,
                    onItemClick = { value ->
                        currentFilter = value
                    },
                    endPadding = 8.dp,
                    width = 150.dp
                )
            }
            if(filteredProfileList.isEmpty()){
                Text(
                    text = "Search to find users...",
                    modifier = Modifier
                        .padding(top = 100.dp)
                        .fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                items(filteredProfileList) { profile ->
                    if (me.studentId != profile.studentId && !me.addedFriends.contains(profile.studentId)) {
                        AddFriendRow(friend = profile, friendvm = friendsvm)
                    }

                }
            }
        }
    }






}

@Composable
fun AddFriendRow(friend: RemoteProfile, friendvm: FriendsVM){
    val context = LocalContext.current

    var sentAlready by rememberSaveable {
        mutableStateOf(false)
    }
    var inMyRequests by rememberSaveable {
        mutableStateOf(false)
    }
    friendvm.isInRequest(
        friend = friend.studentId,
        result = { r ->
            sentAlready = r
        }
    )
    friendvm.isInMyRequest(
        friend = friend.studentId,
        result = { r ->
            inMyRequests = r
        }
    )
    ElevatedCard(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        ),
        colors = CardDefaults.cardColors(paletteBlue6)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Pic_Name_ID(
                name = friend.name,
                id = friend.studentId,
                pic = friend.profilePicture
            )

            if( sentAlready){
                IconButton(
                    onClick = {
                        friendvm.cancelSentRequest(friend.studentId)
                        sentAlready = false
                        Toast.makeText(context, "${friend.name} Request Cancelled", Toast.LENGTH_SHORT).show()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = "Cancel Request",
                        tint = palette7Blue1,
                        )
                }
            }
            else if (inMyRequests){
                IconButton(
                    onClick = {
                        Toast.makeText(context, "Go to Friend Requests page", Toast.LENGTH_SHORT).show()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.QueryBuilder,
                        contentDescription = "Send Request",
                        tint = palette7Blue1
                    )
                }
            }
            else{
                IconButton(
                    onClick = {
                        friendvm.sendRequest(friend.studentId)
                        sentAlready = true
                        Toast.makeText(context, "${friend.name} Request Sent", Toast.LENGTH_SHORT).show()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.PersonAddAlt1,
                        contentDescription = "Send Request",
                        tint = palette7Blue1
                    )
                }
            }
        }
    }
}