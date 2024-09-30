package com.opsc.onesecondapp.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val notificationsEnabled: Boolean = true
)