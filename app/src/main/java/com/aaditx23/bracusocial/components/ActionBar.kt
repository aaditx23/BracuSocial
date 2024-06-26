package com.aaditx23.bracusocial.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopActionBar(drawerState: DrawerState, scope: CoroutineScope){
    Box(
        modifier = Modifier
            .padding(top = 20.dp)
    ){
        Card(
            modifier = Modifier
                .requiredHeight(50.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()

            ) {
                IconButton(onClick = {
                    scope.launch{
                        drawerState.apply{
                            if (isClosed){
                                open()
                            }
                            else{
                                close()
                            }
                        }
                    }
                }
                ) {
                    Icon(Icons.Default.Menu, "Menu")
                }



            }
        }
    }
}