package com.example.kot_start

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TeacherViewModel : ViewModel() {
    private val repository = DataRepository()

    // UI States
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Video states
    private val _videoList = MutableStateFlow<List<Video>>(emptyList())
    val videoList: StateFlow<List<Video>> = _videoList.asStateFlow()

    private val _currentVideo = MutableStateFlow<Video?>(null)
    val currentVideo: StateFlow<Video?> = _currentVideo.asStateFlow()

    // Session states
    private val _sessionList = MutableStateFlow<List<Session>>(emptyList())
    val sessionList: StateFlow<List<Session>> = _sessionList.asStateFlow()

    // Bid states
    private val _bidList = MutableStateFlow<List<Bid>>(emptyList())
    val bidList: StateFlow<List<Bid>> = _bidList.asStateFlow()

    // Transaction states
    private val _transactionList = MutableStateFlow<List<Transaction>>(emptyList())
    val transactionList: StateFlow<List<Transaction>> = _transactionList.asStateFlow()

    private val _totalBalance = MutableStateFlow(0f)
    val totalBalance: StateFlow<Float> = _totalBalance.asStateFlow()

    // Profile state
    private val _teacherProfile = MutableStateFlow<TeacherProfile?>(null)
    val teacherProfile: StateFlow<TeacherProfile?> = _teacherProfile.asStateFlow()

    // ==================== VIDEO OPERATIONS ====================
    fun uploadVideo(
        videoUri: Uri,
        thumbnailUri: Uri?,
        title: String,
        description: String,
        category: String,
        isPaid: Boolean,
        price: Float,
        teacherId: String,
        teacherName: String
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val video = Video(
                    title = title,
                    description = description,
                    category = category,
                    isPaid = isPaid,
                    price = price,
                    teacherId = teacherId,
                    teacherName = teacherName
                )
                val result = repository.uploadVideo(videoUri, thumbnailUri, video)
                result.onSuccess { videoId ->
                    _uiState.value = UiState.Success("Video uploaded successfully")
                    loadTeacherVideos(teacherId)
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to upload video")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadTeacherVideos(teacherId: String) {
        viewModelScope.launch {
            try {
                val result = repository.getTeacherVideos(teacherId)
                result.onSuccess { videos ->
                    _videoList.value = videos
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to load videos")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadVideo(videoId: String) {
        viewModelScope.launch {
            try {
                val result = repository.getVideo(videoId)
                result.onSuccess { video ->
                    _currentVideo.value = video
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to load video")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteVideo(videoId: String, teacherId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = repository.deleteVideo(videoId)
                result.onSuccess {
                    _uiState.value = UiState.Success("Video deleted successfully")
                    loadTeacherVideos(teacherId)
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to delete video")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // ==================== SESSION OPERATIONS ====================
    fun createSession(
        title: String,
        category: String,
        difficulty: String,
        description: String,
        date: String,
        time: String,
        meetingLink: String,
        isPaid: Boolean,
        price: Float,
        teacherId: String,
        teacherName: String
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val session = Session(
                    title = title,
                    category = category,
                    difficulty = difficulty,
                    description = description,
                    date = date,
                    time = time,
                    meetingLink = meetingLink,
                    isPaid = isPaid,
                    price = price,
                    teacherId = teacherId,
                    teacherName = teacherName
                )
                val result = repository.createSession(session)
                result.onSuccess { sessionId ->
                    _uiState.value = UiState.Success("Session created successfully")
                    loadTeacherSessions(teacherId)
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to create session")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadTeacherSessions(teacherId: String) {
        viewModelScope.launch {
            try {
                val result = repository.getTeacherSessions(teacherId)
                result.onSuccess { sessions ->
                    _sessionList.value = sessions
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to load sessions")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteSession(sessionId: String, teacherId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = repository.deleteSession(sessionId)
                result.onSuccess {
                    _uiState.value = UiState.Success("Session deleted successfully")
                    loadTeacherSessions(teacherId)
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to delete session")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // ==================== BID OPERATIONS ====================
    fun loadTeacherBids(teacherId: String) {
        viewModelScope.launch {
            try {
                val result = repository.getTeacherBids(teacherId)
                result.onSuccess { bids ->
                    _bidList.value = bids
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to load bids")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun acceptBid(bidId: String, teacherId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = repository.updateBidStatus(bidId, "completed")
                result.onSuccess {
                    _uiState.value = UiState.Success("Bid accepted successfully")
                    loadTeacherBids(teacherId)
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to accept bid")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun sendCounterOffer(bidId: String, counterPrice: Float, teacherId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val bid = _bidList.value.find { it.id == bidId } ?: return@launch
                val updatedBid = bid.copy(
                    counterOffer = counterPrice,
                    counterOfferBy = "teacher",
                    status = "pending"
                )
                val result = repository.updateBid(updatedBid)
                result.onSuccess {
                    _uiState.value = UiState.Success("Counter offer sent")
                    loadTeacherBids(teacherId)
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to send counter offer")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // ==================== TRANSACTION OPERATIONS ====================
    fun loadTeacherTransactions(teacherId: String) {
        viewModelScope.launch {
            try {
                val result = repository.getTeacherTransactions(teacherId)
                result.onSuccess { transactions ->
                    _transactionList.value = transactions
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to load transactions")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadTotalBalance(teacherId: String) {
        viewModelScope.launch {
            try {
                val result = repository.getTotalBalance(teacherId)
                result.onSuccess { balance ->
                    _totalBalance.value = balance
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to load balance")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // ==================== PROFILE OPERATIONS ====================
    fun loadTeacherProfile(uid: String) {
        viewModelScope.launch {
            try {
                val result = repository.getTeacherProfile(uid)
                result.onSuccess { profile ->
                    _teacherProfile.value = profile
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to load profile")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateTeacherProfile(profile: TeacherProfile) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = repository.updateTeacherProfile(profile)
                result.onSuccess {
                    _teacherProfile.value = profile
                    _uiState.value = UiState.Success("Profile updated successfully")
                }
                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to update profile")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun clearUiState() {
        _uiState.value = UiState.Idle
    }
}

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}
