package com.aaditx23.bracusocial

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


operator fun <T> List<T>.component6(): T = get(5)
operator fun <T> List<T>.component7(): T = get(6)
operator fun <T> List<T>.component8(): T = get(7)
val timeSlots = listOf(
    "08:00 AM - 09:20 AM",
    "08:00 AM - 10:50 AM", //lab
    "09:30 AM - 10:50 AM",
    "11:00 AM - 12:20 PM",
    "11:00 AM - 01:50 PM", //lab

    "12:30 PM - 01:50 PM",
    "02:00 PM - 03:20 PM",
    "02:00 PM - 04:50 PM", //lab
    "03:30 PM - 04:50 PM",
    "05:00 PM - 06:20 PM",
    "05:00 PM - 08:00 PM", //lab
    "06:30 PM - 08:00 PM",
    "Closed"
)
val days = listOf(
    "Sa",
    "Su",
    "Mo",
    "Tu",
    "We",
    "Th",
    "Fr"
)
val dayList = listOf(
    "Saturday",
    "Sunday",
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday"
)

fun listfiltersAddAll(list: List<String>, item: String): List<String>{
    val filters = list.toMutableList()
    filters.add(0, item)
    return filters
}


val classTimeList = listOf(
    "08:00 AM - 09:20 AM",
    "09:30 AM - 10:50 AM",
    "11:00 AM - 12:20 PM",
    "12:30 PM - 01:50 PM",
    "02:00 PM - 03:20 PM",
    "03:30 PM - 04:50 PM",
    "05:00 PM - 06:20 PM",
    "06:30 PM - 08:00 PM",
    "Closed"
)
val labTimeList = listOf(
    "08:00 AM - 10:50 AM",
    "11:00 AM - 01:50 PM",
    "02:00 PM - 04:50 PM",
    "05:00 PM - 08:00 PM",
    "Closed"
)

@SuppressLint("ServiceCast")
fun checkInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}