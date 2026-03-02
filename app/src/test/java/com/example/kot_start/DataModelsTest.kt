package com.example.kot_start

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for DataModels.kt (Teacher-side models).
 * Covers: Video, Session, Bid, Transaction, TeacherProfile, StudentProfile.
 */
class DataModelsTest {

    // ======================== Video ========================

    @Test
    fun `Video default values are correct`() {
        val video = Video()
        assertEquals("", video.id)
        assertEquals("", video.title)
        assertEquals("", video.videoUrl)
        assertFalse(video.isPaid)
        assertEquals(0f, video.price, 0.01f)
        assertEquals("published", video.status)
        assertEquals(0, video.studentCount)
    }

    @Test
    fun `Video with all fields`() {
        val video = Video(
            id = "v1",
            title = "Kotlin 101",
            description = "Intro to Kotlin",
            videoUrl = "https://example.com/video.mp4",
            category = "Programming",
            isPaid = true,
            price = 29.99f,
            teacherId = "t1",
            teacherName = "John",
            duration = 3600L,
            studentCount = 50,
            status = "published"
        )
        assertEquals("v1", video.id)
        assertTrue(video.isPaid)
        assertEquals(29.99f, video.price, 0.01f)
        assertEquals(50, video.studentCount)
    }

    @Test
    fun `Video copy creates independent instance`() {
        val video = Video(title = "Original", status = "draft")
        val published = video.copy(status = "published")
        assertEquals("draft", video.status)
        assertEquals("published", published.status)
    }

    @Test
    fun `Video equality check`() {
        // Timestamps will differ, so create with same timestamp
        val ts = 1704067200000L
        val v1 = Video(id = "v1", title = "A", createdAt = ts)
        val v2 = Video(id = "v1", title = "A", createdAt = ts)
        assertEquals(v1, v2)
    }

    // ======================== Session (Teacher-side) ========================

    @Test
    fun `Session default values are correct`() {
        val session = Session()
        assertEquals("", session.id)
        assertEquals("", session.title)
        assertFalse(session.isPaid)
        assertEquals(0f, session.price, 0.01f)
        assertEquals("scheduled", session.status)
        assertEquals(0, session.studentsRegistered)
    }

    @Test
    fun `Session with paid and live status`() {
        val session = Session(
            id = "s1",
            title = "Live Kotlin",
            isPaid = true,
            price = 49.99f,
            status = "live",
            studentsRegistered = 15
        )
        assertTrue(session.isPaid)
        assertEquals("live", session.status)
        assertEquals(15, session.studentsRegistered)
    }

    @Test
    fun `Session status values are valid strings`() {
        val scheduled = Session(status = "scheduled")
        val live = Session(status = "live")
        val completed = Session(status = "completed")
        assertEquals("scheduled", scheduled.status)
        assertEquals("live", live.status)
        assertEquals("completed", completed.status)
    }

    // ======================== Bid (Teacher-side) ========================

    @Test
    fun `Bid default values are correct`() {
        val bid = Bid()
        assertEquals("", bid.id)
        assertEquals("standard", bid.priority)
        assertEquals("active", bid.status)
        assertEquals(0f, bid.studentOffer, 0.01f)
        assertEquals(0f, bid.counterOffer, 0.01f)
    }

    @Test
    fun `Bid with urgent priority`() {
        val bid = Bid(
            id = "b1",
            studentName = "Alice",
            originalPrice = 100f,
            studentOffer = 80f,
            priority = "urgent",
            status = "pending"
        )
        assertEquals("urgent", bid.priority)
        assertEquals(80f, bid.studentOffer, 0.01f)
    }

    @Test
    fun `Bid counter offer`() {
        val bid = Bid(
            originalPrice = 100f,
            studentOffer = 70f,
            counterOffer = 85f,
            counterOfferBy = "teacher"
        )
        assertEquals(85f, bid.counterOffer, 0.01f)
        assertEquals("teacher", bid.counterOfferBy)
    }

    @Test
    fun `Bid status transitions via copy`() {
        val bid = Bid(status = "active")
        val pending = bid.copy(status = "pending")
        val completed = pending.copy(status = "completed")
        val rejected = bid.copy(status = "rejected")

        assertEquals("active", bid.status)
        assertEquals("pending", pending.status)
        assertEquals("completed", completed.status)
        assertEquals("rejected", rejected.status)
    }

    // ======================== Transaction ========================

    @Test
    fun `Transaction default values are correct`() {
        val txn = Transaction()
        assertEquals("", txn.id)
        assertEquals("", txn.type)
        assertEquals(0f, txn.amount, 0.01f)
        assertEquals("completed", txn.status)
    }

    @Test
    fun `Transaction types are valid`() {
        val withdrawal = Transaction(type = "withdrawal", amount = 500f)
        val sale = Transaction(type = "course_sale", amount = 299f)
        val session = Transaction(type = "session_sale", amount = 99f)

        assertEquals("withdrawal", withdrawal.type)
        assertEquals("course_sale", sale.type)
        assertEquals("session_sale", session.type)
    }

    @Test
    fun `Transaction with various statuses`() {
        val completed = Transaction(status = "completed")
        val processing = Transaction(status = "processing")
        val failed = Transaction(status = "failed")

        assertEquals("completed", completed.status)
        assertEquals("processing", processing.status)
        assertEquals("failed", failed.status)
    }

    // ======================== TeacherProfile ========================

    @Test
    fun `TeacherProfile default values`() {
        val profile = TeacherProfile()
        assertEquals("", profile.uid)
        assertEquals("", profile.name)
        assertEquals(0f, profile.totalEarnings, 0.01f)
        assertEquals(0f, profile.totalBalance, 0.01f)
    }

    @Test
    fun `TeacherProfile with earnings`() {
        val profile = TeacherProfile(
            uid = "t1",
            name = "Professor X",
            totalEarnings = 15000f,
            totalBalance = 3500f
        )
        assertEquals(15000f, profile.totalEarnings, 0.01f)
        assertEquals(3500f, profile.totalBalance, 0.01f)
    }

    // ======================== StudentProfile ========================

    @Test
    fun `StudentProfile default values`() {
        val profile = StudentProfile()
        assertEquals("", profile.uid)
        assertEquals("", profile.name)
        assertTrue(profile.enrolledCourses.isEmpty())
    }

    @Test
    fun `StudentProfile with enrolled courses`() {
        val profile = StudentProfile(
            uid = "s1",
            name = "Alice",
            enrolledCourses = listOf("course1", "course2", "course3")
        )
        assertEquals(3, profile.enrolledCourses.size)
        assertTrue(profile.enrolledCourses.contains("course2"))
    }
}
