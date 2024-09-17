package com.aaditx23.bracusocial.backend.remote

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

// Function to extract slot details
fun extractSlotDetails(slot: String): Map<String, String> {
    val day = slot.substring(0, 2)
    val time = slot.substring(3, 20).trim()
    val separator = slot[20]
    if (separator != '-') {
        throw IllegalArgumentException("Expected '-' at index 20 but found '$separator'")
    }
    val room = slot.substring(21).trim()

    return mapOf("day" to day, "time" to time, "room" to room)
}

// Function to split the schedule into slots
fun splitScheduleIntoSlots(schedule: String): List<Map<String, String>> {
    val rawSlots = schedule.split(")").map { it.trim() }.filter { it.isNotEmpty() }
    return rawSlots.map { extractSlotDetails(it) }
}

// Function to merge consecutive time slots
fun mergeConsecutiveSlots(slots: List<Map<String, String>>): List<Map<String, String>> {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
    val mergedSlots = mutableListOf<Map<String, String>>()

    var currentSlot: MutableMap<String, String>? = null

    for (slot in slots) {
        if (currentSlot == null) {
            currentSlot = slot.toMutableMap()
        } else {
            val currentDay = currentSlot["day"]
            val currentRoom = currentSlot["room"]
            val previousEndTime = currentSlot["time"]?.split("-")?.get(1)?.trim()
            val currentStartTime = slot["time"]?.split("-")?.get(0)?.trim()

            if (currentDay == slot["day"] && currentRoom == slot["room"] && isConsecutiveTime(previousEndTime, currentStartTime, timeFormat)) {
                currentSlot["time"] = currentSlot["time"]?.split("-")?.get(0) + "-" + slot["time"]?.split("-")?.get(1)
            } else {
                mergedSlots.add(currentSlot.toMap())
                currentSlot = slot.toMutableMap()
            }
        }
    }

    currentSlot?.let { mergedSlots.add(it) }

    return mergedSlots
}

// Function to check if two time slots are consecutive
fun isConsecutiveTime(endTime: String?, startTime: String?, timeFormat: SimpleDateFormat): Boolean {
    if (endTime == null || startTime == null) return false

    val endTimeDate = timeFormat.parse(endTime)
    val startTimeDate = timeFormat.parse(startTime)

    val difference = startTimeDate.time - endTimeDate.time
    return difference.toInt() == 10 * 60 * 1000 // 10 minutes in milliseconds
}

// Function to calculate time difference in minutes
fun calculateTimeDifference(startTime: String, endTime: String, timeFormat: SimpleDateFormat): Long {
    val startTimeDate = timeFormat.parse(startTime)
    val endTimeDate = timeFormat.parse(endTime)
    return TimeUnit.MILLISECONDS.toMinutes(endTimeDate.time - startTimeDate.time)
}

// Function to classify slots as class or lab
fun classifySlots(slots: List<Map<String, String>>): List<Map<String, String>> {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
    val classifiedSlots = mutableListOf<Map<String, String>>()

    for (slot in slots) {
        val timeRange = slot["time"]?.split("-") ?: continue
        if (timeRange.size < 2) continue

        val startTime = timeRange[0].trim()
        val endTime = timeRange[1].trim()
        val room = slot["room"] ?: ""

        val timeDifference = calculateTimeDifference(startTime, endTime, timeFormat)
        val slotType = when {
            timeDifference == 170.toLong() && room.contains("L") -> "Lab"
            timeDifference == 80.toLong() -> "Class"
            else -> "Unknown"
        }

        classifiedSlots.add(slot.toMutableMap().apply { put("type", slotType) })
    }

    return classifiedSlots
}

fun processTime(rowEntry: String) :  List<Map<String, String>>{
    val slots = splitScheduleIntoSlots(rowEntry)
    val mergedSlots = mergeConsecutiveSlots(slots)
    return classifySlots(mergedSlots)
}