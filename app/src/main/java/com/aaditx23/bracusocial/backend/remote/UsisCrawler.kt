package com.aaditx23.bracusocial.backend.remote

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

    private fun checkLab(classTime: String): Boolean {
        val raw = classTime.split(")")
        return raw.any { it.endsWith("L") }
    }

    private fun getClassTime(classList: List<String>): Triple<List<String>, String, String> {
        val day = mutableListOf<String>()
        var time = ""
        var room = ""
        when (classList.size) {
            2 -> {
                val d1 = classList[0]
                val d2 = classList[1]
                if (d1.substring(0, 2) != d2.substring(0, 2)) {
                    day.add(d1.substring(0, 2))
                    day.add(d2.substring(0, 2))
                    room = d1.substring(21)
                    time = "${d1.substring(3, 11)} - ${d1.substring(12,20)}"
                } else {
                    day.add(d1.substring(0, 2))
                    room = d1.substring(20)
                    time = "${d1.substring(3, 11)} - ${d1.substring(12,20)}"
                }
            }
            4 ->{
                val d1 = classList[0]
                val d2 = classList[1]
                val d3 = classList[2]
                val d4 = classList[3]
                if (d1.substring(0, 2) != d2.substring(0, 2)) {
                    day.add(d1.substring(0, 2))
                    day.add(d2.substring(0, 2))
                    day.add(d3.substring(0, 2))
                    day.add(d4.substring(0, 2))
                    room = d1.substring(21)
                    time = "${d1.substring(3, 11)} - ${d1.substring(12,20)}"
                } else {
                    day.add(d1.substring(0, 2))
                    room = d1.substring(20)
                    time = "${d1.substring(3, 11)} - ${d1.substring(12,20)}"
                }
            }
            3 ->{
                val d1 = classList[0]
                val d2 = classList[1]
                val d3 = classList[2]
                if (d1.substring(0, 2) != d2.substring(0, 2)) {
                    day.add(d1.substring(0, 2))
                    day.add(d2.substring(0, 2))
                    day.add(d3.substring(0, 2))
                    room = d1.substring(21)
                    time = "${d1.substring(3, 11)} - ${d1.substring(12,20)}"
                } else {
                    day.add(d1.substring(0, 2))
                    room = d1.substring(20)
                    time = "${d1.substring(3, 11)} - ${d1.substring(12,20)}"
                }
            }

            1 -> {
                val d1 = classList[0]
                day.add(d1.substring(0, 2))
                room = d1.substring(21)
                time = d1.substring(3,20)
            }
        }
        return Triple(day, time, room)
    }

    private fun getLabTime(labList: List<String>): Triple<List<String>, String, String> {
        val day = mutableListOf<String>()
        var time = ""
        var room = ""
        when (labList.size) {
            2 -> {
                val t1 = labList[0]
                val t2 = labList[1]
                day.add(t1.substring(0, 2))
                time = "${t1.substring(3, 11)} - ${t2.substring(12,20)}"
                room = t2.substring(21)
            }
            1 -> {
                val t1 = labList[0]
                day.add(t1.substring(0, 2))
                time = t1.substring(3,20)
                room = t1.substring(21)
            }
        }
        return Triple(day, time, room)
    }

    private fun timeFormat(dict: JSONObject) : Boolean {
        val raw = dict.getString("ClassTime")
        val rawList = raw.split(")")
        val classList = mutableListOf<String>()
        val labList = mutableListOf<String>()
        for (time in rawList) {
            when {
                time.endsWith("C") -> classList.add(time.trim())
                time.endsWith("L") -> labList.add(time.trim())
            }
        }
        dict.remove("ClassTime")
        if (classList.isNotEmpty()) {
            val processedClass = getClassTime(classList)
            dict.put("ClassDay", JSONArray(processedClass.first))
            val time = processedClass.second
            if (!isValidTime(time)){
                println("Here are the timings: $time")
                return false
            }
            dict.put("ClassTime", processedClass.second)
            dict.put("ClassRoom", processedClass.third)
        }
        if (labList.isNotEmpty()) {
            val processedLab = getLabTime(labList)
            dict.put("LabDay", JSONArray(processedLab.first))
            dict.put("LabTime", processedLab.second)
            dict.put("LabRoom", processedLab.third)
        }

        return true
    }

    private fun isValidTime(time: String): Boolean{
        val timeSlots = arrayOf(
            "08:00 AM - 09:20 AM",
            "09:30 AM - 10:50 AM",
            "11:00 AM - 12:20 PM",
            "12:30 PM - 01:50 PM",
            "02:00 PM - 03:20 PM",
            "03:30 PM - 04:50 PM",
            "05:00 PM - 06:20 PM",
            "06:30 PM - 08:00 PM",
            "08:00 AM - 10:50 AM",
            "11:00 AM - 01:50 PM",
            "02:00 PM - 04:50 PM",
            "05:00 PM - 08:00 PM"
        )

        return timeSlots.contains(time)
    }

    private fun dataFormat(rows: Elements): JSONArray {
        val courseList = JSONArray()
        for (row in rows) {
            val data = row.select("td")
            if (data.size >= 10) {
                val courseCode = data[1].text().trim()
                if (courseCode.contains("TSL") || courseCode.contains("PSM") || courseCode.contains("HUM103")
                    || courseCode.contains("LAW101") || courseCode.contains("LAW203") || courseCode.contains("ENG102")
                    || courseCode.contains("EMB101") || courseCode.contains("BNG103") || courseCode.contains("PHR")
                    || courseCode.contains("ARC") || courseCode.contains("ACT511")
                ) continue
                val section = data[5].text().trim()
                if (section.contains("OM", ignoreCase = true) || section.contains("closed", ignoreCase = true)) continue
                val timings = data[6].text().trim()
                if (timings.count { it == 'L' } > 2 || "UB0000" in timings) continue
                val labFlag = checkLab(timings)
                val courseInfo = JSONObject()
                courseInfo.put("Course", courseCode)
                courseInfo.put("Section", section)
                courseInfo.put("ClassTime", timings)
                courseInfo.put("Lab", labFlag)
                val timeCheckFlag = timeFormat(courseInfo)
                if(!courseInfo.has("ClassDay")){
                    continue
                }
                if( !timeCheckFlag){
                    continue
                }

                courseList.put(courseInfo)
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


    suspend fun executeAsyncTask(): JSONArray {
        return withContext(Dispatchers.IO) {
            val url =
                "https://usis.bracu.ac.bd/academia/admissionRequirement/getAvailableSeatStatus"
            val doc: Document = Jsoup.connect(url).get()
            val rows: Elements = doc.select("tr")
            val courses = dataFormat(rows)
            createCourseList(courses)
            //val file = File("course_info.json")
            //file.writeText(courses.toString(4))

            println("Extraction completed. Data saved to 'course_info.json'.")

            courses
        }
    }

}