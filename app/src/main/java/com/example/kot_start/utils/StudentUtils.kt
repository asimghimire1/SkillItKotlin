package com.example.kot_start.utils

import com.example.kot_start.model.Bid
import java.text.SimpleDateFormat
import java.util.*

/**
 * Extension function to convert name to URL-friendly slug
 */
fun String.toUserSlug(): String {
    return this.lowercase()
        .replace(" ", "-")
        .replace("\\s+".toRegex(), "-")
        .replace("[^a-z0-9-]".toRegex(), "")
        .replace("-+".toRegex(), "-")
        .trim('-')
}

/**
 * Validate bid amount (must be 60-100% of original)
 */
fun validateBid(bidAmount: Double, originalPrice: Double): Boolean {
    val minBid = originalPrice * 0.60
    val maxBid = originalPrice
    return bidAmount in minBid..maxBid
}

/**
 * Calculate bid savings
 */
fun calculateBidSavings(bidAmount: Double, originalPrice: Double): Double {
    return originalPrice - bidAmount
}

/**
 * Calculate bid savings percentage
 */
fun calculateBidSavingsPercent(bidAmount: Double, originalPrice: Double): Double {
    return ((originalPrice - bidAmount) / originalPrice) * 100
}

/**
 * Format currency value
 */
fun formatCurrency(amount: Double, currencySymbol: String = "NPR"): String {
    return "%s %.2f".format(currencySymbol, amount)
}

/**
 * Format timestamp to readable date
 */
fun formatDate(timestamp: Long, format: String = "MMM dd, yyyy"): String {
    return try {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.format(Date(timestamp))
    } catch (e: Exception) {
        "Invalid date"
    }
}

/**
 * Format timestamp to time only
 */
fun formatTime(timestamp: Long, format: String = "hh:mm a"): String {
    return try {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.format(Date(timestamp))
    } catch (e: Exception) {
        "Invalid time"
    }
}

/**
 * Format timestamp to date and time
 */
fun formatDateTime(timestamp: Long, format: String = "MMM dd, yyyy - hh:mm a"): String {
    return try {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.format(Date(timestamp))
    } catch (e: Exception) {
        "Invalid date/time"
    }
}

/**
 * Get human-readable time difference (e.g., "2 hours ago")
 */
fun getTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val difference = now - timestamp
    
    return when {
        difference < 60000 -> "just now"
        difference < 3600000 -> "${difference / 60000} minutes ago"
        difference < 86400000 -> "${difference / 3600000} hours ago"
        difference < 604800000 -> "${difference / 86400000} days ago"
        difference < 2592000000 -> "${difference / 604800000} weeks ago"
        else -> formatDate(timestamp)
    }
}

/**
 * Count pending notifications (new bids)
 */
fun countPendingBids(bids: List<Bid>): Int {
    return bids.count { it.isNew && it.status == "PENDING" }
}

/**
 * Get bid status color (returns color code string)
 */
fun getBidStatusColor(status: String): String {
    return when (status.uppercase()) {
        "PENDING" -> "#1E88E5" // Blue
        "COUNTERED" -> "#FFA500" // Orange
        "ACCEPTED" -> "#4CAF50" // Green
        "REJECTED" -> "#E53935" // Red
        else -> "#757575" // Grey
    }
}

/**
 * Get bid status display text with emoji
 */
fun getBidStatusDisplay(status: String): String {
    return when (status.uppercase()) {
        "PENDING" -> "⏳ Pending"
        "COUNTERED" -> "🔄 Countered"
        "ACCEPTED" -> "✅ Accepted"
        "REJECTED" -> "❌ Rejected"
        else -> status
    }
}

/**
 * Check if bid is still valid for negotiation
 */
fun isBidNegotiable(bidStatus: String): Boolean {
    return bidStatus.uppercase() in listOf("PENDING", "COUNTERED")
}

/**
 * Get transaction type icon (emoji)
 */
fun getTransactionTypeIcon(type: String): String {
    return when (type.uppercase()) {
        "TOPUP" -> "⬆️" // Top-up
        "PURCHASE" -> "🛒" // Purchase
        "REFUND" -> "⬅️" // Refund
        "CASHBACK" -> "💰" // Cashback
        "WITHDRAWAL" -> "⬇️" // Withdrawal
        else -> "💳"
    }
}

/**
 * Get transaction type color
 */
fun getTransactionTypeColor(type: String): String {
    return when (type.uppercase()) {
        "TOPUP" -> "#4CAF50" // Green - money in
        "PURCHASE" -> "#E53935" // Red - money out
        "REFUND" -> "#4CAF50" // Green - money back
        "CASHBACK" -> "#4CAF50" // Green - bonus
        "WITHDRAWAL" -> "#E53935" // Red - money out
        else -> "#757575"
    }
}

/**
 * Calculate remaining time until session start
 */
fun getTimeUntilSession(startTime: Long): String {
    val now = System.currentTimeMillis()
    val remaining = startTime - now
    
    if (remaining < 0) return "Session started"
    
    return when {
        remaining < 60000 -> "Starting soon"
        remaining < 3600000 -> "${remaining / 60000} min left"
        remaining < 86400000 -> "${remaining / 3600000} hours left"
        else -> "${remaining / 86400000} days left"
    }
}

/**
 * Format duration in minutes to readable string
 */
fun formatDuration(minutes: Int): String {
    return if (minutes >= 60) {
        val hours = minutes / 60
        val mins = minutes % 60
        if (mins > 0) "$hours h $mins min" else "$hours hours"
    } else {
        "$minutes minutes"
    }
}

/**
 * Round rating to nearest 0.5
 */
fun roundRating(rating: Double): Double {
    return (rating * 2).toInt().toDouble() / 2
}

/**
 * Format rating with stars
 */
fun formatRatingWithStars(rating: Double, maxStars: Int = 5): String {
    val rounded = roundRating(rating)
    val fullStars = rounded.toInt()
    val hasHalfStar = (rounded % 1) > 0
    
    val stars = "⭐".repeat(fullStars) + (if (hasHalfStar) "✨" else "")
    return "$rounded/5.0 $stars"
}
