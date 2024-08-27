package com.aaditx23.bracusocial.backend.local.models

import org.json.JSONObject

val courseArray =
    arrayOf(
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
                    "    \"ClassRoom\": \"07A-01C\"\n" +
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
                    "    \"ClassRoom\": \"09A-04C\"\n" +
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
                    "    \"ClassRoom\": \"09H-33C\"\n" +
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
                    "    \"ClassRoom\": \"10A-08C\"\n" +
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
                    "    \"ClassRoom\": \"10A-06C\"\n" +
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
                    "    \"ClassRoom\": \"08B-10C\"\n" +
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
                    "    \"ClassRoom\": \"09A-01C\"\n" +
                    "  }"
        ),
    )

val demoProfiles = listOf(
    listOf(
        "21201618", // Profile 1
        "123",
        "Demo 1",
        "CSE221 02,SOC101 02,CSE489 01",
        "21201617", // Added Friends: Demo 2
        "21201616"  // Friend Requests: Demo 3 sent a request to Demo 1
    ),
    listOf(
        "21201617", // Profile 2
        "123",
        "Demo 2",
        "CSE221 02,SOC101 02,CSE489 01",
        "21201618,21201616", // Added Friends: Demo 1 and Demo 3
        ""            // No pending friend requests
    ),
    listOf(
        "21201616", // Profile 3
        "123",
        "Demo 3",
        "ACT201 02,ANT101 02,BIO101 02",
        "21201617", // Added Friends: Demo 2
        "21201618"  // Friend Requests: Demo 1 sent a request to Demo 3
    ),
    listOf(
        "21201615", // Profile 4
        "123",
        "Demo 4",
        "BUS102 01,CHE110 01,CSE110 19",
        "",         // No friends yet
        "21201614"  // Friend Requests: Demo 5 sent a request to Demo 4
    ),
    listOf(
        "21201614", // Profile 5
        "123",
        "Demo 5",
        "CSE220 25,CSE230 20,CSE250 21",
        "",         // No friends yet
        ""          // No pending friend requests
    )
)
