package com.opsc.onesecondapp.models

data class Entry(
    val id: String = "",
    val userId: String = "",
    val text: String = "",
    val mediaUrl: String = "",
    val timestamp: Long = 0L
)