package com.maryam.smartstudyplanner.data.local.dao

// Room ke liye ek simple POJO result class — koi table nahi hai,
// sirf ek query ke grouped/aggregated result ko represent karta hai.
data class SubjectDuration(
    val subjectId: Long,
    val totalMinutes: Long
)