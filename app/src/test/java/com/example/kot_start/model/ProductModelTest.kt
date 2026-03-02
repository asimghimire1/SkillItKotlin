package com.example.kot_start.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for ProductModel data class.
 * Covers: construction, defaults, toMap conversion, mutation (productId is var).
 */
class ProductModelTest {

    // ======================== Construction & Defaults ========================

    @Test
    fun `default constructor creates model with empty or zero fields`() {
        val product = ProductModel()
        assertEquals("", product.productId)
        assertEquals("", product.name)
        assertEquals(0.0, product.price, 0.01)
        assertEquals("", product.description)
        assertEquals("", product.stock)
    }

    @Test
    fun `constructor with all fields sets values correctly`() {
        val product = ProductModel(
            productId = "prod123",
            name = "Kotlin Course",
            price = 499.99,
            description = "Learn Kotlin from scratch",
            stock = "50"
        )
        assertEquals("prod123", product.productId)
        assertEquals("Kotlin Course", product.name)
        assertEquals(499.99, product.price, 0.01)
        assertEquals("Learn Kotlin from scratch", product.description)
        assertEquals("50", product.stock)
    }

    // ======================== toMap ========================

    @Test
    fun `toMap returns all fields as map`() {
        val product = ProductModel(
            productId = "prod123",
            name = "Test Product",
            price = 100.50,
            description = "A test product",
            stock = "10"
        )
        val map = product.toMap()

        assertEquals(5, map.size)
        assertEquals("prod123", map["productId"])
        assertEquals("Test Product", map["name"])
        assertEquals(100.50, map["price"])
        assertEquals("A test product", map["description"])
        assertEquals("10", map["stock"])
    }

    @Test
    fun `toMap with default values returns empty strings and zero`() {
        val product = ProductModel()
        val map = product.toMap()

        assertEquals("", map["productId"])
        assertEquals("", map["name"])
        assertEquals(0.0, map["price"])
        assertEquals("", map["description"])
        assertEquals("", map["stock"])
    }

    // ======================== Mutable productId ========================

    @Test
    fun `productId can be changed after construction`() {
        val product = ProductModel()
        assertEquals("", product.productId)

        product.productId = "new_id_123"
        assertEquals("new_id_123", product.productId)
    }

    @Test
    fun `toMap reflects changed productId`() {
        val product = ProductModel(name = "Test")
        product.productId = "generated_id"

        val map = product.toMap()
        assertEquals("generated_id", map["productId"])
    }

    // ======================== Equality ========================

    @Test
    fun `two products with same data are equal`() {
        val p1 = ProductModel(productId = "1", name = "A", price = 10.0)
        val p2 = ProductModel(productId = "1", name = "A", price = 10.0)
        assertEquals(p1, p2)
    }

    @Test
    fun `two products with different names are not equal`() {
        val p1 = ProductModel(name = "A")
        val p2 = ProductModel(name = "B")
        assertNotEquals(p1, p2)
    }

    // ======================== Copy ========================

    @Test
    fun `copy creates independent copy with overridden fields`() {
        val product = ProductModel(name = "Original", price = 100.0)
        val updated = product.copy(price = 200.0)

        assertEquals(100.0, product.price, 0.01)
        assertEquals(200.0, updated.price, 0.01)
        assertEquals("Original", updated.name)
    }
}
