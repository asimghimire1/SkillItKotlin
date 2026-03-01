package com.example.kot_start

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

// ==================== SCHEDULE SESSION - FUNCTIONAL ====================
@Composable
fun ScheduleSessionScreenFunctional(
    viewModel: TeacherViewModel,
    onBackClick: () -> Unit,
    onSessionCreated: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    
    var sessionTitle by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Design") }
    var difficulty by remember { mutableStateOf("Beginner") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var meetingLink by remember { mutableStateOf("") }
    var isPaid by remember { mutableStateOf(false) }
    var price by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .paddingFromBaseline(top = 80.dp)
    ) {
        item {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        "Schedule Session",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF3F4F6)
                        ),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Drafts", color = Color.Black, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // Title
                Text("Session Title", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                TextField(
                    value = sessionTitle,
                    onValueChange = { sessionTitle = it },
                    placeholder = { Text("e.g., Advanced UI Design", fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category
                Text("Category", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                SelectDropdown(
                    selectedValue = category,
                    options = listOf("Design", "Technology", "Business", "Lifestyle"),
                    onSelect = { category = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Difficulty
                Text("Difficulty", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                SelectDropdown(
                    selectedValue = difficulty,
                    options = listOf("Beginner", "Intermediate", "Advanced"),
                    onSelect = { difficulty = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Description
                Text("Description", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Describe your session...", fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(vertical = 8.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Date and Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Date", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        TextField(
                            value = date,
                            onValueChange = { date = it },
                            placeholder = { Text("YYYY-MM-DD", fontSize = 13.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White
                            ),
                            trailingIcon = {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Time", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        TextField(
                            value = time,
                            onValueChange = { time = it },
                            placeholder = { Text("HH:MM", fontSize = 13.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White
                            ),
                            trailingIcon = {
                                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Meeting Link
                Text("Meeting Link (Zoom, Google Meet, etc.)", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                TextField(
                    value = meetingLink,
                    onValueChange = { meetingLink = it },
                    placeholder = { Text("https://...", fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    leadingIcon = {
                        Icon(Icons.Default.VideoCall, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Pricing
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Price", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    PricingToggleButton(
                        isPaid = isPaid,
                        onToggle = { isPaid = it },
                        modifier = Modifier.width(100.dp)
                    )
                }

                if (isPaid) {
                    TextField(
                        value = price,
                        onValueChange = { price = it },
                        placeholder = { Text("0.00", fontSize = 13.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        prefix = { Text("$") }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Schedule Button
                Button(
                    onClick = {
                        if (sessionTitle.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {
                            coroutineScope.launch {
                                viewModel.createSession(
                                    title = sessionTitle,
                                    category = category,
                                    difficulty = difficulty,
                                    description = description,
                                    date = date,
                                    time = time,
                                    meetingLink = meetingLink,
                                    isPaid = isPaid,
                                    price = price.toFloatOrNull() ?: 0f,
                                    teacherId = "teacher_123", // Replace with actual ID
                                    teacherName = "Teacher Name" // Replace with actual name
                                )
                            }
                            onSessionCreated()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEA2A33)
                    ),
                    enabled = uiState !is UiState.Loading
                ) {
                    if (uiState is UiState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Schedule Session", fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// ==================== UPLOAD CONTENT - FUNCTIONAL ====================
@Composable
fun UploadContentScreenFunctional(
    viewModel: TeacherViewModel,
    videoLauncher: androidx.activity.compose.ManagedActivityResultLauncher<String, Uri?>,
    thumbnailLauncher: androidx.activity.compose.ManagedActivityResultLauncher<String, Uri?>,
    onBackClick: () -> Unit,
    onVideoUploaded: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var thumbnailUri by remember { mutableStateOf<Uri?>(null) }
    var videoTitle by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Design") }
    var description by remember { mutableStateOf("") }
    var isPaid by remember { mutableStateOf(false) }
    var price by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .paddingFromBaseline(top = 80.dp)
    ) {
        item {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        "Upload Content",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // Video Upload Zone
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .clickable { videoLauncher.launch("video/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (videoUri == null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                Icons.Default.MovieCreation,
                                contentDescription = null,
                                tint = Color(0xFFEA2A33),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Upload Video",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "MP4, MOV, AVI (Max 500MB)",
                                fontSize = 12.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                    } else {
                        Text("Video Selected ✓", fontSize = 14.sp, color = Color(0xFF10B981), fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Thumbnail
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp, 67.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .clickable { thumbnailLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (thumbnailUri == null) {
                            Icon(
                                Icons.Default.Image,
                                contentDescription = null,
                                tint = Color(0xFFD1D5DB),
                                modifier = Modifier.size(32.dp)
                            )
                        } else {
                            AsyncImage(
                                model = thumbnailUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Button(
                        onClick = { thumbnailLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF3F4F6)
                        ),
                        modifier = Modifier
                            .height(67.dp)
                            .weight(1f)
                    ) {
                        Text(
                            "Change Thumbnail",
                            color = Color.Black,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text("Video Title", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                TextField(
                    value = videoTitle,
                    onValueChange = { videoTitle = it },
                    placeholder = { Text("e.g., Advanced UI Design Masterclass", fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category
                Text("Category", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                SelectDropdown(
                    selectedValue = category,
                    options = listOf("Design", "Technology", "Business", "Lifestyle"),
                    onSelect = { category = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Description
                Text("Description", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Describe your course...", fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(vertical = 8.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Pricing
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Price", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    PricingToggleButton(
                        isPaid = isPaid,
                        onToggle = { isPaid = it },
                        modifier = Modifier.width(100.dp)
                    )
                }

                if (isPaid) {
                    TextField(
                        value = price,
                        onValueChange = { price = it },
                        placeholder = { Text("0.00", fontSize = 13.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        prefix = { Text("$") }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Publish Button
                Button(
                    onClick = {
                        if (videoUri != null && videoTitle.isNotEmpty()) {
                            coroutineScope.launch {
                                viewModel.uploadVideo(
                                    videoUri = videoUri!!,
                                    thumbnailUri = thumbnailUri,
                                    title = videoTitle,
                                    description = description,
                                    category = category,
                                    isPaid = isPaid,
                                    price = price.toFloatOrNull() ?: 0f,
                                    teacherId = "teacher_123", // Replace with actual ID
                                    teacherName = "Teacher Name" // Replace with actual name
                                )
                            }
                            onVideoUploaded()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEA2A33)
                    ),
                    enabled = uiState !is UiState.Loading
                ) {
                    if (uiState is UiState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Publish Content", fontWeight = FontWeight.SemiBold)
                    }
                }

                Text(
                    "By publishing, you agree to our Terms & Conditions.",
                    fontSize = 11.sp,
                    color = Color(0xFF9CA3AF),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// ==================== CONTENT SCREEN - FUNCTIONAL ====================
@Composable
fun TeacherContentScreenFunctional(
    viewModel: TeacherViewModel,
    onBackClick: () -> Unit,
    onVideoClick: (Video) -> Unit,
    onSessionClick: (Session) -> Unit
) {
    val videos by viewModel.videoList.collectAsState()
    val sessions by viewModel.sessionList.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadTeacherVideos("teacher_123") // Replace with actual ID
        viewModel.loadTeacherSessions("teacher_123") // Replace with actual ID
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .paddingFromBaseline(top = 80.dp)
        ) {
            item {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                        Text(
                            "Teacher Dashboard",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                        }
                    }
                }
            }

            // Tabs
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    TabButton(
                        title = "My Courses",
                        isActive = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    TabButton(
                        title = "Live Sessions",
                        isActive = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                }
                Divider(color = Color(0xFFE5E7EB), thickness = 1.dp)
            }

            // Content
            if (selectedTab == 0) {
                // Courses Tab
                items(videos.size) { index ->
                    val video = videos[index]
                    CourseCard(
                        video = video,
                        onClick = { onVideoClick(video) },
                        modifier = Modifier.padding(12.dp)
                    )
                }
            } else {
                // Sessions Tab
                items(sessions.size) { index ->
                    val session = sessions[index]
                    SessionCard(
                        session = session,
                        onClick = { onSessionClick(session) },
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun CourseCard(
    video: Video,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp, 80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(video.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 2)
                Row(
                    modifier = Modifier
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFFEA2A33).copy(alpha = 0.1f)
                    ) {
                        Text(
                            if (video.status == "published") "Published" else "In Review",
                            fontSize = 11.sp,
                            color = if (video.status == "published") Color(0xFFEA2A33) else Color(0xFF9CA3AF),
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
                Text(
                    "${video.studentCount} Students • \$${String.format("%.2f", if (video.isPaid) video.price else 0f)}",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            IconButton(onClick = { }) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }
        }
    }
}

@Composable
private fun SessionCard(
    session: Session,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(session.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(
                        "${session.date} at ${session.time}",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                Surface(
                    modifier = Modifier.clip(RoundedCornerShape(4.dp)),
                    color = if (session.status == "live") Color(0xFFEA2A33) else Color(0xFF9CA3AF),
                    contentColor = Color.White
                ) {
                    Text(
                        if (session.status == "live") "STARTS IN 15 MINS" else "TOMORROW",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onClick,
                modifier = Modifier
                    .align(Alignment.End)
                    .height(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (session.status == "live") Color(0xFFEA2A33) else Color(0xFFF3F4F6)
                )
            ) {
                Text(
                    if (session.status == "live") "Start Live" else "Scheduled",
                    fontSize = 12.sp,
                    color = if (session.status == "live") Color.White else Color.Black
                )
            }
        }
    }
}

// ==================== BIDS SCREEN - FUNCTIONAL ====================
@Composable
fun TeacherBidsScreenFunctional(
    viewModel: TeacherViewModel,
    onBackClick: () -> Unit
) {
    val bids by viewModel.bidList.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    
    var selectedTab by remember { mutableStateOf("Active") }
    var showCounterSheet by remember { mutableStateOf(false) }
    var selectedBidId by remember { mutableStateOf("") }
    var counterPrice by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        viewModel.loadTeacherBids("teacher_123") // Replace with actual ID
    }

    val filteredBids = bids.filter {
        when (selectedTab) {
            "Active" -> it.status == "active"
            "Pending" -> it.status == "pending"
            "Completed" -> it.status == "completed"
            else -> true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .paddingFromBaseline(top = 80.dp)
        ) {
            item {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFFEA2A33))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Bid Requests",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { }) {
                            Box {
                                Icon(Icons.Default.Notifications, contentDescription = null)
                                Surface(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .align(Alignment.TopEnd),
                                    color = Color(0xFFEA2A33),
                                    shape = CircleShape
                                ) {}
                            }
                        }
                    }
                }
            }

            // Tabs
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Active", "Pending", "Completed").forEach { tab ->
                        TabButtonBid(
                            title = tab,
                            isActive = selectedTab == tab,
                            onClick = { selectedTab = tab }
                        )
                    }
                }
            }

            // Bid cards
            items(filteredBids.size) { index ->
                val bid = filteredBids[index]
                BidCard(
                    bid = bid,
                    onCounterOffer = {
                        selectedBidId = bid.id
                        counterPrice = bid.studentOffer
                        showCounterSheet = true
                    },
                    onAccept = {
                        coroutineScope.launch {
                            viewModel.acceptBid(bid.id, "teacher_123")
                        }
                    },
                    modifier = Modifier.padding(12.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Counter Offer Bottom Sheet
        if (showCounterSheet) {
            CounterOfferBottomSheet(
                initialPrice = counterPrice,
                onDismiss = { showCounterSheet = false },
                onSend = { newPrice ->
                    coroutineScope.launch {
                        viewModel.sendCounterOffer(selectedBidId, newPrice, "teacher_123")
                        showCounterSheet = false
                    }
                }
            )
        }
    }
}

@Composable
private fun BidCard(
    bid: Bid,
    onCounterOffer: () -> Unit,
    onAccept: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = bid.studentProfileUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD1D5DB)),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(bid.courseTitle, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(12.sp), tint = Color(0xFF6B7280))
                        Text(bid.studentName, fontSize = 11.sp, color = Color(0xFF6B7280))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Price comparison
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Original Price", fontSize = 10.sp, color = Color(0xFF6B7280))
                        Text(
                            "$${String.format("%.2f", bid.originalPrice)}",
                            fontSize = 13.sp,
                            color = Color(0xFF6B7280),
                            style = androidx.compose.ui.text.TextStyle(
                                textDecoration = androidx.compose.ui.text.TextDecoration.LineThrough
                            )
                        )
                    }

                    Column {
                        Text("Student Offer", fontSize = 10.sp, color = Color(0xFF6B7280))
                        Text(
                            "$${String.format("%.2f", bid.studentOffer)}",
                            fontSize = 14.sp,
                            color = Color(0xFFEA2A33),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCounterOffer,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF3F4F6)
                    )
                ) {
                    Text("Counter Offer", color = Color.Black, fontSize = 12.sp)
                }

                Button(
                    onClick = onAccept,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEA2A33)
                    )
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Accept", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun CounterOfferBottomSheet(
    initialPrice: Float,
    onDismiss: () -> Unit,
    onSend: (Float) -> Unit
) {
    var price by remember { mutableStateOf(initialPrice) }
    val platformFee = (price * 0.15f)
    val youReceive = (price * 0.85f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .padding(16.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(40.dp, 4.dp)
                        .background(Color(0xFFE5E7EB), RoundedCornerShape(2.dp))
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Counter Offer", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("Course Name", fontSize = 12.sp, color = Color(0xFF6B7280))

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "$${String.format("%.2f", price)}",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEA2A33)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Slider(
                    value = price,
                    onValueChange = { price = it },
                    valueRange = 40f..60f,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("$40", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                    Text("$60", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Fee breakdown
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("YOU RECEIVE", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text("$${String.format("%.2f", youReceive)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF10B981))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("PLATFORM FEE", fontSize = 12.sp, color = Color(0xFF6B7280))
                        Text("$${String.format("%.2f", platformFee)}", fontSize = 12.sp, color = Color(0xFF6B7280))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF3F4F6)
                        )
                    ) {
                        Text("Cancel", color = Color.Black)
                    }

                    Button(
                        onClick = { onSend(price) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEA2A33)
                        )
                    ) {
                        Text("Send Counter Offer", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// ==================== EARNINGS SCREEN - FUNCTIONAL ====================
@Composable
fun TeacherEarningsScreenFunctional(
    viewModel: TeacherViewModel,
    onBackClick: () -> Unit
) {
    val transactions by viewModel.transactionList.collectAsState()
    val totalBalance by viewModel.totalBalance.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTeacherTransactions("teacher_123") // Replace with actual ID
        viewModel.loadTotalBalance("teacher_123") // Replace with actual ID
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .paddingFromBaseline(top = 80.dp)
        ) {
            item {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                        Text(
                            "Wallet & Earnings",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.MoreVert, contentDescription = null)
                        }
                    }
                }
            }

            item {
                // Balance Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text("Total Available Balance", fontSize = 13.sp, color = Color.White)

                        Text(
                            "$${String.format("%.2f", totalBalance)}",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Icon(Icons.Default.Payment, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Withdraw Now", color = Color(0xFFEA2A33), fontSize = 12.sp)
                            }

                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFFFFF).copy(alpha = 0.3f)
                                )
                            ) {
                                Icon(Icons.Default.History, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("History", color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            item {
                // Income Breakdown
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IncomeCard(
                        title = "COURSES",
                        amount = 8240f,
                        growth = 14,
                        color = Color(0xFFEA2A33),
                        modifier = Modifier.weight(1f)
                    )

                    IncomeCard(
                        title = "LIVE SESSIONS",
                        amount = 4210f,
                        growth = 8,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                // Weekly Earnings Chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text("Weekly Earnings", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            horizontalArrangement = Arrangement.spacedBy(18.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            val heights = listOf(0.65f, 0.80f, 0.45f, 0.90f, 0.70f)
                            val days = listOf("MON", "TUE", "WED", "THU", "FRI")

                            heights.forEachIndexed { index, height ->
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(150.dp),
                                    verticalArrangement = Arrangement.Bottom,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(height)
                                            .background(Color(0xFFEA2A33), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(days[index], fontSize = 11.sp, color = Color(0xFF6B7280))
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                // Recent Payouts
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Recent Payouts", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                        Text("View All", fontSize = 12.sp, color = Color(0xFFEA2A33))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Transaction items from list
                if (transactions.isEmpty()) {
                    items(3) { index ->
                        val sampleTransactions = listOf(
                            Transaction(
                                type = "withdrawal",
                                amount = 1200f,
                                timestamp = System.currentTimeMillis(),
                                status = "completed",
                                description = "Withdrawal to Bank"
                            ),
                            Transaction(
                                type = "course_sale",
                                amount = 450f,
                                timestamp = System.currentTimeMillis(),
                                status = "processing",
                                description = "Course Sale: UI/UX Masterclass"
                            ),
                            Transaction(
                                type = "withdrawal",
                                amount = 2500f,
                                timestamp = System.currentTimeMillis(),
                                status = "completed",
                                description = "Withdrawal to Bank"
                            )
                        )

                        if (index < sampleTransactions.size) {
                            TransactionItem(
                                transaction = sampleTransactions[index],
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                } else {
                    items(transactions.size) { index ->
                        TransactionItem(
                            transaction = transactions[index],
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun IncomeCard(
    title: String,
    amount: Float,
    growth: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape),
                    color = color
                ) {}

                Spacer(modifier = Modifier.width(8.dp))

                Text(title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF6B7280))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "$${String.format("%.2f", amount)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
                Text("+$growth%", fontSize = 12.sp, color = Color(0xFF10B981), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun TransactionItem(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            color = when {
                transaction.type == "withdrawal" -> Color(0xFF10B981)
                transaction.type == "course_sale" -> Color(0xFF3B82F6)
                else -> Color(0xFFEA2A33)
            }
        ) {
            Icon(
                when {
                    transaction.type == "withdrawal" -> Icons.Default.AccountBalanceWallet
                    transaction.type == "course_sale" -> Icons.Default.MenuBook
                    else -> Icons.Default.Trending Up
                },
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                transaction.description,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Text(
                "Oct 22 2023 · 2:15 PM",
                fontSize = 11.sp,
                color = Color(0xFF9CA3AF)
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                "${if (transaction.amount > 0) "+" else "-"}$${String.format("%.2f", transaction.amount)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp)),
                color = if (transaction.status == "completed") Color(0xFFDCFCE7) else Color(0xFFF3F4F6),
                contentColor = if (transaction.status == "completed") Color(0xFF10B981) else Color(0xFF6B7280)
            ) {
                Text(
                    transaction.status.replaceFirstChar { it.uppercase() },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(4.dp, 2.dp)
                )
            }
        }
    }
}
