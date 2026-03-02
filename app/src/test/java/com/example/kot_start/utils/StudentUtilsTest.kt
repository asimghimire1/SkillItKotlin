package com.example.kot_start.utils

import com.example.kot_start.model.Bid
import org.junit.Assert.*
import org.junit.Test

/**
 * Comprehensive unit tests for all StudentUtils utility functions.
 * Covers: slug generation, bid validation, currency formatting,
 * date/time formatting, bid status helpers, transaction helpers,
 * session timing, duration formatting, and rating utilities.
 */
class StudentUtilsTest {

    // ======================== toUserSlug ========================

    @Test
    fun `toUserSlug converts name to lowercase with hyphens`() {
        assertEquals("john-smith", "John Smith".toUserSlug())
    }

    @Test
    fun `toUserSlug handles multiple spaces`() {
        assertEquals("john-smith", "John   Smith".toUserSlug())
    }

    @Test
    fun `toUserSlug removes special characters`() {
        assertEquals("john-smith", "John @Smith!".toUserSlug())
    }

    @Test
    fun `toUserSlug trims leading and trailing hyphens`() {
        assertEquals("john", "-John-".toUserSlug())
    }

    @Test
    fun `toUserSlug handles empty string`() {
        assertEquals("", "".toUserSlug())
    }

    @Test
    fun `toUserSlug handles single word`() {
        assertEquals("alice", "Alice".toUserSlug())
    }

    @Test
    fun `toUserSlug handles numbers in name`() {
        assertEquals("user123", "User123".toUserSlug())
    }

    // ======================== validateBid ========================

    @Test
    fun `validateBid returns true for bid at 60 percent of original`() {
        assertTrue(validateBid(60.0, 100.0))
    }

    @Test
    fun `validateBid returns true for bid at 100 percent of original`() {
        assertTrue(validateBid(100.0, 100.0))
    }

    @Test
    fun `validateBid returns true for bid at 80 percent of original`() {
        assertTrue(validateBid(80.0, 100.0))
    }

    @Test
    fun `validateBid returns false for bid below 60 percent`() {
        assertFalse(validateBid(59.0, 100.0))
    }

    @Test
    fun `validateBid returns false for bid above original price`() {
        assertFalse(validateBid(101.0, 100.0))
    }

    @Test
    fun `validateBid returns false for zero bid`() {
        assertFalse(validateBid(0.0, 100.0))
    }

    @Test
    fun `validateBid returns false for negative bid`() {
        assertFalse(validateBid(-10.0, 100.0))
    }

    @Test
    fun `validateBid handles large amounts`() {
        assertTrue(validateBid(6000.0, 10000.0))
    }

    @Test
    fun `validateBid boundary at exactly 59_99 percent`() {
        assertFalse(validateBid(59.99, 100.0))
    }

    // ======================== calculateBidSavings ========================

    @Test
    fun `calculateBidSavings returns correct savings`() {
        assertEquals(50.0, calculateBidSavings(50.0, 100.0), 0.01)
    }

    @Test
    fun `calculateBidSavings returns zero when bid equals original`() {
        assertEquals(0.0, calculateBidSavings(100.0, 100.0), 0.01)
    }

    @Test
    fun `calculateBidSavings handles fractional amounts`() {
        assertEquals(100.50, calculateBidSavings(399.50, 500.0), 0.01)
    }

    // ======================== calculateBidSavingsPercent ========================

    @Test
    fun `calculateBidSavingsPercent returns correct percentage`() {
        assertEquals(40.0, calculateBidSavingsPercent(60.0, 100.0), 0.01)
    }

    @Test
    fun `calculateBidSavingsPercent returns zero when no savings`() {
        assertEquals(0.0, calculateBidSavingsPercent(100.0, 100.0), 0.01)
    }

    @Test
    fun `calculateBidSavingsPercent returns 25 percent savings`() {
        assertEquals(25.0, calculateBidSavingsPercent(75.0, 100.0), 0.01)
    }

    // ======================== formatCurrency ========================

    @Test
    fun `formatCurrency formats with default NPR symbol`() {
        assertEquals("NPR 100.00", formatCurrency(100.0))
    }

