package com.aaditx23.bracusocial.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.bracusocial.backend.local.models.Course
import org.json.JSONObject

@Composable
fun SearchBar(
    action: (TextFieldValue) -> Unit,
    width: Dp = 305.dp,
    height: Dp = 40.dp,
    paddingStart: Dp = 8.dp,
    paddingEnd: Dp = 8.dp,
    cornerRadius: Dp = 5.dp,
    textSize: TextUnit = 15.sp
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
                        text = "Search Courses",
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
