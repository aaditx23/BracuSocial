package com.aaditx23.bracusocial.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aaditx23.bracusocial.backend.local.models.Profile
import com.aaditx23.bracusocial.backend.remote.AccountProxyVM
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun Profile(
    accountvm: AccountVM,
    accountproxyvm: AccountProxyVM,
    navController: NavHostController
){
    val allProfile by accountvm.allProfiles.collectAsState()
    val allProxyProfile by accountproxyvm.allProfiles.collectAsState()
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    LaunchedEffect(allProfile) {
        CoroutineScope(Dispatchers.IO).launch { 
            delay(200)
            if(allProfile.isNotEmpty() && allProxyProfile.isNotEmpty()){
//                profile = allProfile[0]
                isLoading = false
            }
        }
    }
    if (isLoading){
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(text = "Please login to view profile")
        }
    }
    else if (allProfile.isNotEmpty()){
        Column(modifier = Modifier
            .padding(top = 100.dp)
        ){

            ProfilePage(
                profile = allProfile[0],
                navController = navController
            )
            HorizontalDivider()

            LazyColumn{
                item {
                    Text(text = allProfile[0].studentId.toString())
                    Text(text = allProfile[0].studentName)
                    Text(text = allProfile[0].enrolledCourses.split(",").toString())
                    Text(text = allProfile[0].addedFriends.split(",").toString())
                    Text(text = allProfile[0].friendRequests.split(",").toString())
                    HorizontalDivider()
                    allProxyProfile.forEachIndexed { _, profile ->
                        Text(text = profile.studentId.toString())
                        Text(text = profile.studentName)
                        Text(text = profile.enrolledCourses.split(",").toString())
                        Text(text = profile.addedFriends.split(",").toString())
                        Text(text = profile.friendRequests.split(",").toString())
                    }
                    HorizontalDivider()
                }
            }

        }
    }
}

@Composable
fun ProfilePage(profile: Profile, navController: NavHostController) {
    val (isEditing, setEditing) = remember { mutableStateOf(false) }
    val (name, setName) = remember { mutableStateOf(profile.studentName) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isEditing) {
                TextField(
                    value = name,
                    onValueChange = { setName(it) },
                    modifier = Modifier.weight(1f),
                    label = { Text("Student Name") }
                )
                IconButton(onClick = {
                    setEditing(false)
                    // Save action to be handled later
                }) {
                    Icon(Icons.Filled.Save, contentDescription = "Save")
                }
            } else {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { setEditing(true) }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
            }
        }

        ElevatedCardSection(
            title = "Enrolled Courses",
            items = profile.enrolledCourses.split(","),
            navController = navController
        )
        ElevatedCardSection(title = "Friends", items = profile.addedFriends.split(","))
    }
}

@Composable
fun ElevatedCardSection(
    title: String,
    items: List<String>,
    navController: NavHostController? = null
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = title, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
                if(title == "Enrolled Courses"){
                    IconButton(onClick = {
                        navController!!.navigate("AddCourseFromProfile")
                        },
                        modifier = Modifier
                            .size(25.dp)
                    ) {
                        Icon(
                            Icons.Filled.Edit, contentDescription = "Edit",
                            modifier = Modifier
                                .size(15.dp)
                            )
                    }
                }
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if(items.contains("") && items.size == 1){
                    item{
                        Text(
                            text = "No friends Added",
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                }
                else{
                    items(items) { item ->
                        if(item != ""){
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = item.trim(),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}