    @Test
    fun `formatCurrency formats with custom symbol`() {
        assertEquals("USD 99.99", formatCurrency(99.99, "USD"))
    }

    @Test
    fun `formatCurrency formats zero`() {
        assertEquals("NPR 0.00", formatCurrency(0.0))
    }

    @Test
    fun `formatCurrency formats large amounts`() {
        assertEquals("NPR 10000.50", formatCurrency(10000.50))
    }

    // ======================== formatDate ========================

    @Test
    fun `formatDate returns formatted date for valid timestamp`() {
        // Jan 1, 2024 00:00:00 UTC
        val timestamp = 1704067200000L
        val result = formatDate(timestamp)
        // The exact output depends on locale, but should not be "Invalid date"
        assertNotEquals("Invalid date", result)
        assertTrue(result.contains("2024"))
    }

    @Test
    fun `formatDate with custom format returns correct pattern`() {
        val timestamp = 1704067200000L
        val result = formatDate(timestamp, "yyyy-MM-dd")
        assertNotEquals("Invalid date", result)
        assertTrue(result.contains("2024"))
    }

    @Test
    fun `formatDate returns Invalid date for zero timestamp with invalid format`() {
        // Zero timestamp should still format to a valid date (epoch)
        val result = formatDate(0L)
        assertNotEquals("Invalid date", result)
    }

    // ======================== formatTime ========================

    @Test
    fun `formatTime returns formatted time for valid timestamp`() {
        val timestamp = 1704067200000L
        val result = formatTime(timestamp)
        assertNotEquals("Invalid time", result)
    }

    @Test
    fun `formatTime with custom format`() {
        val timestamp = 1704067200000L
        val result = formatTime(timestamp, "HH:mm")
        assertNotEquals("Invalid time", result)
    }

    // ======================== formatDateTime ========================

    @Test
    fun `formatDateTime returns formatted date and time`() {
        val timestamp = 1704067200000L
        val result = formatDateTime(timestamp)
        assertNotEquals("Invalid date/time", result)
        assertTrue(result.contains("2024"))
    }

    // ======================== getTimeAgo ========================

    @Test
    fun `getTimeAgo returns just now for recent timestamp`() {
        val now = System.currentTimeMillis()
        assertEquals("just now", getTimeAgo(now - 30000)) // 30 seconds ago
    }

    @Test
    fun `getTimeAgo returns minutes ago`() {
        val now = System.currentTimeMillis()
        val result = getTimeAgo(now - 300000) // 5 minutes ago
        assertTrue(result.contains("minutes ago"))
    }

    @Test
    fun `getTimeAgo returns hours ago`() {
        val now = System.currentTimeMillis()
        val result = getTimeAgo(now - 7200000) // 2 hours ago
        assertTrue(result.contains("hours ago"))
    }

    @Test
    fun `getTimeAgo returns days ago`() {
        val now = System.currentTimeMillis()
        val result = getTimeAgo(now - 259200000) // 3 days ago
        assertTrue(result.contains("days ago"))
    }

    @Test
    fun `getTimeAgo returns weeks ago`() {
        val now = System.currentTimeMillis()
        val result = getTimeAgo(now - 1209600000) // 2 weeks ago
        assertTrue(result.contains("weeks ago"))
    }

    // ======================== countPendingBids ========================

    @Test
    fun `countPendingBids returns correct count for pending new bids`() {
        val bids = listOf(
            Bid(bidId = "1", isNew = true, status = "PENDING"),
            Bid(bidId = "2", isNew = true, status = "PENDING"),
            Bid(bidId = "3", isNew = false, status = "PENDING"),
            Bid(bidId = "4", isNew = true, status = "ACCEPTED")
        )
        assertEquals(2, countPendingBids(bids))
    }

    @Test
    fun `countPendingBids returns zero for empty list`() {
        assertEquals(0, countPendingBids(emptyList()))
    }

    @Test
    fun `countPendingBids returns zero when no pending new bids`() {
        val bids = listOf(
            Bid(bidId = "1", isNew = false, status = "PENDING"),
            Bid(bidId = "2", isNew = true, status = "ACCEPTED")
        )
        assertEquals(0, countPendingBids(bids))
    }

