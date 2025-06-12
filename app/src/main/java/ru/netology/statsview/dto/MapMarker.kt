package ru.netology.statsview.dto

data class MapMarker(
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val description: String,
    val createdAt: Long = System.currentTimeMillis()
)