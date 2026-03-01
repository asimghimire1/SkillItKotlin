package com.example.kot_start

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VideoPlayerScreen(
    video: Video,
    onBackClick: () -> Unit,
    onPlayClick: () -> Unit = {},
    isTeacher: Boolean = false
) {
    val context = LocalContext.current
    var playerInstance by remember { mutableStateOf<ExoPlayer?>(null) }

    // Initialize ExoPlayer
    LaunchedEffect(Unit) {
        val exoPlayer = ExoPlayer.Builder(context).build()
        if (video.videoUrl.isNotEmpty()) {
            val mediaItem = MediaItem.fromUri(Uri.parse(video.videoUrl))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }
        playerInstance = exoPlayer
    }

    // Cleanup
    DisposableEffect(Unit) {
        onDispose {
            playerInstance?.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Video Player
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color.Black)
                ) {
                    if (playerInstance != null) {
                        AndroidView(
                            factory = {
                                PlayerView(context).apply {
                                    player = playerInstance
                                    useController = true
                                    controllerShowTimeoutMs = 5000
                                    controllerHideTimeoutMs = 5000
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        // Fallback thumbnail if video URL is empty
                        AsyncImage(
                            model = video.thumbnailUrl,
                            contentDescription = video.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Back button overlay
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            }

            // Video Info
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Title
                    Text(
                        text = video.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Category and difficulty badges
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color(0xFFEA2A33).copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = video.category,
                                fontSize = 12.sp,
                                color = Color(0xFFEA2A33),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        if (video.isPaid) {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFF10B981).copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "PAID - $${String.format("%.2f", video.price)}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF10B981),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        } else {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFF6B7280).copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "FREE",
                                    fontSize = 12.sp,
                                    color = Color(0xFF6B7280),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Teacher info
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AsyncImage(
                            model = video.teacherName,
                            contentDescription = video.teacherName,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFD1D5DB)),
                            contentScale = ContentScale.Crop
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = video.teacherName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Text(
                                text = "${video.studentCount} students",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                        }

                        if (isTeacher) {
                            Button(
                                onClick = onPlayClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFEA2A33)
                                ),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Edit", fontSize = 12.sp)
                            }
                        } else {
                            Button(
                                onClick = onPlayClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFEA2A33)
                                ),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Enroll", fontSize = 12.sp)
                            }
                        }
                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        color = Color(0xFFE5E7EB)
                    )

                    // Description
                    Text(
                        text = "About this course",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = video.description,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Created date
                    Text(
                        text = "Created: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(video.createdAt))}",
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }

            // Related Videos (Optional)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "More from ${video.teacherName}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Placeholder for related videos
                    Text(
                        text = "More content coming soon",
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun SessionDetailsScreen(
    session: Session,
    onBackClick: () -> Unit,
    onJoinClick: () -> Unit = {},
    isTeacher: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFFEA2A33))
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Event,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = session.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 2
                        )
                    }
                }
            }

            // Session Info
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    SessionInfoRow(
                        label = "Category",
                        value = session.category,
                        icon = Icons.Default.School
                    )
                    SessionInfoRow(
                        label = "Difficulty",
                        value = session.difficulty,
                        icon = Icons.Default.TrendingUp
                    )
                    SessionInfoRow(
                        label = "Date & Time",
                        value = "${session.date} at ${session.time}",
                        icon = Icons.Default.Schedule
                    )
                    SessionInfoRow(
                        label = "Status",
                        value = session.status.replaceFirstChar { it.uppercase() },
                        icon = Icons.Default.CheckCircle
                    )

                    if (session.isPaid) {
                        SessionInfoRow(
                            label = "Price",
                            value = "$${String.format("%.2f", session.price)}",
                            icon = Icons.Default.Payment
                        )
                    } else {
                        SessionInfoRow(
                            label = "Price",
                            value = "FREE",
                            icon = Icons.Default.Payment
                        )
                    }

                    if (session.meetingLink.isNotEmpty()) {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            color = Color(0xFFE5E7EB)
                        )

                        Text(
                            text = "Meeting Link",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6B7280),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        SelectionContainer {
                            Text(
                                text = session.meetingLink,
                                fontSize = 13.sp,
                                color = Color(0xFFEA2A33),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFEA2A33).copy(alpha = 0.1f))
                                    .padding(12.dp)
                            )
                        }
                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        color = Color(0xFFE5E7EB)
                    )

                    Text(
                        text = "Description",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = session.description,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Button
                    Button(
                        onClick = onJoinClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEA2A33)
                        )
                    ) {
                        Icon(
                            Icons.Default.VideoCall,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isTeacher) "Start Session" else "Join Session",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun SessionInfoRow(
    label: String,
    value: String,
    icon: androidx.compose.material.icons.materialIcon
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color(0xFFEA2A33),
            modifier = Modifier.size(20.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun SelectionContainer(content: @Composable () -> Unit) {
    Box(modifier = Modifier) {
        content()
    }
}
