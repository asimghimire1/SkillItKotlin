package com.example.kot_start.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Comprehensive unit tests for Student data models.
 * Covers: StudentStats, Teacher, Content, Session (with computed properties),
 * Enrollment, Bid (with computed properties), BidRequest, WalletTransaction, DashboardData.
 */
class StudentDataModelsTest {

    // ======================== StudentStats ========================

    @Test
    fun `StudentStats default values are correct`() {
        val stats = StudentStats()
        assertEquals(0, stats.totalCourses)
        assertEquals(0, stats.completedCourses)
        assertEquals(0.0, stats.totalHours, 0.01)
        assertEquals(0.0, stats.credits, 0.01)
        assertEquals("Beginner", stats.level)
        assertTrue(stats.badges.isEmpty())
    }

    @Test
    fun `StudentStats with custom values`() {
        val stats = StudentStats(
            totalCourses = 10,
            completedCourses = 5,
            totalHours = 120.5,
            credits = 5000.0,
            level = "Advanced",
            badges = listOf("Star", "Badge")
        )
        assertEquals(10, stats.totalCourses)
        assertEquals(5, stats.completedCourses)
        assertEquals(5000.0, stats.credits, 0.01)
        assertEquals(2, stats.badges.size)
    }

    @Test
    fun `StudentStats copy updates credits correctly`() {
        val stats = StudentStats(credits = 1000.0)
        val updated = stats.copy(credits = 500.0)
        assertEquals(1000.0, stats.credits, 0.01)
        assertEquals(500.0, updated.credits, 0.01)
    }

    // ======================== Teacher ========================

    @Test
    fun `Teacher default values are correct`() {
        val teacher = Teacher()
        assertEquals("", teacher.teacherId)
        assertEquals("", teacher.name)
        assertEquals(0.0, teacher.rating, 0.01)
        assertFalse(teacher.isVerified)
        assertTrue(teacher.expertise.isEmpty())
    }

    @Test
    fun `Teacher toUserSlug generates correct slug`() {
        val teacher = Teacher(name = "John Smith")
        assertEquals("john-smith", teacher.toUserSlug())
    }

    @Test
    fun `Teacher toUserSlug handles single name`() {
        val teacher = Teacher(name = "Alice")
        assertEquals("alice", teacher.toUserSlug())
    }

    @Test
    fun `Teacher with expertise list`() {
        val teacher = Teacher(
            expertise = listOf("Kotlin", "Android", "Firebase")
        )
        assertEquals(3, teacher.expertise.size)
        assertTrue(teacher.expertise.contains("Kotlin"))
    }

    // ======================== Content ========================

    @Test
    fun `Content default values are correct`() {
        val content = Content()
        assertEquals("", content.contentId)
        assertEquals(0.0, content.price, 0.01)
        assertEquals(0, content.duration)
        assertEquals("Beginner", content.level)
        assertTrue(content.isFree)
        assertFalse(content.isLocked)
    }

    @Test
    fun `Content with all fields`() {
        val content = Content(
            contentId = "c1",
            title = "Kotlin Basics",
            price = 299.0,
            level = "Advanced",
            isFree = false,
            isLocked = true
        )
        assertEquals("c1", content.contentId)
        assertEquals(299.0, content.price, 0.01)
        assertEquals("Advanced", content.level)
        assertFalse(content.isFree)
        assertTrue(content.isLocked)
    }

    @Test
    fun `Content syllabus is accessible`() {
        val items = listOf(
            SyllabusItem(title = "Intro", duration = 10),
            SyllabusItem(title = "Variables", duration = 15)
        )
        val content = Content(syllabus = items)
        assertEquals(2, content.syllabus.size)
        assertEquals("Intro", content.syllabus[0].title)
    }

    // ======================== SyllabusItem ========================

    @Test
    fun `SyllabusItem has auto-generated id`() {
        val item = SyllabusItem(title = "Intro")
        assertTrue(item.id.isNotEmpty())
    }

    @Test
    fun `SyllabusItem default is not completed`() {
        val item = SyllabusItem()
        assertFalse(item.isCompleted)
        assertTrue(item.resourceLinks.isEmpty())
    }

    // ======================== Session (computed properties) ========================

    @Test
    fun `Session getAvailableSeats returns correct value`() {
        val session = Session(maxCapacity = 30, currentEnrollment = 18)
        assertEquals(12, session.getAvailableSeats())
    }

    @Test
    fun `Session getAvailableSeats when full`() {
        val session = Session(maxCapacity = 20, currentEnrollment = 20)
        assertEquals(0, session.getAvailableSeats())
    }

    @Test
    fun `Session getAvailableSeats when empty`() {
        val session = Session(maxCapacity = 25, currentEnrollment = 0)
        assertEquals(25, session.getAvailableSeats())
    }

    @Test
    fun `Session getCapacityPercent returns correct percentage`() {
        val session = Session(maxCapacity = 100, currentEnrollment = 75)
        assertEquals(75.0f, session.getCapacityPercent(), 0.1f)
    }

