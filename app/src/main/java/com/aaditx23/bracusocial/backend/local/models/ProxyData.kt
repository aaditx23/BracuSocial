package com.aaditx23.bracusocial.backend.local.models

import android.graphics.Bitmap
import com.aaditx23.bracusocial.MainActivity
import com.aaditx23.bracusocial.components.bitmapToString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

//var emptiProfile = MainActivity.EmptyImage.emptyProfileImage
//val emptyProfileString = runBlocking(Dispatchers.IO) {
//    async { bitmapToString(emptiProfile) }.await()
//}

val courseArray = arrayOf(
    JSONObject(
        "{\n" +
                "    \"ClassDay\": [\n" +
                "      \"Su\",\n" +
                "      \"Tu\"\n" +
                "    ],\n" +
                "    \"ClassTime\": \"02:00 PM - 03:20 PM\",\n" +
                "    \"Course\": \"ACT201\",\n" +
                "    \"Lab\": false,\n" +
                "    \"Section\": \"01\",\n" +
                "    \"ClassRoom\": \"07A-01C\",\n" +
                "    \"Faculty\": \"TBA\"\n" + // Added Faculty
                "  }"
    ),
    JSONObject(
        "{\n" +
                "    \"LabRoom\": \"09B-09L\",\n" +
                "    \"LabTime\": \"02:00 PM - 04:50 PM\",\n" +
                "    \"ClassDay\": [\n" +
                "      \"Mo\",\n" +
                "      \"We\"\n" +
                "    ],\n" +
                "    \"ClassTime\": \"11:00 AM - 12:20 PM\",\n" +
                "    \"LabDay\": [\"Tu\"],\n" +
                "    \"Course\": \"CSE111\",\n" +
                "    \"Lab\": true,\n" +
                "    \"Section\": \"01\",\n" +
                "    \"ClassRoom\": \"09A-04C\",\n" +
                "    \"Faculty\": \"NTR\"\n" + // Added Faculty
                "  }"
    ),
    JSONObject(
        "{\n" +
                "    \"LabRoom\": \"10G-32L\",\n" +
                "    \"LabTime\": \"08:00 AM - 10:50 AM\",\n" +
                "    \"ClassDay\": [\n" +
                "      \"Mo\",\n" +
                "      \"We\"\n" +
                "    ],\n" +
                "    \"ClassTime\": \"09:30 AM - 10:50 AM\",\n" +
                "    \"LabDay\": [\"Tu\"],\n" +
                "    \"Course\": \"CSE421\",\n" +
                "    \"Lab\": true,\n" +
                "    \"Section\": \"05\",\n" +
                "    \"ClassRoom\": \"09H-33C\",\n" +
                "    \"Faculty\": \"ADR\"\n" + // Added Faculty
                "  }"
    ),
    JSONObject(
        "{\n" +
                "    \"LabRoom\": \"10E-25L\",\n" +
                "    \"LabTime\": \"02:00 PM - 04:50 PM\",\n" +
                "    \"ClassDay\": [\n" +
                "      \"Mo\",\n" +
                "      \"We\"\n" +
                "    ],\n" +
                "    \"ClassTime\": \"03:30 PM - 04:50 PM\",\n" +
                "    \"LabDay\": [\"Sa\"],\n" +
                "    \"Course\": \"CSE428\",\n" +
                "    \"Lab\": true,\n" +
                "    \"Section\": \"02\",\n" +
                "    \"ClassRoom\": \"10A-08C\",\n" +
                "    \"Faculty\": \"FGZ\"\n" + // Added Faculty
                "  }"
    ),
    JSONObject(
        "{\n" +
                "    \"LabRoom\": \"12F-31L\",\n" +
                "    \"LabTime\": \"08:00 AM - 10:50 AM\",\n" +
                "    \"ClassDay\": [\n" +
                "      \"Su\",\n" +
                "      \"Tu\"\n" +
                "    ],\n" +
                "    \"ClassTime\": \"12:30 PM - 01:50 PM\",\n" +
                "    \"LabDay\": [\"We\"],\n" +
                "    \"Course\": \"CSE489\",\n" +
                "    \"Lab\": true,\n" +
                "    \"Section\": \"01\",\n" +
                "    \"ClassRoom\": \"10A-06C\",\n" +
                "    \"Faculty\": \"TAW\"\n" + // Added Faculty
                "  }"
    ),
    JSONObject(
        "{\n" +
                "    \"ClassDay\": [\n" +
                "      \"Mo\",\n" +
                "      \"We\"\n" +
                "    ],\n" +
                "    \"ClassTime\": \"02:00 PM - 03:20 PM\",\n" +
                "    \"Course\": \"SOC101\",\n" +
                "    \"Lab\": false,\n" +
                "    \"Section\": \"09\",\n" +
                "    \"ClassRoom\": \"08B-10C\",\n" +
                "    \"Faculty\": \"SEJ\"\n" + // Added Faculty
                "  }"
    ),
    JSONObject(
        "{\n" +
                "    \"LabRoom\": \"09F-25L\",\n" +
                "    \"LabTime\": \"02:00 PM - 04:50 PM\",\n" +
                "    \"ClassDay\": [\n" +
                "      \"Su\",\n" +
                "      \"Tu\"\n" +
                "    ],\n" +
                "    \"ClassTime\": \"09:30 AM - 10:50 AM\",\n" +
                "    \"LabDay\": [\"Mo\"],\n" +
                "    \"Course\": \"CSE221\",\n" +
                "    \"Lab\": true,\n" +
                "    \"Section\": \"02\",\n" +
                "    \"ClassRoom\": \"09A-01C\",\n" +
                "    \"Faculty\": \"TBA\"\n" + // Added Faculty
                "  }"
    )
)


