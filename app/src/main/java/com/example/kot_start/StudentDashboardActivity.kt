package com.example.kot_start

import android.content.Context
import android.content.Intent
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.ViewModelProvider
import com.example.kot_start.repository.UserRepoImpl
import com.example.kot_start.viewmodel.StudentViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kot_start.ui.theme.Kot_startTheme
import com.example.kot_start.ui.theme.LightBlue
import com.example.kot_start.ui.theme.greenback

class StudentDashboardActivity : ComponentActivity() {
    
    private lateinit var studentViewModel: StudentViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize ViewModel
        val userRepo = UserRepoImpl()
        studentViewModel = ViewModelProvider(this, object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return StudentViewModel(userRepo) as T
            }
        }).get(StudentViewModel::class.java)
        
        // Load dashboard data
        val currentUser = userRepo.getCurrentUser()
        if (currentUser != null) {
            studentViewModel.loadDashboardData(currentUser.uid)
        }
        
        setContent {
            Kot_startTheme {
                StudentDashboardBody(studentViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardBody(viewModel: StudentViewModel) {
    val context = LocalContext.current
    val studentName = context.getSharedPreferences("User", Context.MODE_PRIVATE).getString("firstName", "Student") ?: "Student"

    // Collect StateFlow values
    val activeTab by viewModel.activeTab.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val pendingBidsCount by viewModel.pendingBidsCount.collectAsState()

    data class NavItem(val title: String, val icon: Int)
    
    var selectedIndex by remember { mutableIntStateOf(0) }
    var showAddCreditsScreen by remember { mutableStateOf(false) }
    val activity = context as? Activity

    if (showAddCreditsScreen) {
        AddCreditsScreen(
            currentBalance = stats.credits,
            onBackClick = { showAddCreditsScreen = false },
            onProceedClick = { amount, method ->
                // Handle payment processing
                showAddCreditsScreen = false
            }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F6F6))
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Safe area padding for status bar
                Spacer(modifier = Modifier.height(8.dp))
                
                // Welcome Message
                Text(
                    text = "Welcome, $studentName",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                // Custom Header with wallet balance from ViewModel
                com.example.kot_start.ui.components.SkillItHeader(
                    walletBalance = "NPR ${String.format("%.0f", stats.credits)}",
                    onNotificationClick = { /* Handle notification */ },
                    onAddCreditsClick = { showAddCreditsScreen = true },
                    onLogoutClick = {
                        viewModel.logoutUser { success, _ ->
                            if (success) {
                                val intent = Intent(context, SkillitLoginActivity::class.java)
                                context.startActivity(intent)
                                if (activity != null) {
                                    activity.finish()
                                }
                            }
                        }
                    }
                )

                // Show loading indicator
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        androidx.compose.material3.CircularProgressIndicator()
                    }
                } else {
                    // Main Content
                    when (selectedIndex) {
                        0 -> StudentHomeScreen(viewModel, onExploreClick = { selectedIndex = 1 }, onSessionsClick = { selectedIndex = 1 }, onContentClick = { selectedIndex = 2 }, onBidsClick = { selectedIndex = 3 })
                        1 -> ExploreScreen(viewModel)
                        2 -> StudentMyLearningScreen(viewModel)
                        3 -> StudentBidsScreen(viewModel)
                        else -> StudentHomeScreen(viewModel, onExploreClick = { selectedIndex = 1 }, onSessionsClick = { selectedIndex = 1 }, onContentClick = { selectedIndex = 2 }, onBidsClick = { selectedIndex = 3 })
                    }
                }
            }

            // Custom Bottom Navigation Pill with Scroll - Safe area padding
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 28.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(4) { index ->
                            val (icon, label) = when (index) {
                                0 -> Pair(R.drawable.baseline_home_24, "HOME")
                                1 -> Pair(R.drawable.baseline_apartment_24, "EXPLORE")
                                2 -> Pair(R.drawable.baseline_home_24, "CONTENT")
                                else -> Pair(R.drawable.baseline_person_24, "BIDS")
                            }

                            NavigationPillItem(
                                icon = icon,
                                label = label,
                                isSelected = selectedIndex == index,
                                onClick = { selectedIndex = index }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationPillItem(
    icon: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            tint = if (isSelected) Color(0xFFEA2A33) else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label.uppercase(),
            style = TextStyle(
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color(0xFFEA2A33) else Color.Gray,
                letterSpacing = 0.5.sp
            )
        )
    }
}

@Composable
fun StudentHomeScreen(
    viewModel: StudentViewModel,
    onExploreClick: () -> Unit = {},
    onSessionsClick: () -> Unit = {},
    onContentClick: () -> Unit = {},
    onBidsClick: () -> Unit = {}
) {
    val stats by viewModel.stats.collectAsState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Navigation Grid Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    com.example.kot_start.ui.components.NavGridItem(
                        title = "Explore",
                        subtitle = "Discover skills",
                        iconPainter = R.drawable.baseline_apartment_24,
                        backgroundColor = Color(0xFF1E88E5),
                        onClick = { onExploreClick() },
                        modifier = Modifier.weight(1f)
                    )
                    com.example.kot_start.ui.components.NavGridItem(
                        title = "Sessions",
                        subtitle = "Booked classes",
                        iconPainter = R.drawable.baseline_devices_24,
                        backgroundColor = Color(0xFFFFA500),
                        onClick = { onSessionsClick() },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    com.example.kot_start.ui.components.NavGridItem(
                        title = "Content",
                        subtitle = "Your learning",
                        iconPainter = R.drawable.baseline_home_24,
                        backgroundColor = Color(0xFFEA2A33),
                        onClick = { onContentClick() },
                        modifier = Modifier.weight(1f)
                    )
                    com.example.kot_start.ui.components.NavGridItem(
                        title = "Bids",
                        subtitle = "Active offers",
                        iconPainter = R.drawable.baseline_person_24,
                        backgroundColor = Color(0xFF9C27B0),
                        onClick = { onBidsClick() },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick Actions Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                com.example.kot_start.ui.components.QuickActionButton(
                    title = "Wallet Balance",
                    icon = R.drawable.baseline_home_24,
                    iconBackgroundColor = Color(0xFF4CAF50),
                    onClick = { }
                )
            }
        }

        // Trending Section
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
                        text = "Trending",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    androidx.compose.material3.TextButton(
                        onClick = { }
                    ) {
                        Text(
                            text = "View all",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEA2A33)
                            )
                        )
                    }
                }

                // Trending Card (Banner)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Gray)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        Color(0xFFEA2A33),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "FLASH SALE",
                                    style = TextStyle(
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Mobile Cinematography",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Join expert Raj Thapa this Sunday",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            )
                        }
                    }
                }
            }
        }

        // Upcoming Sessions Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Upcoming Sessions",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

                com.example.kot_start.ui.components.SessionCard(
                    month = "OCT",
                    day = "24",
                    title = "UI/UX Advanced Principles",
                    time = "10:00 AM - 12:30 PM",
                    onClick = { }
                )

                com.example.kot_start.ui.components.SessionCard(
                    month = "OCT",
                    day = "26",
                    title = "Digital Marketing Strategy",
                    time = "02:00 PM - 04:00 PM",
                    onClick = { }
                )
            }
        }

        // Bottom padding for navigation
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun StudentAppsScreen(viewModel: StudentViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val content by viewModel.content.collectAsState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        item {
            com.example.kot_start.ui.components.SearchBar(
                hint = "Search learning resources...",
                onSearchChange = { }
            )

            com.example.kot_start.ui.components.SectionHeader(
                title = "Learning Resources"
            )
        }

        items(5) { index ->
            com.example.kot_start.ui.components.AssignmentCard(
                title = listOf(
                    "Video Tutorial: Basics",
                    "PDF: Advanced Concepts",
                    "Code Examples",
                    "Quiz: Chapter 1",
                    "Assignment: Build App"
                )[index],
                subject = "Android Development",
                dueDate = listOf(
                    "Available now",
                    "Available now",
                    "Available now",
                    "Due: Mar 5",
                    "Due: Mar 10"
                )[index],
                status = "pending",
                onCardClick = { }
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun StudentDevicesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5))
    ) {
        com.example.kot_start.ui.components.SectionHeader(
            title = "Your Devices"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Currently no devices added
        com.example.kot_start.ui.components.EmptyState(
            title = "No devices added",
            description = "Connect your devices to access learning materials offline",
            actionText = "Add Device",
            onActionClick = { }
        )
    }
}

