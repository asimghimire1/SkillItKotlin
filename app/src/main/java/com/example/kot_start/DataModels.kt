package com.example.kot_start

import java.io.Serializable

// Video/Course Models
data class Video(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val thumbnailUrl: String = "",
    val videoUrl: String = "",
    val category: String = "",
    val isPaid: Boolean = false,
    val price: Float = 0f,
    val teacherId: String = "",
    val teacherName: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val duration: Long = 0L,
    val studentCount: Int = 0,
    val status: String = "published" // published, draft, review
) : Serializable

// Session Models
data class Session(
    val id: String = "",
    val title: String = "",
    val category: String = "",
    val difficulty: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val meetingLink: String = "",
    val isPaid: Boolean = false,
    val price: Float = 0f,
    val teacherId: String = "",
    val teacherName: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val studentsRegistered: Int = 0,
    val status: String = "scheduled" // scheduled, live, completed
) : Serializable

// Bid Models
data class Bid(
    val id: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val studentProfileUrl: String = "",
    val courseTitle: String = "",
    val originalPrice: Float = 0f,
    val studentOffer: Float = 0f,
    val teacherId: String = "",
    val teacherName: String = "",
    val priority: String = "standard", // urgent, standard
    val status: String = "active", // active, pending, completed, rejected
    val createdAt: Long = System.currentTimeMillis(),
    val counterOffer: Float = 0f,
    val counterOfferBy: String = "" // teacher, student
) : Serializable

// Transaction/Earning Models
data class Transaction(
    val id: String = "",
    val type: String = "", // withdrawal, course_sale, session_sale
    val amount: Float = 0f,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "completed", // completed, processing, failed
    val description: String = "",
    val courseTitle: String = ""
) : Serializable

// User Models
data class TeacherProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val bio: String = "",
    val totalEarnings: Float = 0f,
    val totalBalance: Float = 0f,
    val createdAt: Long = System.currentTimeMillis()
) : Serializable

data class StudentProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val bio: String = "",
    val enrolledCourses: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) : Serializable
