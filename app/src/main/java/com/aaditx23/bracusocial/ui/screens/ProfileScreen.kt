package com.aaditx23.bracusocial.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aaditx23.bracusocial.MainActivity
import com.aaditx23.bracusocial.R
import com.aaditx23.bracusocial.components.CircularLoadingBasic
import com.aaditx23.bracusocial.components.ImagePicker
import com.aaditx23.bracusocial.components.bitmapToString
import com.aaditx23.bracusocial.components.drawableToBitmap
import com.aaditx23.bracusocial.components.stringToBitmap
import com.aaditx23.bracusocial.ui.theme.paletteBlue5
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

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
    val scope = rememberCoroutineScope()
    LaunchedEffect(allProfile) {
        CoroutineScope(Dispatchers.IO).launch { 
            delay(200)
            if(allProfile.isNotEmpty()){
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
                navController = navController,
                accountvm = accountvm,
            )
            HorizontalDivider()

//            LazyColumn{
//                item {
//                    Text(text = allProfile[0].studentId.toString())
//                    Text(text = allProfile[0].studentName)
//                    Text(text = allProfile[0].enrolledCourses.split(",").toString())
//                    Text(text = allProfile[0].addedFriends.split(",").toString())
//                    Text(text = allProfile[0].friendRequests.split(",").toString())
////                    Text(text = allProfile[0].profilePicture)
//                    Text(text = allProfile[0].email)
//                    HorizontalDivider()
//                    allProxyProfile.forEachIndexed { _, profile ->
//                        Text(text = profile.studentId.toString())
//                        Text(text = profile.studentName)
//                        Text(text = profile.enrolledCourses.split(",").toString())
//                        Text(text = profile.addedFriends.split(",").toString())
//                        Text(text = profile.friendRequests.split(",").toString())
////                        Text(text = profile.profilePicture)
//                        Text(text = profile.email)
//                    }
//                    HorizontalDivider()
//                }
//            }

        }
    }
}

@Composable
fun ProfilePage(
    profile: Profile,
    navController: NavHostController,
    accountvm: AccountVM
) {
    val (isEditing, setEditing) = remember { mutableStateOf(false) }
    val (name, setName) = remember { mutableStateOf(profile.studentName) }
    val context = LocalContext.current

    var updatingName by remember {
        mutableStateOf(false)
    }
    var showImagePicker by remember {
        mutableStateOf(false)
    }
    var enableImageSelect by remember {
        mutableStateOf(true)
    }
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    var profileImage by remember { mutableStateOf(MainActivity.EmptyImage.emptyProfileImage) }
    var showToast by remember {
        mutableStateOf(false)
    }
    var imageUpload by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            profileImage = withContext(Dispatchers.IO){
                stringToBitmap(profile.profilePicture)!!
            }
            isLoading = false
        }
    }


    if(isLoading){
        CircularLoadingBasic("Loading profile...")
    }
    else{
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = BitmapPainter(image = profileImage.asImageBitmap() ),
                        contentDescription = name,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(
                                1.dp,
                                color = paletteBlue5,
                                shape = CircleShape
                            ),
                        contentScale = ContentScale.FillWidth,
                    )
                    TextButton(
                        onClick = {
                            showImagePicker = true
                            enableImageSelect = false
                        },
                        enabled = enableImageSelect
                    ) {
                        Text("Edit Image")
                    }
                }

                val mod = Modifier
                    .padding(start = 10.dp, top = 15.dp)
                    .weight(1f)
                Column(
                    modifier = mod
                ){
                    Text(
                        text = name,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .padding(4.dp)
                    )
                    Text(
                        text = profile.studentId,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(5.dp)
                    )
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


    if (showImagePicker){
        ImagePicker {image ->
            scope.launch{
//                println("CALLED IMAGE PICKER $showImagePicker")
                profileImage = image
                accountvm.updatePic(
                    bitmapToString(profileImage),
                    result = { r ->
                        imageUpload = r
                        showToast = true
                    }
                )
//                println("$image FOUND")
                enableImageSelect = true
                showImagePicker = false
            }
        }
    }
    if(showToast){
        if (imageUpload) {
            Toast.makeText(context, "Image Updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Error uploading photo", Toast.LENGTH_SHORT).show()
        }
        showToast = false
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