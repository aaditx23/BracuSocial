package com.aaditx23.bracusocial.backend.remote

import com.aaditx23.bracusocial.backend.local.models.Course
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.coroutines.*

class UsisCrawler{
    companion object{

    }

    private var courseKey: MutableList<String> = mutableListOf("")


    private fun timeFormat(dict: JSONObject): Boolean {
        val raw = dict.getString("ClassTime")
        val processed = processTime(raw)

        dict.put("ClassTime", "")

        processed.forEachIndexed { _, map ->
            if (!isValidTime(map["time"].toString())) {
                return false
            }
            val classTime = dict.getString("ClassTime")
            val lab = dict.getBoolean("Lab")
            if(map["type"] == "Class") {
                if(classTime == ""){
                    dict.put("ClassTime", map["time"])
                    dict.put("ClassDay", map["day"])
                    dict.put("ClassRoom", map["room"])
                }
                else{
                    val day = dict.getString("ClassDay")
                    dict.put("ClassDay", "$day ${map["day"]}")
                }
            }
            else{
                dict.put("Lab", true)
                if(!lab){
                    dict.put("LabTime", map["time"])
                    dict.put("LabDay", map["day"])
                    dict.put("LabRoom", map["room"])
                }
                else{
                    val day = dict.getString("LabDay").toString()
                    dict.put("LabDay", "$day ${map["day"]}")
                }
            }

        }
        return true

    }

    private fun isValidTime(time: String): Boolean{
        val timeSlots = arrayOf(
            "08:00 AM-09:20 AM",
            "09:30 AM-10:50 AM",
            "11:00 AM-12:20 PM",
            "12:30 PM-01:50 PM",
            "02:00 PM-03:20 PM",
            "03:30 PM-04:50 PM",
            "05:00 PM-06:20 PM",
            "06:30 PM-08:00 PM",
            "08:00 AM-10:50 AM",
            "11:00 AM-01:50 PM",
            "02:00 PM-04:50 PM",
            "05:00 PM-08:00 PM"
        )

        return timeSlots.contains(time)
    }

    private fun dataFormat(rows: Elements): JSONArray {
        val courseList = JSONArray()
        for (row in rows) {
            val data = row.select("td")
            if (data.size >= 10) {
                val courseCode = data[1].text().trim()
                val faculty = data[3].text().trim()
//                if (courseCode.contains("TSL") || courseCode.contains("PSM") || courseCode.contains("HUM103")
//                    || courseCode.contains("LAW101") || courseCode.contains("LAW203") || courseCode.contains("ENG102")
//                    || courseCode.contains("EMB101") || courseCode.contains("BNG103") || courseCode.contains("PHR")
//                    || courseCode.contains("ARC") || courseCode.contains("ACT511")
//                ) continue
                val section = data[5].text().trim()
                if (section.contains("OM", ignoreCase = true) || section.contains("closed", ignoreCase = true)) continue
                val timings = data[6].text().trim()
                if ("UB0000" in timings) continue
                val courseInfo = JSONObject()
                courseInfo.put("Course", courseCode)
                courseInfo.put("Section", section)
                courseInfo.put("ClassTime", timings)
                courseInfo.put("Lab", false)
                courseInfo.put("Faculty", faculty)
                val timeCheckFlag = timeFormat(courseInfo)
                if( !timeCheckFlag){
                    continue
                }

                courseList.put(courseInfo)
//                println(courseList.toString(4))
            }
        }
        return courseList
    }

    private fun createCourseList(json: JSONArray){
        val temp: MutableSet<String> = mutableSetOf()


        for( i in 0 until json.length()){
            val jsonObject = json.getJSONObject(i)
            val course = jsonObject.optString("Course")
            val section = jsonObject.optString("Section")
            if (course.isNotEmpty()){
                temp.add("$course $section")
            }
        }
        courseKey = temp.toMutableList()

    }



    suspend fun executeAsyncTask(): MutableList<JSONObject> {
        return withContext(Dispatchers.IO) {
            val url =
                "https://usis.bracu.ac.bd/academia/admissionRequirement/getAvailableSeatStatus"
            val doc: Document = Jsoup.connect(url).get()
            val rows: Elements = doc.select("tr")
            println(rows.size)
            val courses = dataFormat(rows)
            val courseList = mutableListOf<JSONObject>()
            for (i in 0 until courses.length()) {
                val jsonObject = courses.getJSONObject(i)
                courseList.add(jsonObject)
            }

//             You can still use createCourseList(courseList) if it accepts MutableList<JSONObject>
            createCourseList(courses)

            //val file = File("course_info.json")
            //file.writeText(courseList.toString())

            println("Extraction completed. Data saved to 'course_info.json'. ${courseList.size}")

            courseList
        }
    }

}