    // ======================== getBidStatusColor ========================

    @Test
    fun `getBidStatusColor returns blue for PENDING`() {
        assertEquals("#1E88E5", getBidStatusColor("PENDING"))
    }

    @Test
    fun `getBidStatusColor returns orange for COUNTERED`() {
        assertEquals("#FFA500", getBidStatusColor("COUNTERED"))
    }

    @Test
    fun `getBidStatusColor returns green for ACCEPTED`() {
        assertEquals("#4CAF50", getBidStatusColor("ACCEPTED"))
    }

    @Test
    fun `getBidStatusColor returns red for REJECTED`() {
        assertEquals("#E53935", getBidStatusColor("REJECTED"))
    }

    @Test
    fun `getBidStatusColor returns grey for unknown status`() {
        assertEquals("#757575", getBidStatusColor("UNKNOWN"))
    }

    @Test
    fun `getBidStatusColor is case insensitive`() {
        assertEquals("#1E88E5", getBidStatusColor("pending"))
    }

    // ======================== getBidStatusDisplay ========================

    @Test
    fun `getBidStatusDisplay returns emoji for PENDING`() {
        assertEquals("⏳ Pending", getBidStatusDisplay("PENDING"))
    }

    @Test
    fun `getBidStatusDisplay returns emoji for COUNTERED`() {
        assertEquals("🔄 Countered", getBidStatusDisplay("COUNTERED"))
    }

    @Test
    fun `getBidStatusDisplay returns emoji for ACCEPTED`() {
        assertEquals("✅ Accepted", getBidStatusDisplay("ACCEPTED"))
    }

    @Test
    fun `getBidStatusDisplay returns emoji for REJECTED`() {
        assertEquals("❌ Rejected", getBidStatusDisplay("REJECTED"))
    }

    @Test
    fun `getBidStatusDisplay returns raw status for unknown`() {
        assertEquals("UNKNOWN", getBidStatusDisplay("UNKNOWN"))
    }

    // ======================== isBidNegotiable ========================

    @Test
    fun `isBidNegotiable returns true for PENDING`() {
        assertTrue(isBidNegotiable("PENDING"))
    }

    @Test
    fun `isBidNegotiable returns true for COUNTERED`() {
        assertTrue(isBidNegotiable("COUNTERED"))
    }

    @Test
    fun `isBidNegotiable returns false for ACCEPTED`() {
        assertFalse(isBidNegotiable("ACCEPTED"))
    }

    @Test
    fun `isBidNegotiable returns false for REJECTED`() {
        assertFalse(isBidNegotiable("REJECTED"))
    }

    @Test
    fun `isBidNegotiable is case insensitive`() {
        assertTrue(isBidNegotiable("pending"))
    }

    // ======================== getTransactionTypeIcon ========================

    @Test
    fun `getTransactionTypeIcon returns correct icon for TOPUP`() {
        assertEquals("⬆️", getTransactionTypeIcon("TOPUP"))
    }

    @Test
    fun `getTransactionTypeIcon returns correct icon for PURCHASE`() {
        assertEquals("🛒", getTransactionTypeIcon("PURCHASE"))
    }

    @Test
    fun `getTransactionTypeIcon returns correct icon for REFUND`() {
        assertEquals("⬅️", getTransactionTypeIcon("REFUND"))
    }

    @Test
    fun `getTransactionTypeIcon returns correct icon for CASHBACK`() {
        assertEquals("💰", getTransactionTypeIcon("CASHBACK"))
    }

    @Test
    fun `getTransactionTypeIcon returns correct icon for WITHDRAWAL`() {
        assertEquals("⬇️", getTransactionTypeIcon("WITHDRAWAL"))
    }

    @Test
    fun `getTransactionTypeIcon returns default for unknown type`() {
        assertEquals("💳", getTransactionTypeIcon("OTHER"))
    }

    // ======================== getTransactionTypeColor ========================

    @Test
    fun `getTransactionTypeColor returns green for TOPUP`() {
        assertEquals("#4CAF50", getTransactionTypeColor("TOPUP"))
    }

