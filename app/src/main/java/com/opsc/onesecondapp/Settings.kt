package com.opsc.onesecondapp.models

data class Settings(
    val notificationsEnabled: Boolean = true,
    val language: String = "en",
    val theme: String = "light"
)

