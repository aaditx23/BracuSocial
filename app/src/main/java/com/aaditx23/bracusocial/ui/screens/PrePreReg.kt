package com.aaditx23.bracusocial.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun PrePreReg(courseList: JSONArray){
    AllCourseList(courseList = courseList)

}


@Composable
fun AllCourseList(courseList: JSONArray){
    Box(
        modifier = Modifier
            .size(height = 330.dp, width = 150.dp)
            .padding(top = 100.dp)
    ) {
        LazyColumn {
            items(courseList.length()){index ->
                val course = courseList.getJSONObject(index)
                Course(course = course)
            }
        }
    }
}

@Composable
fun Course(course: JSONObject){
    val text = "${course.getString("Course")} - ${course.getString("Section")}"
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        ElevatedCard(
            onClick = { /*TODO*/ },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp,
            ),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .padding(5.dp)


        ) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(10.dp)
            )
        }
    }
    
}