@Composable
fun StudentProfileScreen(activity: ComponentActivity, viewModel: StudentViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5))
    ) {
        item {
            com.example.kot_start.ui.components.SectionHeader(
                title = "Profile Settings"
            )

            Spacer(modifier = Modifier.height(16.dp))

            com.example.kot_start.ui.components.QuickActionCard(
                title = "Edit Profile",
                description = "Update your personal information",
                backgroundColor = LightBlue,
                onCardClick = { }
            )

            com.example.kot_start.ui.components.QuickActionCard(
                title = "Change Password",
                description = "Update your account security",
                backgroundColor = Color(0xFFFF6B6B),
                onCardClick = { }
            )

            com.example.kot_start.ui.components.QuickActionCard(
                title = "Notification Settings",
                description = "Manage your preferences",
                backgroundColor = greenback,
                onCardClick = { }
            )

            com.example.kot_start.ui.components.QuickActionCard(
                title = "Logout",
                description = "Sign out from your account",
                backgroundColor = Color.Gray,
                onCardClick = {
                    // Logout through ViewModel
                    viewModel.logoutUser { success, message ->
                        if (success) {
                            val intent = Intent(activity, SkillitLoginActivity::class.java)
                            activity.startActivity(intent)
                            activity.finish()
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Preview
@Composable
fun StudentDashboardPreview() {
    // Note: This preview requires a StudentViewModel to be provided
    // For preview purposes, consider creating a mock or test instance
}
