package com.aaditx23.bracusocial.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.bracusocial.backend.local.models.Course
import com.aaditx23.bracusocial.ui.theme.paletteBlue1
import com.aaditx23.bracusocial.ui.theme.paletteBlue2
import com.aaditx23.bracusocial.ui.theme.paletteBlue3
import com.aaditx23.bracusocial.ui.theme.paletteBlue4
import com.aaditx23.bracusocial.ui.theme.paletteBlue9
import org.json.JSONObject


data class CourseInfo(
    var courseName: String,
    var section: String,
    var faculty: String,
    var classDay: String,
    var classTime: String,
    var classRoom: String,
    var labDay: String,
    var labTime: String,
    var labRoom: String,
)



@Composable
fun CallCourseItem(
    courseJson: JSONObject? = null,
    courseData: Course? = null
) {

    if (courseData == null && courseJson != null){
        val courseName = courseJson.optString("Course", "N/A")
        val section = courseJson.optString("Section", "N/A")
        val faculty = courseJson.optString("Faculty", "TBA")
        val classTime = courseJson.optString("ClassTime", "N/A")
        val classDays = courseJson.optJSONArray("ClassDay")?.let { jsonArray ->
            (0 until jsonArray.length()).joinToString(" ") { index ->
                jsonArray.optString(index)
            }
        } ?: "N/A"
        val classRoom = courseJson.optString("ClassRoom", "N/A")

        val hasLab = courseJson.optBoolean("Lab", false)
        val labDays = if (hasLab) courseJson.optJSONArray("LabDay")?.let { jsonArray ->
            (0 until jsonArray.length()).joinToString(" ") { index ->
                jsonArray.optString(index)
            }
        } ?: "N/A" else null
        val labTime = if (hasLab) {
            courseJson.optJSONArray("LabTime")?.let { jsonArray ->
                (0 until jsonArray.length()).joinToString(" ") { index ->
                    jsonArray.optString(index)
                }
            } ?: courseJson.optString("LabTime", "-")
        } else null

        val labRoom = if (hasLab) courseJson.optString("LabRoom", "-") else null

        CourseCard(
            CourseInfo(
                courseName = courseName,
                section = section,
                faculty = faculty,
                classTime = classTime,
                classDay = classDays,
                classRoom = classRoom,
                labDay = if(hasLab) labDays!! else "-",
                labRoom = if(hasLab) labRoom!! else "-",
                labTime = if(hasLab) labTime!! else "-"
            )
        )
    }
    else if(courseData != null && courseJson == null){
        CourseCard(
            CourseInfo(
                courseName = courseData.courseName,
                section = courseData.section,
                faculty = courseData.faculty,
                classTime = courseData.classTime!!,
                classDay = courseData.classDay!!,
                classRoom = courseData.classRoom!!,
                labDay = courseData.labDay!!,
                labRoom = courseData.labRoom!!,
                labTime = courseData.labTime!!,
            )
        )
    }


}

@Composable
fun CourseCard(course: CourseInfo){
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(paletteBlue9),
        elevation = CardDefaults.elevatedCardElevation(5.dp)
    ) {


        Column(
            modifier = Modifier
                .padding(8.dp)
        ){
            Text(
                text = "${course.courseName} - ${course.section}",
                fontWeight = FontWeight.Black,
                fontSize = 23.sp,
                textAlign = TextAlign.Center,
                color = paletteBlue1,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = course.faculty,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = paletteBlue2,
                modifier = Modifier.fillMaxWidth()
            )
            if(course.classDay != "-"){
                TitledText(
                    title = "Class Time: ",
                    text = course.classTime.toString()
                )
                TitledText(
                    title = "Class Day: ",
                    text = course.classDay.toString()
                )
                TitledText(
                    title = "Class Room: ",
                    text = course.classRoom.toString()
                )
            }
            if (course.labDay != "-") {
                TitledText(
                    title = "Lab Day: ",
                    text = course.labDay.toString()
                )
                TitledText(
                    title = "Lab Time: ",
                    text = course.labTime.toString()
                )
                TitledText(
                    title = "Lab Room: ",
                    text = course.labRoom.toString()
                )
            }
        }

    }
}


@Composable
fun TitledText(title: String, text: String){
    Row{
      Text(
          text = title,
          fontWeight = FontWeight.Bold,
          color = paletteBlue1
      )
        Text(
            text = text,
            color = paletteBlue2
        )
    }
}