package com.example.kot_start.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for UserModel data class.
 * Covers: construction, default values, toMap conversion, equality.
 */
class UserModelTest {

    // ======================== Construction & Defaults ========================

    @Test
    fun `default constructor creates model with empty fields`() {
        val user = UserModel()
        assertEquals("", user.userId)
        assertEquals("", user.email)
        assertEquals("", user.password)
        assertEquals("", user.firstName)
        assertEquals("", user.lastName)
        assertEquals("", user.dob)
        assertEquals("", user.role)
    }

    @Test
    fun `constructor with all fields sets values correctly`() {
        val user = UserModel(
            userId = "uid123",
            email = "test@example.com",
            password = "pass123",
            firstName = "John",
            lastName = "Doe",
            dob = "1995-01-15",
            role = "Student"
        )
        assertEquals("uid123", user.userId)
        assertEquals("test@example.com", user.email)
        assertEquals("pass123", user.password)
        assertEquals("John", user.firstName)
        assertEquals("Doe", user.lastName)
        assertEquals("1995-01-15", user.dob)
        assertEquals("Student", user.role)
    }

    // ======================== toMap ========================

    @Test
    fun `toMap returns all fields as map`() {
        val user = UserModel(
            userId = "uid123",
            email = "test@example.com",
            password = "pass123",
            firstName = "John",
            lastName = "Doe",
            dob = "1995-01-15",
            role = "Teacher"
        )
        val map = user.toMap()

        assertEquals(7, map.size)
        assertEquals("uid123", map["userId"])
        assertEquals("test@example.com", map["email"])
        assertEquals("pass123", map["password"])
        assertEquals("John", map["firstName"])
        assertEquals("Doe", map["lastName"])
        assertEquals("1995-01-15", map["dob"])
        assertEquals("Teacher", map["role"])
    }

    @Test
    fun `toMap with default values returns empty strings`() {
        val user = UserModel()
        val map = user.toMap()

        assertEquals(7, map.size)
        assertEquals("", map["userId"])
        assertEquals("", map["email"])
        assertEquals("", map["password"])
        assertEquals("", map["firstName"])
        assertEquals("", map["lastName"])
        assertEquals("", map["dob"])
        assertEquals("", map["role"])
    }

    // ======================== Equality ========================

    @Test
    fun `two UserModels with same data are equal`() {
        val user1 = UserModel(userId = "1", email = "a@b.com", role = "Student")
        val user2 = UserModel(userId = "1", email = "a@b.com", role = "Student")
        assertEquals(user1, user2)
    }

    @Test
    fun `two UserModels with different data are not equal`() {
        val user1 = UserModel(userId = "1")
        val user2 = UserModel(userId = "2")
        assertNotEquals(user1, user2)
    }

    // ======================== Copy ========================

    @Test
    fun `copy creates independent copy with overridden fields`() {
        val user = UserModel(userId = "1", firstName = "John", role = "Student")
        val updated = user.copy(role = "Teacher")

        assertEquals("Student", user.role)
        assertEquals("Teacher", updated.role)
        assertEquals("John", updated.firstName)
    }

    // ======================== Role Validation ========================

    @Test
    fun `role can be Student`() {
        val user = UserModel(role = "Student")
        assertEquals("Student", user.role)
    }

    @Test
    fun `role can be Teacher`() {
        val user = UserModel(role = "Teacher")
        assertEquals("Teacher", user.role)
    }
}
