package com.example.kot_start.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kot_start.model.*
import com.example.kot_start.repository.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class StudentViewModel(val userRepo: UserRepo) : ViewModel() {

    // ============== UI STATE ==============
    private val _activeTab = MutableStateFlow("dashboard")
    val activeTab: StateFlow<String> = _activeTab.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isMenuOpen = MutableStateFlow(false)
    val isMenuOpen: StateFlow<Boolean> = _isMenuOpen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    // ============== DATA STATE ==============
    private val _stats = MutableStateFlow(StudentStats())
    val stats: StateFlow<StudentStats> = _stats.asStateFlow()

    private val _sessions = MutableStateFlow<List<Session>>(emptyList())
    val sessions: StateFlow<List<Session>> = _sessions.asStateFlow()

    private val _filteredSessions = MutableStateFlow<List<Session>>(emptyList())
    val filteredSessions: StateFlow<List<Session>> = _filteredSessions.asStateFlow()

    private val _content = MutableStateFlow<List<Content>>(emptyList())
    val content: StateFlow<List<Content>> = _content.asStateFlow()

    private val _teachers = MutableStateFlow<List<Teacher>>(emptyList())
    val teachers: StateFlow<List<Teacher>> = _teachers.asStateFlow()

    private val _enrollments = MutableStateFlow<List<Enrollment>>(emptyList())
    val enrollments: StateFlow<List<Enrollment>> = _enrollments.asStateFlow()

    private val _unlockedContent = MutableStateFlow<List<Content>>(emptyList())
    val unlockedContent: StateFlow<List<Content>> = _unlockedContent.asStateFlow()

    private val _bids = MutableStateFlow<List<Bid>>(emptyList())
    val bids: StateFlow<List<Bid>> = _bids.asStateFlow()

    private val _transactions = MutableStateFlow<List<WalletTransaction>>(emptyList())
    val transactions: StateFlow<List<WalletTransaction>> = _transactions.asStateFlow()

    // ============== MODAL/NAVIGATION STATE ==============
    private val _selectedSession = MutableStateFlow<Session?>(null)
    val selectedSession: StateFlow<Session?> = _selectedSession.asStateFlow()

    private val _selectedContent = MutableStateFlow<Content?>(null)
    val selectedContent: StateFlow<Content?> = _selectedContent.asStateFlow()

    private val _isBidModalVisible = MutableStateFlow(false)
    val isBidModalVisible: StateFlow<Boolean> = _isBidModalVisible.asStateFlow()

    private val _isWalletModalVisible = MutableStateFlow(false)
    val isWalletModalVisible: StateFlow<Boolean> = _isWalletModalVisible.asStateFlow()

    private val _isEnrollmentModalVisible = MutableStateFlow(false)
    val isEnrollmentModalVisible: StateFlow<Boolean> = _isEnrollmentModalVisible.asStateFlow()

    // ============== COMPUTED STATE ==============
    val pendingBidsCount: StateFlow<Int>
        get() = MutableStateFlow(_bids.value.count { it.isNew }).asStateFlow()

    val totalAvailableCredits: StateFlow<Double>
        get() = MutableStateFlow(_stats.value.credits).asStateFlow()

    // ============== UI STATE MANAGEMENT ==============
    fun setActiveTab(tabName: String) {
        _activeTab.value = tabName
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun toggleMenu() {
        _isMenuOpen.value = !_isMenuOpen.value
    }

    fun closeMenu() {
        _isMenuOpen.value = false
    }

    // ============== SEARCH FUNCTIONALITY ==============
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterSessions(query)
    }

    private fun filterSessions(query: String) {
        if (query.isEmpty()) {
            _filteredSessions.value = _sessions.value
        } else {
            _filteredSessions.value = _sessions.value.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true) ||
                it.teacherName.contains(query, ignoreCase = true)
            }
        }
    }

    // ============== CORE BUSINESS LOGIC ==============
    
    /**
     * Load all dashboard data for student
     */
    fun loadDashboardData(studentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Mock data - Replace with actual Firebase calls
                val mockStats = StudentStats(
                    totalCourses = 5,
                    completedCourses = 2,
                    totalHours = 45.5,
                    credits = 5000.0,
                    level = "Intermediate",
                    badges = listOf("Quick Learner", "Problem Solver")
                )
                _stats.value = mockStats

                // Load mock sessions
                _sessions.value = getMockSessions()
                _filteredSessions.value = _sessions.value

                // Load mock content
                _content.value = getMockContent()

                // Load mock teachers
                _teachers.value = getMockTeachers()

                // Load enrollments
                _enrollments.value = getMockEnrollments()

                // Load unlocked content
                _unlockedContent.value = _content.value.filter { !it.isLocked }

                // Load bids
                _bids.value = getMockBids()

                // Load transactions
                _transactions.value = getMockTransactions()

                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error"
                _isLoading.value = false
            }
        }
    }

    /**
     * Add credits to wallet
     */
    fun addCredits(amount: Double) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Update stats
                val updatedStats = _stats.value.copy(
                    credits = _stats.value.credits + amount
                )
                _stats.value = updatedStats

                // Add transaction record
                val transaction = WalletTransaction(
                    transactionId = UUID.randomUUID().toString(),
                    studentId = userRepo.getCurrentUser()?.uid ?: "",
                    type = "TOPUP",
                    amount = amount,
                    description = "Wallet Top-up: ${amount}",
                    timestamp = System.currentTimeMillis(),
                    status = "SUCCESS"
                )
                _transactions.value = listOf(transaction) + _transactions.value

                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to add credits"
                _isLoading.value = false
            }
        }
    }

    /**
     * Enroll in a session
     */
    fun enrollInSession(session: Session) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val userId = userRepo.getCurrentUser()?.uid ?: return@launch
                val enrollment = Enrollment(
                    enrollmentId = UUID.randomUUID().toString(),
                    studentId = userId,
                    contentId = session.sessionId,
                    contentTitle = session.title,
                    teacherName = session.teacherName,
                    enrolledDate = System.currentTimeMillis(),
                    progress = 0f
                )

                // Add to enrollments
                _enrollments.value = _enrollments.value + enrollment

                // Deduct from wallet
                val updatedStats = _stats.value.copy(
                    credits = _stats.value.credits - session.price,
                    totalCourses = _stats.value.totalCourses + 1
                )
                _stats.value = updatedStats

                // Record transaction
                val transaction = WalletTransaction(
                    transactionId = UUID.randomUUID().toString(),
                    studentId = userId,
                    type = "PURCHASE",
                    amount = session.price,
                    description = "Enrolled in: ${session.title}",
                    timestamp = System.currentTimeMillis(),
                    status = "SUCCESS"
                )
                _transactions.value = listOf(transaction) + _transactions.value

                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to enroll"
                _isLoading.value = false
            }
        }
    }

    /**
     * Unlock premium content with credits
     */
    fun unlockContent(content: Content) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val userId = userRepo.getCurrentUser()?.uid ?: return@launch

                // Check if student has enough credits
                if (_stats.value.credits < content.price) {
                    _errorMessage.value = "Insufficient credits. Need NPR ${content.price}"
                    _isLoading.value = false
                    return@launch
                }

                // Deduct from wallet
                val updatedStats = _stats.value.copy(
                    credits = _stats.value.credits - content.price
                )
                _stats.value = updatedStats

                // Mark as unlocked
                val unlockedContentItem = content.copy(isLocked = false)
                _unlockedContent.value = _unlockedContent.value + unlockedContentItem

                // Record transaction
                val transaction = WalletTransaction(
                    transactionId = UUID.randomUUID().toString(),
                    studentId = userId,
                    type = "PURCHASE",
                    amount = content.price,
                    description = "Unlocked: ${content.title}",
                    timestamp = System.currentTimeMillis(),
                    status = "SUCCESS"
                )
                _transactions.value = listOf(transaction) + _transactions.value

                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to unlock content"
                _isLoading.value = false
            }
        }
    }

    /**
     * Submit a bid for content
     */
    fun submitBid(bidRequest: BidRequest) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val userId = userRepo.getCurrentUser()?.uid ?: return@launch
                
                // Find the content being bid on
                val contentItem = _content.value.find { it.contentId == bidRequest.contentId }
                    ?: return@launch

                // Validate bid (must be 60-100% of original price)
                if (!validateBid(bidRequest.bidAmount, contentItem.price)) {
                    _errorMessage.value = "Bid must be between 60-100% of original price (NPR ${contentItem.price * 0.6} - ${contentItem.price})"
                    _isLoading.value = false
                    return@launch
                }

                // Create bid
                val bid = Bid(
                    bidId = UUID.randomUUID().toString(),
                    studentId = userId,
                    contentId = contentItem.contentId,
                    contentTitle = contentItem.title,
                    teacherId = contentItem.teacherId,
                    teacherName = contentItem.teacherName,
                    originalPrice = contentItem.price,
                    bidAmount = bidRequest.bidAmount,
                    status = "PENDING",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    isNew = true
                )

                _bids.value = listOf(bid) + _bids.value
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to submit bid"
                _isLoading.value = false
            }
        }
    }

    /**
     * Cancel an existing bid
     */
    fun cancelBid(bidId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val updatedBids = _bids.value.map { bid ->
                    if (bid.bidId == bidId) {
                        bid.copy(status = "REJECTED", updatedAt = System.currentTimeMillis())
                    } else {
                        bid
                    }
                }
                _bids.value = updatedBids
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to cancel bid"
                _isLoading.value = false
            }
        }
    }

    /**
     * Respond to counter offer from teacher
     */
    fun respondToCounter(bidId: String, accepted: Boolean) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val updatedBids = _bids.value.map { bid ->
                    if (bid.bidId == bidId) {
                        if (accepted) {
                            // Deduct counter offer amount from wallet
                            val updatedStats = _stats.value.copy(
                                credits = _stats.value.credits - bid.counterOfferAmount
                            )
                            _stats.value = updatedStats

                            // Record transaction
                            val transaction = WalletTransaction(
                                transactionId = UUID.randomUUID().toString(),
                                studentId = userRepo.getCurrentUser()?.uid ?: "",
                                type = "PURCHASE",
                                amount = bid.counterOfferAmount,
                                description = "Bid accepted: ${bid.contentTitle}",
                                timestamp = System.currentTimeMillis(),
                                status = "SUCCESS"
                            )
                            _transactions.value = listOf(transaction) + _transactions.value

                            bid.copy(status = "ACCEPTED", updatedAt = System.currentTimeMillis())
                        } else {
                            bid.copy(status = "REJECTED", updatedAt = System.currentTimeMillis())
                        }
                    } else {
                        bid
                    }
                }
                _bids.value = updatedBids
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to respond to counter"
                _isLoading.value = false
            }
        }
    }

    /**
     * Logout user
     */
    fun logoutUser(callback: (Boolean, String) -> Unit) {
        userRepo.logout(callback)
    }

    /**
     * Validate bid amount (60-100% of original)
     */
    private fun validateBid(bidAmount: Double, originalPrice: Double): Boolean {
        val minBid = originalPrice * 0.60
        val maxBid = originalPrice
        return bidAmount in minBid..maxBid
    }

    /**
     * Select a session to view details
     */
    fun selectSession(session: Session) {
        _selectedSession.value = session
    }

    /**
     * Select content to view details
     */
    fun selectContent(content: Content) {
        _selectedContent.value = content
    }

    /**
     * Clear selected session
     */
    fun clearSelectedSession() {
        _selectedSession.value = null
    }

    /**
     * Clear selected content
     */
    fun clearSelectedContent() {
        _selectedContent.value = null
    }

    /**
     * Show bid modal
     */
    fun showBidModal() {
        _isBidModalVisible.value = true
    }

    /**
     * Hide bid modal
     */
    fun hideBidModal() {
        _isBidModalVisible.value = false
    }

    /**
     * Show wallet modal
     */
    fun showWalletModal() {
        _isWalletModalVisible.value = true
    }

    /**
     * Hide wallet modal
     */
    fun hideWalletModal() {
        _isWalletModalVisible.value = false
    }

    /**
     * Show enrollment modal
     */
    fun showEnrollmentModal() {
        _isEnrollmentModalVisible.value = true
    }

    /**
     * Hide enrollment modal
     */
    fun hideEnrollmentModal() {
        _isEnrollmentModalVisible.value = false
    }

    // ============== MOCK DATA GENERATORS ==============
    private fun getMockSessions(): List<Session> {
        return listOf(
            Session(
                sessionId = "session1",
                title = "Advanced Kotlin Coroutines",
                description = "Deep dive into Kotlin coroutines and async programming",
                teacherId = "teacher1",
                teacherName = "John Smith",
                teacherImage = "https://via.placeholder.com/150",
                teacherRating = 4.8,
                category = "Mobile Development",
                startTime = System.currentTimeMillis() + 3600000,
                duration = 90,
                maxCapacity = 30,
                currentEnrollment = 18,
                price = 499.0,
                isLive = true,
                isBidAllowed = true
            ),
            Session(
                sessionId = "session2",
                title = "Firebase Real-time Database Mastery",
                description = "Learn Firebase and build real-time applications",
                teacherId = "teacher2",
                teacherName = "Sarah Connor",
                teacherImage = "https://via.placeholder.com/150",
                teacherRating = 4.9,
                category = "Backend Development",
                startTime = System.currentTimeMillis() + 7200000,
                duration = 120,
                maxCapacity = 25,
                currentEnrollment = 20,
                price = 599.0,
                isLive = false,
                isBidAllowed = true
            )
        )
    }

    private fun getMockContent(): List<Content> {
        return listOf(
            Content(
                contentId = "content1",
                title = "Android UI/UX Design Basics",
                description = "Master the fundamentals of beautiful Android interfaces",
                category = "Design",
                price = 299.0,
                duration = 240,
                teacherId = "teacher3",
                teacherName = "Design Pro",
                rating = 4.6,
                level = "Beginner",
                isFree = false,
                isLocked = true
            ),
            Content(
                contentId = "content2",
                title = "Jetpack Compose Advanced",
                description = "Advanced Compose patterns and best practices",
                category = "Mobile Development",
                price = 399.0,
                duration = 360,
                teacherId = "teacher1",
                teacherName = "John Smith",
                rating = 4.7,
                level = "Advanced",
                isFree = false,
                isLocked = true
            )
        )
    }

    private fun getMockTeachers(): List<Teacher> {
        return listOf(
            Teacher(
                teacherId = "teacher1",
                name = "John Smith",
                email = "john@example.com",
                expertise = listOf("Kotlin", "Android", "Coroutines"),
                rating = 4.8,
                reviewCount = 250,
                hourlyRate = 50.0,
                isVerified = true
            ),
            Teacher(
                teacherId = "teacher2",
                name = "Sarah Connor",
                email = "sarah@example.com",
                expertise = listOf("Firebase", "Backend", "Cloud Functions"),
                rating = 4.9,
                reviewCount = 310,
                hourlyRate = 60.0,
                isVerified = true
            )
        )
    }

    private fun getMockEnrollments(): List<Enrollment> {
        return listOf(
            Enrollment(
                enrollmentId = "enroll1",
                studentId = "student1",
                contentId = "content1",
                contentTitle = "Android Basics",
                teacherName = "John Smith",
                enrolledDate = System.currentTimeMillis() - 2592000000, // 30 days ago
                progress = 75f
            )
        )
    }

    private fun getMockBids(): List<Bid> {
        return listOf(
            Bid(
                bidId = "bid1",
                studentId = "student1",
                contentId = "content1",
                contentTitle = "Android UI Design",
                teacherId = "teacher1",
                teacherName = "John Smith",
                originalPrice = 299.0,
                bidAmount = 199.0,
                status = "PENDING",
                createdAt = System.currentTimeMillis() - 86400000,
                isNew = true
            ),
            Bid(
                bidId = "bid2",
                studentId = "student1",
                contentId = "content2",
                contentTitle = "Jetpack Compose",
                teacherId = "teacher2",
                teacherName = "Sarah Connor",
                originalPrice = 399.0,
                bidAmount = 279.0,
                status = "COUNTERED",
                counterOfferAmount = 349.0,
                createdAt = System.currentTimeMillis() - 172800000,
                isNew = false
            )
        )
    }

    private fun getMockTransactions(): List<WalletTransaction> {
        return listOf(
            WalletTransaction(
                transactionId = "txn1",
                studentId = "student1",
                type = "TOPUP",
                amount = 2000.0,
                description = "Wallet Top-up",
                timestamp = System.currentTimeMillis() - 86400000,
                status = "SUCCESS"
            ),
            WalletTransaction(
                transactionId = "txn2",
                studentId = "student1",
                type = "PURCHASE",
                amount = 299.0,
                description = "Purchased: Android UI Design",
                timestamp = System.currentTimeMillis() - 172800000,
                status = "SUCCESS"
            )
        )
    }
}