    @Test
    fun `Session getCapacityPercent at full capacity`() {
        val session = Session(maxCapacity = 20, currentEnrollment = 20)
        assertEquals(100.0f, session.getCapacityPercent(), 0.1f)
    }

    @Test
    fun `Session getCapacityPercent when empty`() {
        val session = Session(maxCapacity = 20, currentEnrollment = 0)
        assertEquals(0.0f, session.getCapacityPercent(), 0.1f)
    }

    @Test
    fun `Session default values are correct`() {
        val session = Session()
        assertEquals("", session.sessionId)
        assertEquals(20, session.maxCapacity)
        assertEquals(0, session.currentEnrollment)
        assertFalse(session.isLive)
        assertTrue(session.isBidAllowed)
    }

    // ======================== Enrollment ========================

    @Test
    fun `Enrollment default values`() {
        val enrollment = Enrollment()
        assertEquals("", enrollment.enrollmentId)
        assertEquals(0f, enrollment.progress, 0.01f)
        assertFalse(enrollment.isCompleted)
        assertEquals("", enrollment.certificateUrl)
    }

    @Test
    fun `Enrollment with progress`() {
        val enrollment = Enrollment(
            enrollmentId = "e1",
            progress = 75.0f,
            isCompleted = false
        )
        assertEquals(75.0f, enrollment.progress, 0.01f)
    }

    // ======================== Bid (computed properties) ========================

    @Test
    fun `Bid getSavings returns correct amount`() {
        val bid = Bid(originalPrice = 100.0, bidAmount = 70.0)
        assertEquals(30.0, bid.getSavings(), 0.01)
    }

    @Test
    fun `Bid getSavings returns zero when bid equals original`() {
        val bid = Bid(originalPrice = 100.0, bidAmount = 100.0)
        assertEquals(0.0, bid.getSavings(), 0.01)
    }

    @Test
    fun `Bid getSavingsPercent returns correct percentage`() {
        val bid = Bid(originalPrice = 200.0, bidAmount = 150.0)
        assertEquals(25.0, bid.getSavingsPercent(), 0.01)
    }

    @Test
    fun `Bid getSavingsPercent returns zero when no savings`() {
        val bid = Bid(originalPrice = 200.0, bidAmount = 200.0)
        assertEquals(0.0, bid.getSavingsPercent(), 0.01)
    }

    @Test
    fun `Bid default status is PENDING`() {
        val bid = Bid()
        assertEquals("PENDING", bid.status)
        assertTrue(bid.isNew)
    }

    @Test
    fun `Bid copy changes status correctly`() {
        val bid = Bid(bidId = "b1", status = "PENDING")
        val accepted = bid.copy(status = "ACCEPTED")
        assertEquals("PENDING", bid.status)
        assertEquals("ACCEPTED", accepted.status)
    }

    // ======================== BidRequest ========================

    @Test
    fun `BidRequest default values`() {
        val request = BidRequest()
        assertEquals("", request.contentId)
        assertEquals(0.0, request.bidAmount, 0.01)
        assertEquals("", request.negotiationMessage)
    }

    @Test
    fun `BidRequest with values`() {
        val request = BidRequest(
            contentId = "c1",
            bidAmount = 250.0,
            negotiationMessage = "Can I get a discount?"
        )
        assertEquals("c1", request.contentId)
        assertEquals(250.0, request.bidAmount, 0.01)
    }

    // ======================== WalletTransaction ========================

    @Test
    fun `WalletTransaction default values`() {
        val txn = WalletTransaction()
        assertEquals("", txn.transactionId)
        assertEquals("", txn.type)
        assertEquals(0.0, txn.amount, 0.01)
        assertEquals("SUCCESS", txn.status)
    }

    @Test
    fun `WalletTransaction for top-up`() {
        val txn = WalletTransaction(
            transactionId = "t1",
            type = "TOPUP",
            amount = 1000.0,
            status = "SUCCESS"
        )
        assertEquals("TOPUP", txn.type)
        assertEquals(1000.0, txn.amount, 0.01)
    }

    // ======================== DashboardData ========================

    @Test
    fun `DashboardData default is all empty`() {
        val data = DashboardData()
        assertEquals(StudentStats(), data.stats)
        assertTrue(data.sessions.isEmpty())
        assertTrue(data.content.isEmpty())
        assertTrue(data.teachers.isEmpty())
        assertTrue(data.enrollments.isEmpty())
        assertTrue(data.unlockedContent.isEmpty())
        assertTrue(data.bids.isEmpty())
        assertTrue(data.transactions.isEmpty())
    }

    @Test
    fun `DashboardData with populated lists`() {
        val data = DashboardData(
            sessions = listOf(Session(sessionId = "s1")),
            bids = listOf(Bid(bidId = "b1"), Bid(bidId = "b2")),
            teachers = listOf(Teacher(name = "John"))
        )
        assertEquals(1, data.sessions.size)
        assertEquals(2, data.bids.size)
        assertEquals(1, data.teachers.size)
    }
}