val demoProfiles = listOf<List<String>>(
//    listOf(
//        "23341077", // Profile 1
//        "",
//        "Demo 1",
//        "CSE421 05, CSE489 01, SOC101 09, CSE428 02",
//        "21201617", // Added Friends: Demo 2
//        "21201616",  // Friend Requests: Demo 3 sent a request to Demo 1,
//        emptyProfileString,
//        "amir.ul.islam@g.bracu.ac.bd"
//    ),
//    listOf(
//        "21201617", // Profile 2
//        "123",
//        "Demo 2",
//        "CSE221 02,SOC101 01,CSE489 01",
//        "21201618,21201616", // Added Friends: Demo 1 and Demo 3
//        "",            // No pending friend requests
//        emptyProfileString,
//        "amir1.ul.islam@g.bracu.ac.bd"
//
//    ),
//    listOf(
//        "21201616", // Profile 3
//        "123",
//        "Demo 3",
//        "ACT201 02,ANT101 02,BIO101 02",
//        "21201617", // Added Friends: Demo 2
//        "21201618",  // Friend Requests: Demo 1 sent a request to Demo 3
//        emptyProfileString,
//        "amir2.ul.islam@g.bracu.ac.bd"
//    ),
//    listOf(
//        "21201615", // Profile 4
//        "123",
//        "Demo 4",
//        "BUS102 01,CHE110 01,CSE110 19",
//        "",         // No friends yet
//        "21201614",  // Friend Requests: Demo 5 sent a request to Demo 4
//        emptyProfileString,
//        "amir3.ul.islam@g.bracu.ac.bd"
//    ),
//    listOf(
//        "21201614", // Profile 5
//        "123",
//        "Demo 5",
//        "CSE220 25,CSE230 20,CSE250 21",
//        "",         // No friends yet
//        "",          // No pending friend requests
//        emptyProfileString,
//        "amir4.ul.islam@g.bracu.ac.bd"
//    )
)

const val emptyProfileString = "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\n" +
        "sDCFAgAAWJhCAQAALEyhAAAAFqZQAAAAC1MoAACAhSkUAADAwhQKAABgYQoFAACwMIUCAABYmEIB\n" +
        "AAAsTKEAAAAWplAAAAALUygAAICFKRQAAMDCFAoAAGBhCgUAALAwhQIAAFiYQgEAACxMoQAAABam\n" +
        "UAAAAAtTKAAAgIUpFAAAwMIUCgAAYGHHjQ7ArLy/OmN0CABg9i4aHQAAAAAAAAAAAAAAAAAAAAAA\n" +
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABvr/Aamj2GOS8/SIAAAA\n" +
        "AElFTkSuQmCC\n"