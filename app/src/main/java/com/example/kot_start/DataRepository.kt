package com.example.kot_start

import android.net.Uri
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class DataRepository {
    private val database = Firebase.database
    private val storage = Firebase.storage

    // ==================== VIDEO OPERATIONS ====================
    suspend fun uploadVideo(videoUri: Uri, thumbnailUri: Uri?, video: Video): Result<String> = runCatching {
        val videoRef = storage.reference.child("videos/${System.currentTimeMillis()}_${video.title}")
        videoRef.putFile(videoUri).await()
        val videoUrl = videoRef.downloadUrl.await().toString()

        val thumbnailUrl = if (thumbnailUri != null) {
            val thumbnailRef = storage.reference.child("thumbnails/${System.currentTimeMillis()}_${video.title}")
            thumbnailRef.putFile(thumbnailUri).await()
            thumbnailRef.downloadUrl.await().toString()
        } else {
            ""
        }

        val updatedVideo = video.copy(
            videoUrl = videoUrl,
            thumbnailUrl = thumbnailUrl,
            id = database.reference.child("videos").push().key ?: ""
        )

        database.reference.child("videos").child(updatedVideo.id).setValue(updatedVideo).await()
        updatedVideo.id
    }

    suspend fun getVideo(videoId: String): Result<Video> = runCatching {
        val snapshot = database.reference.child("videos").child(videoId).get().await()
        snapshot.getValue(Video::class.java) ?: throw Exception("Video not found")
    }

    suspend fun getTeacherVideos(teacherId: String): Result<List<Video>> = runCatching {
        val snapshot = database.reference.child("videos").get().await()
        snapshot.children.mapNotNull { it.getValue(Video::class.java) }
            .filter { it.teacherId == teacherId }
    }

    suspend fun deleteVideo(videoId: String): Result<Unit> = runCatching {
        database.reference.child("videos").child(videoId).removeValue().await()
    }

    // ==================== SESSION OPERATIONS ====================
    suspend fun createSession(session: Session): Result<String> = runCatching {
        val sessionId = database.reference.child("sessions").push().key ?: ""
        val newSession = session.copy(id = sessionId)
        database.reference.child("sessions").child(sessionId).setValue(newSession).await()
        sessionId
    }

    suspend fun getTeacherSessions(teacherId: String): Result<List<Session>> = runCatching {
        val snapshot = database.reference.child("sessions").get().await()
        snapshot.children.mapNotNull { it.getValue(Session::class.java) }
            .filter { it.teacherId == teacherId }
            .sortedByDescending { it.createdAt }
    }

    suspend fun deleteSession(sessionId: String): Result<Unit> = runCatching {
        database.reference.child("sessions").child(sessionId).removeValue().await()
    }

    // ==================== BID OPERATIONS ====================
    suspend fun getTeacherBids(teacherId: String): Result<List<Bid>> = runCatching {
        val snapshot = database.reference.child("bids").get().await()
        snapshot.children.mapNotNull { it.getValue(Bid::class.java) }
            .filter { it.teacherId == teacherId }
            .sortedByDescending { it.createdAt }
    }

    suspend fun updateBid(bid: Bid): Result<Unit> = runCatching {
        database.reference.child("bids").child(bid.id).setValue(bid).await()
    }

    suspend fun updateBidStatus(bidId: String, status: String): Result<Unit> = runCatching {
        database.reference.child("bids").child(bidId).child("status").setValue(status).await()
    }

    // ==================== TRANSACTION OPERATIONS ====================
    suspend fun getTeacherTransactions(teacherId: String): Result<List<Transaction>> = runCatching {
        val snapshot = database.reference.child("transactions").child(teacherId).get().await()
        snapshot.children.mapNotNull { it.getValue(Transaction::class.java) }
            .sortedByDescending { it.timestamp }
    }

    suspend fun getTotalBalance(teacherId: String): Result<Float> = runCatching {
        val snapshot = database.reference.child("teacher_balances").child(teacherId).get().await()
        snapshot.getValue(Float::class.java) ?: 0f
    }

    // ==================== PROFILE OPERATIONS ====================
    suspend fun updateTeacherProfile(profile: TeacherProfile): Result<Unit> = runCatching {
        database.reference.child("teachers").child(profile.uid).setValue(profile).await()
    }

    suspend fun getTeacherProfile(uid: String): Result<TeacherProfile> = runCatching {
        val snapshot = database.reference.child("teachers").child(uid).get().await()
        snapshot.getValue(TeacherProfile::class.java) ?: throw Exception("Profile not found")
    }
}
