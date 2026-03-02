package com.example.kot_start.viewmodel

import com.example.kot_start.model.*
import com.example.kot_start.repository.UserRepo
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

/**
 * Unit tests for StudentViewModel.
 * Tests business logic: tab switching, search/filter, credit management,
 * enrollment, content unlocking, bid submission, bid cancellation,
 * counter offer responses, and modal state management.
 *
 * Uses TestCoroutineDispatcher for coroutine testing.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class StudentViewModelTest {

    private lateinit var mockRepo: UserRepo
    private lateinit var viewModel: StudentViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepo = mock()

        // Mock getCurrentUser to return a fake FirebaseUser
        val mockUser: FirebaseUser = mock()
        whenever(mockUser.uid).thenReturn("test_student_uid")
        whenever(mockRepo.getCurrentUser()).thenReturn(mockUser)

        viewModel = StudentViewModel(mockRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ======================== UI State ========================

    @Test
    fun `initial active tab is dashboard`() {
        assertEquals("dashboard", viewModel.activeTab.value)
    }

    @Test
    fun `setActiveTab changes active tab`() {
        viewModel.setActiveTab("wallet")
        assertEquals("wallet", viewModel.activeTab.value)
    }

    @Test
    fun `setActiveTab to bids`() {
        viewModel.setActiveTab("bids")
        assertEquals("bids", viewModel.activeTab.value)
    }

    @Test
    fun `initial dark mode is true`() {
        assertTrue(viewModel.isDarkMode.value)
    }

    @Test
    fun `toggleDarkMode switches mode`() {
        viewModel.toggleDarkMode()
        assertFalse(viewModel.isDarkMode.value)
        viewModel.toggleDarkMode()
        assertTrue(viewModel.isDarkMode.value)
    }

    @Test
    fun `initial menu is closed`() {
        assertFalse(viewModel.isMenuOpen.value)
    }

    @Test
    fun `toggleMenu opens and closes menu`() {
        viewModel.toggleMenu()
        assertTrue(viewModel.isMenuOpen.value)
        viewModel.toggleMenu()
        assertFalse(viewModel.isMenuOpen.value)
    }

    @Test
    fun `closeMenu closes menu`() {
        viewModel.toggleMenu() // open
        assertTrue(viewModel.isMenuOpen.value)
        viewModel.closeMenu()
        assertFalse(viewModel.isMenuOpen.value)
    }

    // ======================== Search & Filter ========================

    @Test
    fun `initial search query is empty`() {
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `updateSearchQuery updates query value`() {
        viewModel.updateSearchQuery("kotlin")
        assertEquals("kotlin", viewModel.searchQuery.value)
    }

    @Test
    fun `updateSearchQuery filters sessions by title`() = runTest {
        // Load data first
        viewModel.loadDashboardData("student1")

        // Search for a specific session
        viewModel.updateSearchQuery("Kotlin")
        val filtered = viewModel.filteredSessions.value
        assertTrue(filtered.isNotEmpty())
        assertTrue(filtered.all {
            it.title.contains("Kotlin", ignoreCase = true) ||
            it.category.contains("Kotlin", ignoreCase = true) ||
            it.teacherName.contains("Kotlin", ignoreCase = true)
        })
    }

    @Test
    fun `updateSearchQuery with empty string returns all sessions`() = runTest {
        viewModel.loadDashboardData("student1")
        val allSessions = viewModel.sessions.value

        viewModel.updateSearchQuery("")
        assertEquals(allSessions.size, viewModel.filteredSessions.value.size)
    }

    @Test
    fun `updateSearchQuery with no match returns empty`() = runTest {
        viewModel.loadDashboardData("student1")

        viewModel.updateSearchQuery("xyznonexistent123")
        assertTrue(viewModel.filteredSessions.value.isEmpty())
    }

    // ======================== Load Dashboard Data ========================

    @Test
    fun `loadDashboardData populates all data fields`() = runTest {
        viewModel.loadDashboardData("student1")

        assertNotEquals(StudentStats(), viewModel.stats.value)
        assertTrue(viewModel.sessions.value.isNotEmpty())
        assertTrue(viewModel.content.value.isNotEmpty())
        assertTrue(viewModel.teachers.value.isNotEmpty())
        assertTrue(viewModel.enrollments.value.isNotEmpty())
        assertTrue(viewModel.bids.value.isNotEmpty())
        assertTrue(viewModel.transactions.value.isNotEmpty())
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `loadDashboardData sets loading to false after completion`() = runTest {
        viewModel.loadDashboardData("student1")
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `loadDashboardData populates stats with non-default values`() = runTest {
        viewModel.loadDashboardData("student1")

        val stats = viewModel.stats.value
        assertTrue(stats.totalCourses > 0)
        assertTrue(stats.credits > 0)
        assertEquals("Intermediate", stats.level)
    }

    // ======================== Add Credits ========================

    @Test
    fun `addCredits increases wallet balance`() = runTest {
        viewModel.loadDashboardData("student1")
        val initialCredits = viewModel.stats.value.credits

        viewModel.addCredits(1000.0)

        assertEquals(initialCredits + 1000.0, viewModel.stats.value.credits, 0.01)
    }

    @Test
    fun `addCredits creates TOPUP transaction`() = runTest {
        viewModel.loadDashboardData("student1")
        val initialTxnCount = viewModel.transactions.value.size

        viewModel.addCredits(500.0)

        val transactions = viewModel.transactions.value
        assertEquals(initialTxnCount + 1, transactions.size)
        assertEquals("TOPUP", transactions.first().type)
        assertEquals(500.0, transactions.first().amount, 0.01)
        assertEquals("SUCCESS", transactions.first().status)
    }

    @Test
    fun `addCredits multiple times accumulates`() = runTest {
        viewModel.loadDashboardData("student1")
        val initial = viewModel.stats.value.credits

        viewModel.addCredits(100.0)
        viewModel.addCredits(200.0)

        assertEquals(initial + 300.0, viewModel.stats.value.credits, 0.01)
    }

    // ======================== Enroll In Session ========================

    @Test
    fun `enrollInSession adds enrollment and deducts credits`() = runTest {
        viewModel.loadDashboardData("student1")
        val initialCredits = viewModel.stats.value.credits
        val initialEnrollments = viewModel.enrollments.value.size
        val initialCourses = viewModel.stats.value.totalCourses

        val session = viewModel.sessions.value.first()
        viewModel.enrollInSession(session)

        // Check credits deducted
        assertEquals(initialCredits - session.price, viewModel.stats.value.credits, 0.01)
        // Check enrollment added
        assertEquals(initialEnrollments + 1, viewModel.enrollments.value.size)
        // Check course count increased
        assertEquals(initialCourses + 1, viewModel.stats.value.totalCourses)
    }

    @Test
    fun `enrollInSession creates PURCHASE transaction`() = runTest {
        viewModel.loadDashboardData("student1")

        val session = viewModel.sessions.value.first()
        viewModel.enrollInSession(session)

        val latestTxn = viewModel.transactions.value.first()
        assertEquals("PURCHASE", latestTxn.type)
        assertEquals(session.price, latestTxn.amount, 0.01)
        assertTrue(latestTxn.description.contains(session.title))
    }

    // ======================== Unlock Content ========================

    @Test
    fun `unlockContent deducts credits and adds to unlocked list`() = runTest {
        viewModel.loadDashboardData("student1")
        val initialCredits = viewModel.stats.value.credits
        val initialUnlocked = viewModel.unlockedContent.value.size

        val lockedContent = viewModel.content.value.first { it.isLocked }
        viewModel.unlockContent(lockedContent)

        assertEquals(initialCredits - lockedContent.price, viewModel.stats.value.credits, 0.01)
        assertEquals(initialUnlocked + 1, viewModel.unlockedContent.value.size)
    }

    @Test
    fun `unlockContent fails with insufficient credits`() = runTest {
        viewModel.loadDashboardData("student1")

        // Create content more expensive than available credits
        val expensiveContent = Content(
            contentId = "expensive",
            title = "Very Expensive",
            price = 999999.0,
            isLocked = true
        )
        viewModel.unlockContent(expensiveContent)

        assertTrue(viewModel.errorMessage.value.contains("Insufficient"))
    }

    @Test
    fun `unlockContent creates PURCHASE transaction`() = runTest {
        viewModel.loadDashboardData("student1")

        val content = viewModel.content.value.first { it.isLocked }
        viewModel.unlockContent(content)

        val latestTxn = viewModel.transactions.value.first()
        assertEquals("PURCHASE", latestTxn.type)
        assertTrue(latestTxn.description.contains(content.title))
    }

    // ======================== Submit Bid ========================

    @Test
    fun `submitBid adds bid to list with valid amount`() = runTest {
        viewModel.loadDashboardData("student1")
        val initialBids = viewModel.bids.value.size

        val content = viewModel.content.value.first()
        val bidRequest = BidRequest(
            contentId = content.contentId,
            bidAmount = content.price * 0.8, // 80% of original
            negotiationMessage = "Test bid"
        )
        viewModel.submitBid(bidRequest)

        assertEquals(initialBids + 1, viewModel.bids.value.size)
        val newBid = viewModel.bids.value.first()
        assertEquals("PENDING", newBid.status)
        assertEquals(content.price * 0.8, newBid.bidAmount, 0.01)
    }

    @Test
    fun `submitBid rejects bid below 60 percent`() = runTest {
        viewModel.loadDashboardData("student1")
        val initialBids = viewModel.bids.value.size

        val content = viewModel.content.value.first()
        val lowBid = BidRequest(
            contentId = content.contentId,
            bidAmount = content.price * 0.5, // 50% - too low
            negotiationMessage = "Lowball bid"
        )
        viewModel.submitBid(lowBid)

        // Bid count should not change
        assertEquals(initialBids, viewModel.bids.value.size)
        assertTrue(viewModel.errorMessage.value.contains("60-100%"))
    }

    @Test
    fun `submitBid accepts bid at exactly 60 percent`() = runTest {
        viewModel.loadDashboardData("student1")
        val initialBids = viewModel.bids.value.size

        val content = viewModel.content.value.first()
        val minBid = BidRequest(
            contentId = content.contentId,
            bidAmount = content.price * 0.6, // Exactly 60%
            negotiationMessage = "Minimum bid"
        )
        viewModel.submitBid(minBid)

        assertEquals(initialBids + 1, viewModel.bids.value.size)
    }

    @Test
    fun `submitBid accepts bid at full price`() = runTest {
        viewModel.loadDashboardData("student1")
        val initialBids = viewModel.bids.value.size

        val content = viewModel.content.value.first()
        val fullBid = BidRequest(
            contentId = content.contentId,
            bidAmount = content.price, // 100%
            negotiationMessage = "Full price bid"
        )
        viewModel.submitBid(fullBid)

        assertEquals(initialBids + 1, viewModel.bids.value.size)
    }

    // ======================== Cancel Bid ========================

    @Test
    fun `cancelBid changes bid status to REJECTED`() = runTest {
        viewModel.loadDashboardData("student1")

        val bidId = viewModel.bids.value.first().bidId
        viewModel.cancelBid(bidId)

        val cancelledBid = viewModel.bids.value.find { it.bidId == bidId }
        assertEquals("REJECTED", cancelledBid?.status)
    }

    @Test
    fun `cancelBid does not affect other bids`() = runTest {
        viewModel.loadDashboardData("student1")

        val allBids = viewModel.bids.value
        val firstBidId = allBids.first().bidId
        viewModel.cancelBid(firstBidId)

        // Other bids should remain unchanged
        allBids.drop(1).forEach { originalBid ->
            val currentBid = viewModel.bids.value.find { it.bidId == originalBid.bidId }
            assertEquals(originalBid.status, currentBid?.status)
        }
    }

    // ======================== Respond To Counter ========================

    @Test
    fun `respondToCounter accept changes status and deducts credits`() = runTest {
        viewModel.loadDashboardData("student1")
        val initialCredits = viewModel.stats.value.credits

        // Find a countered bid
        val counteredBid = viewModel.bids.value.find { it.status == "COUNTERED" }
        assertNotNull(counteredBid)

        viewModel.respondToCounter(counteredBid!!.bidId, accepted = true)

        val updatedBid = viewModel.bids.value.find { it.bidId == counteredBid.bidId }
        assertEquals("ACCEPTED", updatedBid?.status)
        assertEquals(
            initialCredits - counteredBid.counterOfferAmount,
            viewModel.stats.value.credits,
            0.01
        )
    }

    @Test
    fun `respondToCounter reject changes status to REJECTED`() = runTest {
        viewModel.loadDashboardData("student1")

        val counteredBid = viewModel.bids.value.find { it.status == "COUNTERED" }
        assertNotNull(counteredBid)

        viewModel.respondToCounter(counteredBid!!.bidId, accepted = false)

        val updatedBid = viewModel.bids.value.find { it.bidId == counteredBid.bidId }
        assertEquals("REJECTED", updatedBid?.status)
    }

    @Test
    fun `respondToCounter accept creates PURCHASE transaction`() = runTest {
        viewModel.loadDashboardData("student1")
        val initialTxnCount = viewModel.transactions.value.size

        val counteredBid = viewModel.bids.value.find { it.status == "COUNTERED" }
        viewModel.respondToCounter(counteredBid!!.bidId, accepted = true)

        assertEquals(initialTxnCount + 1, viewModel.transactions.value.size)
        assertEquals("PURCHASE", viewModel.transactions.value.first().type)
    }

    // ======================== Modal State ========================

    @Test
    fun `bid modal initially hidden`() {
        assertFalse(viewModel.isBidModalVisible.value)
    }

    @Test
    fun `showBidModal and hideBidModal toggle correctly`() {
        viewModel.showBidModal()
        assertTrue(viewModel.isBidModalVisible.value)
        viewModel.hideBidModal()
        assertFalse(viewModel.isBidModalVisible.value)
    }

    @Test
    fun `wallet modal initially hidden`() {
        assertFalse(viewModel.isWalletModalVisible.value)
    }

    @Test
    fun `showWalletModal and hideWalletModal toggle correctly`() {
        viewModel.showWalletModal()
        assertTrue(viewModel.isWalletModalVisible.value)
        viewModel.hideWalletModal()
        assertFalse(viewModel.isWalletModalVisible.value)
    }

    @Test
    fun `enrollment modal initially hidden`() {
        assertFalse(viewModel.isEnrollmentModalVisible.value)
    }

    @Test
    fun `showEnrollmentModal and hideEnrollmentModal toggle correctly`() {
        viewModel.showEnrollmentModal()
        assertTrue(viewModel.isEnrollmentModalVisible.value)
        viewModel.hideEnrollmentModal()
        assertFalse(viewModel.isEnrollmentModalVisible.value)
    }

    // ======================== Selection State ========================

    @Test
    fun `selectSession and clearSelectedSession work`() = runTest {
        viewModel.loadDashboardData("student1")

        val session = viewModel.sessions.value.first()
        viewModel.selectSession(session)
        assertEquals(session, viewModel.selectedSession.value)

        viewModel.clearSelectedSession()
        assertNull(viewModel.selectedSession.value)
    }

    @Test
    fun `selectContent and clearSelectedContent work`() = runTest {
        viewModel.loadDashboardData("student1")

        val content = viewModel.content.value.first()
        viewModel.selectContent(content)
        assertEquals(content, viewModel.selectedContent.value)

        viewModel.clearSelectedContent()
        assertNull(viewModel.selectedContent.value)
    }

    // ======================== Logout ========================

    @Test
    fun `logoutUser delegates to repo`() {
        val callback: (Boolean, String) -> Unit = { _, _ -> }
        viewModel.logoutUser(callback)

        verify(mockRepo).logout(any())
    }
}
