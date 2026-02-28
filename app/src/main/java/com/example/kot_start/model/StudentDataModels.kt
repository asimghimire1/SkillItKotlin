package com.example.kot_start.model

import java.io.Serializable
import java.util.*

// Student Statistics
data class StudentStats(
    val totalCourses: Int = 0,
    val completedCourses: Int = 0,
    val totalHours: Double = 0.0,
    val credits: Double = 0.0,
    val level: String = "Beginner",
    val badges: List<String> = emptyList()
) : Serializable

// Teacher Profile
data class Teacher(
    val teacherId: String = "",
    val name: String = "",
    val email: String = "",
    val expertise: List<String> = emptyList(),
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val profileImage: String = "",
    val hourlyRate: Double = 0.0,
    val bio: String = "",
    val isVerified: Boolean = false
) : Serializable {
    fun toUserSlug(): String = name.lowercase().replace(" ", "-")
}

// Content (Courses/Resources)
data class Content(
    val contentId: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val thumbnail: String = "",
    val price: Double = 0.0,
    val duration: Int = 0, // minutes
    val teacherId: String = "",
    val teacherName: String = "",
    val rating: Double = 0.0,
    val level: String = "Beginner", // Beginner, Intermediate, Advanced
    val videoUrl: String = "",
    val syllabus: List<SyllabusItem> = emptyList(),
    val isFree: Boolean = true,
    val isLocked: Boolean = false
) : Serializable

// Syllabus Item for Video Player
data class SyllabusItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val duration: Int = 0, // minutes
    val videoUrl: String = "",
    val isCompleted: Boolean = false,
    val resourceLinks: List<String> = emptyList()
) : Serializable

// Live Session/Class
data class Session(
    val sessionId: String = "",
    val title: String = "",
    val description: String = "",
    val teacherId: String = "",
    val teacherName: String = "",
    val teacherImage: String = "",
    val teacherRating: Double = 0.0,
    val category: String = "",
    val startTime: Long = 0, // milliseconds
    val duration: Int = 0, // minutes
    val maxCapacity: Int = 20,
    val currentEnrollment: Int = 0,
    val price: Double = 0.0,
    val isLive: Boolean = false,
    val sessionLink: String = "",
    val requirements: List<String> = emptyList(),
    val isBidAllowed: Boolean = true
) : Serializable {
    fun getAvailableSeats(): Int = maxCapacity - currentEnrollment
    fun getCapacityPercent(): Float = (currentEnrollment.toFloat() / maxCapacity) * 100
}

// Student Enrollment
data class Enrollment(
    val enrollmentId: String = "",
    val studentId: String = "",
    val contentId: String = "",
    val contentTitle: String = "",
    val teacherName: String = "",
    val enrolledDate: Long = 0,
    val progress: Float = 0f, // 0-100%
    val isCompleted: Boolean = false,
    val certificateUrl: String = ""
) : Serializable

// Bid Management
data class Bid(
    val bidId: String = "",
    val studentId: String = "",
    val contentId: String = "",
    val contentTitle: String = "",
    val teacherId: String = "",
    val teacherName: String = "",
    val originalPrice: Double = 0.0,
    val bidAmount: Double = 0.0,
    val status: String = "PENDING", // PENDING, COUNTERED, ACCEPTED, REJECTED
    val counterOfferAmount: Double = 0.0,
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
    val negotiationDate: Long = 0,
    val isNew: Boolean = true
) : Serializable {
    fun getSavings(): Double = originalPrice - bidAmount
    fun getSavingsPercent(): Double = ((originalPrice - bidAmount) / originalPrice) * 100
}

// Bid Request (for submitting new bids)
data class BidRequest(
    val contentId: String = "",
    val bidAmount: Double = 0.0,
    val negotiationMessage: String = ""
) : Serializable

// Wallet/Payment
data class WalletTransaction(
    val transactionId: String = "",
    val studentId: String = "",
    val type: String = "", // TOPUP, PURCHASE, REFUND, CASHBACK, WITHDRAWAL
    val amount: Double = 0.0,
    val description: String = "",
    val timestamp: Long = 0,
    val status: String = "SUCCESS" // SUCCESS, PENDING, FAILED
) : Serializable

// Dashboard Data Container
data class DashboardData(
    val stats: StudentStats = StudentStats(),
    val sessions: List<Session> = emptyList(),
    val content: List<Content> = emptyList(),
    val teachers: List<Teacher> = emptyList(),
    val enrollments: List<Enrollment> = emptyList(),
    val unlockedContent: List<Content> = emptyList(),
    val bids: List<Bid> = emptyList(),
    val transactions: List<WalletTransaction> = emptyList()
) : Serializable