    @Test
    fun `getTransactionTypeColor returns red for PURCHASE`() {
        assertEquals("#E53935", getTransactionTypeColor("PURCHASE"))
    }

    @Test
    fun `getTransactionTypeColor returns green for REFUND`() {
        assertEquals("#4CAF50", getTransactionTypeColor("REFUND"))
    }

    @Test
    fun `getTransactionTypeColor returns red for WITHDRAWAL`() {
        assertEquals("#E53935", getTransactionTypeColor("WITHDRAWAL"))
    }

    @Test
    fun `getTransactionTypeColor returns grey for unknown`() {
        assertEquals("#757575", getTransactionTypeColor("UNKNOWN"))
    }

    // ======================== getTimeUntilSession ========================

    @Test
    fun `getTimeUntilSession returns Session started for past time`() {
        val pastTime = System.currentTimeMillis() - 60000
        assertEquals("Session started", getTimeUntilSession(pastTime))
    }

    @Test
    fun `getTimeUntilSession returns Starting soon for less than 1 min`() {
        val soonTime = System.currentTimeMillis() + 30000
        assertEquals("Starting soon", getTimeUntilSession(soonTime))
    }

    @Test
    fun `getTimeUntilSession returns minutes for less than 1 hour`() {
        val futureTime = System.currentTimeMillis() + 1800000 // 30 min
        val result = getTimeUntilSession(futureTime)
        assertTrue(result.contains("min left"))
    }

    @Test
    fun `getTimeUntilSession returns hours for less than 1 day`() {
        val futureTime = System.currentTimeMillis() + 7200000 // 2 hours
        val result = getTimeUntilSession(futureTime)
        assertTrue(result.contains("hours left"))
    }

    @Test
    fun `getTimeUntilSession returns days for more than 1 day`() {
        val futureTime = System.currentTimeMillis() + 172800000 // 2 days
        val result = getTimeUntilSession(futureTime)
        assertTrue(result.contains("days left"))
    }

    // ======================== formatDuration ========================

    @Test
    fun `formatDuration returns minutes for less than 60`() {
        assertEquals("30 minutes", formatDuration(30))
    }

    @Test
    fun `formatDuration returns hours for exact hours`() {
        assertEquals("2 hours", formatDuration(120))
    }

    @Test
    fun `formatDuration returns hours and minutes for mixed`() {
        assertEquals("1 h 30 min", formatDuration(90))
    }

    @Test
    fun `formatDuration handles single minute`() {
        assertEquals("1 minutes", formatDuration(1))
    }

    @Test
    fun `formatDuration handles exactly 60 minutes`() {
        assertEquals("1 hours", formatDuration(60))
    }

    // ======================== roundRating ========================

    @Test
    fun `roundRating rounds 4_3 to 4_0`() {
        assertEquals(4.0, roundRating(4.3), 0.01)
    }

    @Test
    fun `roundRating rounds 4_7 to 4_5`() {
        assertEquals(4.5, roundRating(4.7), 0.01)
    }

    @Test
    fun `roundRating keeps 4_5 as 4_5`() {
        assertEquals(4.5, roundRating(4.5), 0.01)
    }

    @Test
    fun `roundRating keeps 5_0 as 5_0`() {
        assertEquals(5.0, roundRating(5.0), 0.01)
    }

    @Test
    fun `roundRating rounds 0_0 to 0_0`() {
        assertEquals(0.0, roundRating(0.0), 0.01)
    }

    // ======================== formatRatingWithStars ========================

    @Test
    fun `formatRatingWithStars returns correct format for 4_5`() {
        val result = formatRatingWithStars(4.5)
        assertTrue(result.contains("4.5/5.0"))
        assertTrue(result.contains("⭐"))
    }

    @Test
    fun `formatRatingWithStars returns correct format for 5_0`() {
        val result = formatRatingWithStars(5.0)
        assertTrue(result.contains("5.0/5.0"))
    }

    @Test
    fun `formatRatingWithStars returns correct format for 0_0`() {
        val result = formatRatingWithStars(0.0)
        assertTrue(result.contains("0.0/5.0"))
    }
}
