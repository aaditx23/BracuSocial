package com.aaditx23.bracusocial.ui.screens.Friends

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.models.FriendProfile
import com.aaditx23.bracusocial.backend.remote.AccountProxyVM
import com.aaditx23.bracusocial.backend.remote.ProfileProxy
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import com.aaditx23.bracusocial.backend.viewmodels.FriendsVM
import com.aaditx23.bracusocial.components.Pic_Name_ID
import com.aaditx23.bracusocial.components.stringToBitmap
import com.aaditx23.bracusocial.ui.theme.palette2DarkRed
import com.aaditx23.bracusocial.ui.theme.palette7Blue1
import com.aaditx23.bracusocial.ui.theme.palette7Green2
import com.aaditx23.bracusocial.ui.theme.palette7Paste1
import com.aaditx23.bracusocial.ui.theme.paletteBlue5
import com.aaditx23.bracusocial.ui.theme.paletteBlue6
import com.aaditx23.bracusocial.ui.theme.paletteBlue7
import kotlinx.coroutines.delay

@Composable
fun FindFriends(friendsvm: FriendsVM){
    val accountproxyvm : AccountProxyVM = hiltViewModel()
    val allAccounts by accountproxyvm.allProfiles.collectAsState()
    val accountvm: AccountVM = hiltViewModel()
    val profiles = accountvm.allProfiles.collectAsState()


    if (allAccounts.isNotEmpty()){
        val me = profiles.value[0]
        LazyColumn(
            modifier = Modifier
                .padding(top = 70.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            items(allAccounts){ profile ->
                if(me.studentId != profile.studentId && !me.addedFriends.contains(profile.studentId)) {
                    AddFriendRow(friend = profile, friendvm = friendsvm)
                }

            }
        }
    }

}

@Composable
fun AddFriendRow(friend: ProfileProxy, friendvm: FriendsVM){
    val context = LocalContext.current

    var sentAlready by rememberSaveable {
        mutableStateOf(false)
    }
    friendvm.isInRequest(
        friend = friend.studentId,
        result = { r ->
            sentAlready = r
        }
    )
    println("$sentAlready sent already")

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
                name = friend.studentName,
                id = friend.studentId,
                pic = friend.profilePicture
            )

            if(sentAlready){
                IconButton(
                    onClick = {
                        friendvm.cancelSentRequest(friend.studentId)
                        Toast.makeText(context, "${friend.studentName} Request Sent", Toast.LENGTH_SHORT).show()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = "Cancel Request",
                        tint = palette7Blue1,
                        )
                }
            }
            else{
                IconButton(
                    onClick = {
                        friendvm.sendRequest(friend.studentId)
                        Toast.makeText(context, "${friend.studentName} Request Cancelled", Toast.LENGTH_SHORT).show()
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