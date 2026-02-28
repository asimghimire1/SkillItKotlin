package com.example.kot_start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kot_start.model.Content
import com.example.kot_start.model.Enrollment
import com.example.kot_start.model.Session
import com.example.kot_start.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentMyLearningScreen(viewModel: StudentViewModel) {
    val enrollments by viewModel.enrollments.collectAsState()
    val sessions by viewModel.sessions.collectAsState()
    
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Tab Bar
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    height = 3.dp,
                    color = Color(0xFFEA2A33)
                )
            },
            divider = {}
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        "My Content",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) Color(0xFFEA2A33) else Color.Gray
                        )
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        "My Sessions",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) Color(0xFFEA2A33) else Color.Gray
                        )
                    )
                }
            )
        }

        // Tab Content
        when (selectedTab) {
            0 -> MyContentScreen(enrollments)
            1 -> MySessionsScreen(sessions)
        }
    }
}

@Composable
fun MyContentScreen(enrollments: List<Enrollment>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Continue Watching Section
        item {
            if (enrollments.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Continue Watching",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val firstEnrollment = enrollments.first()
                    ContinueWatchingCard(enrollment = firstEnrollment)
                }
            }
        }

        // Recent Courses Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Courses",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Text(
                        text = "See All",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEA2A33)
                        )
                    )
                }
            }
        }

        // Course Cards
        items(enrollments.take(3)) { enrollment ->
            RecentCourseCard(enrollment = enrollment)
        }

        // Bottom Padding
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun MySessionsScreen(sessions: List<Session>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Upcoming Sessions Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Upcoming Sessions",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }

        // Session Cards
        items(sessions) { session ->
            UpcomingSessionCardItem(session = session)
        }

        // Bottom Padding
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ContinueWatchingCard(enrollment: Enrollment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB8A89E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFB8A89E),
                            Color(0xFF8B7D74)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Video Thumbnail Placeholder
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Prev Button
                Button(
                    onClick = { },
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.8f))
                ) {
                    Text(
                        text = "<",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }

                // Play Button
                FloatingActionButton(
                    onClick = { },
                    containerColor = Color.White,
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play",
                        tint = Color(0xFFEA2A33),
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Next Button
                Button(
                    onClick = { },
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.8f))
                ) {
                    Text(
                        text = ">",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        Text(
            text = enrollment.contentTitle,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )
        Text(
            text = "Lesson 4 • 15 min remaining",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            ),
            modifier = Modifier.padding(top = 4.dp)
        )

        // Resume Button
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(36.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Resume",
                tint = Color.White,
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Resume",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}

@Composable
fun RecentCourseCard(enrollment: Enrollment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF757575))
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = "Course thumbnail",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = enrollment.contentTitle,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = "2 Modules • ${enrollment.progress.toInt()}% Completed",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Progress Bar
                LinearProgressIndicator(
                    progress = enrollment.progress / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    trackColor = Color(0xFFE0E0E0),
                    color = Color(0xFFEA2A33)
                )
            }
        }
    }
}

@Composable
fun UpcomingSessionCardItem(session: Session) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date and Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Date
                Text(
                    text = "OCT\n24",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEA2A33),
                        lineHeight = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Title and Time
                Text(
                    text = session.title,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    maxLines = 1
                )
                Text(
                    text = formatSessionTime(session.startTime),
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Arrow Icon
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private fun formatSessionTime(startTime: Long): String {
    val calendar = java.util.Calendar.getInstance()
    calendar.timeInMillis = startTime
    val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
    val minute = calendar.get(java.util.Calendar.MINUTE)
    val period = if (hour >= 12) "PM" else "AM"
    val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
    return String.format("Tomorrow 0%d:00 AM • Live", displayHour)
}
