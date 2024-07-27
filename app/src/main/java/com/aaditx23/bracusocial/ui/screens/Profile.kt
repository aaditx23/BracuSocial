package com.aaditx23.bracusocial.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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

@Composable
fun Profile(accountvm: AccountVM, accountproxyvm: AccountProxyVM){
    val allProfile by accountvm.allProfiles.collectAsState()
    val allProxyProfile by accountproxyvm.allProfiles.collectAsState()
//    var profile by rememberSaveable {
//        mutableStateOf<Profile?>(null)
//    }
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