package com.aaditx23.bracusocial.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.ui.theme.paletteBlue1
import com.aaditx23.bracusocial.ui.theme.paletteBlue2
import com.aaditx23.bracusocial.ui.theme.paletteBlue3
import com.aaditx23.bracusocial.ui.theme.paletteBlue4
import com.aaditx23.bracusocial.ui.theme.paletteBlue5
import com.aaditx23.bracusocial.ui.theme.paletteBlue6
import com.aaditx23.bracusocial.ui.theme.paletteBlue7
import com.aaditx23.bracusocial.ui.theme.paletteBlue8
import com.aaditx23.bracusocial.ui.theme.paletteBlue9
import org.json.JSONObject

@Composable
fun SearchBar(
    action: (TextFieldValue) -> Unit,
    width: Dp = 305.dp,
    height: Dp = 40.dp,
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
            .size(width = width, height = height)
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
    action: (TextFieldValue) -> Unit,
    width: Dp = 250.dp,
    height: Dp = 40.dp,
    paddingStart: Dp = 8.dp,
    paddingEnd: Dp = 8.dp,
    cornerRadius: Dp = 5.dp,
    textSize: TextUnit = 15.sp,
    text: String ="Search Courses",
    dropDown: List<String>
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    action(searchQuery)

    val textColor = MaterialTheme.colorScheme.onSurface
    val placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // Slightly lighter for placeholder

    Row {
        BasicTextField(
            value = searchQuery,
            onValueChange = { newValue -> searchQuery = newValue },
            modifier = Modifier
                .size(width = width, height = height)
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
//        DropdownSample()
        DropDownCard(
            dropdownItems = dropDown,

        ) {

        }
    }
}

@Composable
fun DropDownCard(
    dropdownItems: List<String>,
    height: Dp = 40.dp,
    width: Dp = 150.dp,
    onItemClick: (String) -> Unit,
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
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val density = LocalDensity.current

    Card(
//        elevation = 4.dp,
        modifier = Modifier
            .padding(end = 10.dp)
            .height(40.dp)
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            },
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(paletteBlue4)
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
                    color = paletteBlue1,
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
                .width(140.dp),
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
                            textAlign = TextAlign.Center
                        )
                    }
                )
            }
        }
    }
}

fun FilterCourseList(list: List<Course>, searchQuery: String): List<Course>{
    return list.filter { course ->
        val query = searchQuery.trim().split("-")
        when (query.size) {
            1 -> course.courseName.contains(query[0].trim(), ignoreCase = true)
            2 -> course.courseName.contains(query[0].trim(), ignoreCase = true) &&
                    course.section.contains(query[1].trim(), ignoreCase = true)
            else -> false
        }
    }
}

fun FilterCourseListJson(list: List<JSONObject>, searchQuery: String): List<JSONObject> {
    return list.filter { courseJson ->
        val query = searchQuery.trim().split("-")
        val courseName = courseJson.optString("Course", "")
        val section = courseJson.optString("Section", "")

        when (query.size) {
            1 -> courseName.contains(query[0].trim(), ignoreCase = true)
            2 -> courseName.contains(query[0].trim(), ignoreCase = true) &&
                    section.contains(query[1].trim(), ignoreCase = true)
            else -> false
        }
    }
}










@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSample() {
    // State to hold expanded/collapsed status
    var expanded by remember { mutableStateOf(false) }

    // State to hold selected option


    // List of options for the dropdown
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4")
    var selectedOption by remember { mutableStateOf(options[0]) }

    // Dropdown component
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier
            .height(40.dp)
            .padding(end = 10.dp)
    ) {
        TextField(
            value = selectedOption,
            onValueChange = { selectedOption = it },
            readOnly = true,
            label = {
                Text(
                    text = "Search By",
                    fontSize = 10.sp
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            textStyle = TextStyle(fontSize = 15.sp),
            modifier = Modifier
                .fillMaxSize()
                .exposedDropdownSize()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            fontSize = 15.sp
                        )
                    },
                    onClick = {
                        selectedOption = item
                        expanded = false
                    })
            }

        }

    }

}