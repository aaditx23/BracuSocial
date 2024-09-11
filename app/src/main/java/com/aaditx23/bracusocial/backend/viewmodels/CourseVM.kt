package com.aaditx23.bracusocial.backend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.bracusocial.backend.local.models.courseArray
import com.aaditx23.bracusocial.backend.local.repositories.CourseRepository
import com.aaditx23.bracusocial.backend.local.repositories.SavedRoutineRepository
import com.aaditx23.bracusocial.backend.local.repositories.SessionRepository
import com.aaditx23.bracusocial.backend.remote.UsisCrawler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
open class CourseVM @Inject constructor(
    private val courseR: CourseRepository,
    private val srR: SavedRoutineRepository,
    private val sessionR: SessionRepository,
    private val uc: UsisCrawler
    ): ViewModel() {

    val allCourses = courseR.getAllCourses()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )


    val allClassRooms = courseR.getAllClass()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptySet()
        )
    val allLabRooms = courseR.getAllLab()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptySet()
        )

    fun deleteCourse(id: ObjectId){
        viewModelScope.launch {
            courseR.deleteCourse(id)
        }
    }

    private suspend fun populateDb(
        onSet: (s: String) -> Unit,
        setSize: (Int) -> Unit,
        setProgress: (Int) -> Unit
    ){
        onSet("Fetching data from website")
//        val allCoursesJson: MutableList<JSONObject> = uc.executeAsyncTask()
            val allCoursesJson = courseArray
        val size = allCoursesJson.size
        setSize(size)
        onSet("Data fetching Complete")
        allCoursesJson.forEachIndexed { idx, course ->
            val lab = course.getBoolean("Lab")
            val classArray = course.getJSONArray("ClassDay")
            var classRoom = course.optString("ClassRoom")
            if (classRoom[0] != '0' && classRoom[0] != '1') {
                println(classRoom)
                classRoom = classRoom.slice(1..<classRoom.length)
            }
            var classDay = ""
            for (i in 0..<classArray.length()) {
                classDay = "$classDay ${classArray[i]}"
            }
            val courseInfo: List<String>
            if (lab) {
                val labArray = course.getJSONArray("LabDay")
                var labDay = ""
                for (i in 0..<labArray.length()) {
                    labDay = "$labDay ${labArray[i]}"
                }
                courseInfo = listOf(
                    course.optString("Course"),
                    course.optString("Section"),
                    classDay,
                    course.optString("ClassTime"),
                    classRoom,
                    labDay,
                    course.optString("LabTime"),
                    course.optString("LabRoom"),
                    course.optString("Faculty")
                )
            } else {
                courseInfo = listOf(
                    course.optString("Course"),
                    course.optString("Section"),
//                    course.getJSONArray("ClassDay").join(" - "),
                    classDay,
                    course.optString("ClassTime"),
                    classRoom,
                    "-",
                    "-",
                    "-",
                    course.optString("Faculty")
                )
            }
            courseR.createCourse(courseInfo)
            onSet("Creating: ${courseInfo[0]} - ${courseInfo[1]}")
            setProgress(idx)
            if (idx == size/2) {
                onSet("Waiting 1s before continuing")
                delay(1000)
            }
        }
        onSet("Database Updated")
        sessionR.dbStatusUpdate(true)
        onSet("")
    }


    fun refreshDB(
        onSet: (s: String) -> Unit,
        status: (b: Boolean) -> Unit,
        setSize: (Int) -> Unit,
        setProgress: (Int) -> Unit
    )
    {
        viewModelScope.launch {
            status(true)
            courseR.deleteAllCourses()
            populateDb(onSet, setSize, setProgress)
            status(false)
        }
    }

    fun addSavedRoutine(courseKey: String){
        viewModelScope.launch {
            srR.createSavedRoutine(courseKey)
        }
    }

    suspend fun liveData(setLoading: (Boolean) -> Unit): MutableList<JSONObject> {
        setLoading(true)
        val allCoursesJson: MutableList<JSONObject> = uc.executeAsyncTask()
        setLoading(false)
        return allCoursesJson

    }



}