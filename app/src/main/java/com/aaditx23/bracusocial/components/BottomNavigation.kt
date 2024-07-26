package com.aaditx23.bracusocial.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aaditx23.bracusocial.components.models.BottomNavItem
import com.aaditx23.bracusocial.components.models.IconMap


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(
    items: List<BottomNavItem>,
    selectedIndex: Int,
    onItemSelcted: (Int) -> Unit
){
    var navSelect by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        modifier = Modifier.height(120.dp)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    val idx = items.indexOf(item)
                    onItemSelcted(idx)
                    navSelect = idx
                    println(item.selectedIcon)
                          },
                label = {
                    Text(text = item.title)
                },
                alwaysShowLabel = true,
                icon = {
                    BadgedBox(
                        badge = {

                        }
                    ) {
                        Icon(imageVector = if (index == navSelect) IconMap(item.selectedIcon)
                                            else IconMap(item.unselectedIcon),
                            contentDescription = item.title )
                    }
                }
            )
        }
    }


}

