package com.aaditx23.bracusocial.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.bracusocial.dayList
import com.aaditx23.bracusocial.timeSlots
import com.aaditx23.bracusocial.ui.theme.paletteBlue1
import com.aaditx23.bracusocial.ui.theme.paletteBlue2
import com.aaditx23.bracusocial.ui.theme.paletteBlue4
import com.aaditx23.bracusocial.ui.theme.paletteBlue6
import com.aaditx23.bracusocial.ui.theme.paletteBlue9

@Composable
fun SearchBar(
    action: (TextFieldValue) -> Unit,
    width: Dp = 0.dp,
    height: Dp = 40.dp,
    weight: Float = 1f,
    paddingStart: Dp = 8.dp,
    paddingEnd: Dp = 8.dp,
    cornerRadius: Dp = 5.dp,
    textSize: TextUnit = 15.sp,
    text: String ="Search Courses"
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    action(searchQuery)

    val textColor = MaterialTheme.colorScheme.onSurface
    val placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // Slightly lighter for placeholder

    BasicTextField(
        value = searchQuery,
        onValueChange = { newValue -> searchQuery = newValue },
        modifier = Modifier
            .height(height)
            .then(
                if (width == 0.dp) Modifier.fillMaxWidth(weight) else Modifier.width(width)
            )
            .padding(start = paddingStart, end = paddingEnd)
            .border(1.dp, Color.Gray, RoundedCornerShape(cornerRadius))
            .padding(8.dp),
        textStyle = TextStyle(
            fontSize = textSize,
            color = textColor // Set the text color based on the theme
        ),
        decorationBox = { innerTextField ->
            if (searchQuery.text.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = text,
                        color = placeholderColor, // Set the placeholder color based on the theme
                        fontSize = textSize
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                innerTextField()
                IconButton(
                    onClick = { searchQuery = TextFieldValue("") }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                        contentDescription = "Clear search",
                        tint = Color.Gray
                    )
                }
            }
        }
    )
}

@Composable
fun SearchBarDropDown(
    searchAction: (TextFieldValue) -> Unit,
    dropDownAction: (String) -> Unit, //primary
    setFilterValue: (String) -> Unit, //secondary
    width: Dp = 250.dp,
    height: Dp = 40.dp,
    paddingStart: Dp = 8.dp,
    paddingEnd: Dp = 8.dp,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 8.dp,
    cornerRadius: Dp = 5.dp,
    textSize: TextUnit = 15.sp,
    text: String ="Search Courses",
    primaryDropDown: List<String>,
    secondaryDropDown: List<String> = emptyList(),
    currentFilterSelection: String,

    ) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    searchAction(searchQuery)


    val textColor = MaterialTheme.colorScheme.onSurface
    val placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // Slightly lighter for placeholder
    Row {
        // search bar
        if(!listOf("Day", "Time").contains(currentFilterSelection)){
            BasicTextField(
                value = searchQuery,
                onValueChange = { newValue -> searchQuery = newValue },
                modifier = Modifier
                    .height(height)

                    .padding(start = paddingStart, end = paddingEnd, top = paddingTop, bottom =  paddingBottom)
                    .border(1.dp, Color.Gray, RoundedCornerShape(cornerRadius))
                    .padding(8.dp),
                textStyle = TextStyle(
                    fontSize = textSize,
                    color = textColor // Set the text color based on the theme
                ),
                decorationBox = { innerTextField ->
                    if (searchQuery.text.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = text,
                                color = placeholderColor, // Set the placeholder color based on the theme
                                fontSize = textSize
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        innerTextField()
                        IconButton(
                            onClick = { searchQuery = TextFieldValue("") }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Backspace,
                                contentDescription = "Clear search",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            )
        }
        else{
            // secondary drop down
            DropDownCard(
                dropdownItems = secondaryDropDown,
                width = width,
                startPadding = 10.dp,
                endPadding = 10.dp,
                topPadding = paddingTop,
                bottomPadding = paddingBottom,
                onItemClick = setFilterValue,
                bgColor = when (currentFilterSelection) {
                    "Day" -> paletteBlue4
                    "Time" -> paletteBlue6
                    else -> paletteBlue1
                },
                fontColor = paletteBlue1
            )
        }

        // drop down for search


        // primary drop down for category
        DropDownCard(
            dropdownItems = primaryDropDown,
            endPadding = 10.dp,
            onItemClick = dropDownAction
        )

    }
}

@Composable
fun DropDownCard(
    dropdownItems: List<String>,
    height: Dp = 40.dp,
    width: Dp = 0.dp,
    weight: Float = 1f,
    startPadding: Dp = 0.dp,
    endPadding: Dp = 0.dp,
    topPadding: Dp = 0.dp,
    bottomPadding: Dp = 0.dp,
    onItemClick: (String) -> Unit,
    bgColor: Color = paletteBlue2,
    fontColor: Color = paletteBlue9

) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedText by remember{
        mutableStateOf(dropdownItems[0])
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    var widthDp by remember { mutableStateOf(0.dp) } // State to hold the width
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val density = LocalDensity.current
    LaunchedEffect(key1 = dropdownItems) {
        selectedText = dropdownItems[0]
    }

//    println("Inside drop down $dropdownItems $selectedText ${dropdownItems[0]}")
    Card(
//        elevation = 4.dp,
        modifier = Modifier
            .padding(start = startPadding, end = endPadding, top = topPadding, bottom = bottomPadding)
            .height(height)
            .then(
                if (width == 0.dp) Modifier.fillMaxWidth(weight) else Modifier.width(width)
            )
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            },
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(bgColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(true) {
                    detectTapGestures(
                        onPress = {
                            isContextMenuVisible = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        },
                    )
                },
            contentAlignment = Alignment.Center
        ) {
                Text(
                    text = selectedText,
                    fontWeight = FontWeight.Bold,
                    color = fontColor,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
        }
        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = {
                isContextMenuVisible = false
            },
            modifier = Modifier
                .onSizeChanged { size ->
                    // Convert the width from pixels to dp
                    widthDp = with(density) { size.width.toDp() }
                }
                .padding(start = startPadding, end = endPadding),
            offset = DpOffset(0.dp, 10.dp)
        ) {
            dropdownItems.forEach {
                DropdownMenuItem(
                    onClick = {
                        onItemClick(it)
                        isContextMenuVisible = false
                        selectedText = it
                    },
                    text = {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                )
            }
        }
    }
}