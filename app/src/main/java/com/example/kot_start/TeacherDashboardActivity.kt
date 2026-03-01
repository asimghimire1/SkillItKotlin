package com.example.kot_start

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.vector.ImageVector

class TeacherDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent { TeacherDashboardScreen() }
    }
}


@Composable
fun TeacherDashboardScreen() {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var showLogoutMenu by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf("dashboard") }  // "dashboard", "session", "content"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
    ) {
        if (currentScreen == "dashboard") {
            DashboardContent(showLogoutMenu) { showLogoutMenu = it }
        } else if (currentScreen == "session") {
            ScheduleSessionScreen(onBackClick = { currentScreen = "dashboard" })
        } else if (currentScreen == "content") {
            UploadContentScreen(onBackClick = { currentScreen = "dashboard" })
        } else if (currentScreen == "learning") {
            TeacherContentScreen(onBackClick = { currentScreen = "dashboard" })
        } else if (currentScreen == "bids") {
            TeacherBidsScreen(onBackClick = { currentScreen = "dashboard" })
        } else if (currentScreen == "earnings") {
            TeacherEarningsScreen(onBackClick = { currentScreen = "dashboard" })
        }

        // Floating Bottom Navigation
        if (currentScreen == "dashboard") {
            TeacherFloatingBottomNav(selectedTab, { selectedTab = it }, { currentScreen = it })
        }
    }
}

@Composable
private fun DashboardContent(
    showLogoutMenu: Boolean,
    onLogoutMenuChange: (Boolean) -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .paddingFromBaseline(top = 80.dp)
            .paddingFromBaseline(bottom = 100.dp)
    ) {
            // Main Content
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 10.dp)
                ) {
                    // Quick Action Grid
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionCard(
                            title = "Explore",
                            description = "View peers & trends",
                            icon = Icons.Default.Explore,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionCard(
                            title = "Sessions",
                            description = "Upcoming classes",
                            icon = Icons.Default.Event,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionCard(
                            title = "Learning",
                            description = "Manage curriculum",
                            icon = Icons.Default.MenuBook,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionCard(
                            title = "Bids",
                            description = "Student requests",
                            icon = Icons.Default.LocalOffer,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Withdraw Button
            item {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEA2A33)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = "Withdraw",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            "Withdraw Earnings ($4,280.00)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Engagement Insights
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Engagement Insights",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            "View Report",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEA2A33),
                            modifier = Modifier.clickable { }
                        )
                    }

                    // Featured Workshop Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(256.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.Black)
                    ) {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1552664730-d307ca884978?w=500&h=500&fit=crop",
                            contentDescription = "Analytics Dashboard",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.8f)
                                        ),
                                        startY = 100f
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(24.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(bottom = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .padding(0.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = Color(0xFFEA2A33)
                                ) {
                                    Text(
                                        "TOP PERFORMING",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }

                                Surface(
                                    modifier = Modifier
                                        .padding(0.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        "LIVE NOW",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }
                            }

                            Text(
                                "Advanced UI Dynamics",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                "Your course reach increased by 24% this week. 120 students active.",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // Stats Overview
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    item {
                        StatCard(
                            label = "Total Students",
                            value = "1,402",
                            change = "+12%",
                            changeColor = Color(0xFF22C55E)
                        )
                    }
                    item {
                        StatCard(
                            label = "Course Rating",
                            value = "4.9/5.0",
                            change = "(240 reviews)",
                            changeColor = Color(0xFFEA2A33)
                        )
                    }
                    item {
                        StatCard(
                            label = "Active Hours",
                            value = "86 hrs",
                            change = "This month",
                            changeColor = Color(0xFF3B82F6)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Fixed Header
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding(),
            color = Color.White.copy(alpha = 0.8f),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE5E7EB))
                            .border(2.dp, Color(0xFFEA2A33), CircleShape)
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDHTc_DO2lNzcz9nlqubumpcTwFrtlT3KhDZiUJPQAu41bzCcpMmmGycER6Wf64lcjwNdmnGkDtdXswSYnx43f7vQ56pgEWjx1dEgYritoJK3AWGziRyepDSP67jX-pXcNaq5JmAcroKMZ6LIXLMa7mOfT_cnVdUhMv984EDZK9YvICoLt5yR0Cs77EZ8ULgy6D1tLxxKJ5qqImVg-jcJlzxomOEoPohs24bkX2LLt193375N_iO-P0zl0eOttHVf8ELtV0FJglbOiw",
                            contentDescription = "Professor Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column {
                        Text(
                            "Welcome back,",
                            fontSize = 11.sp,
                            color = Color(0xFF6B7280),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Professor Sarah",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            lineHeight = 22.sp
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF4B5563),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box {
                        IconButton(
                            onClick = { onLogoutMenuChange(!showLogoutMenu) },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color(0xFF4B5563),
                                modifier = Modifier.size(20.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEA2A33))
                            )
                        }

                        DropdownMenu(
                            expanded = showLogoutMenu,
                            onDismissRequest = { onLogoutMenuChange(false) },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Logout",
                                        fontSize = 14.sp,
                                        color = Color(0xFFEA2A33),
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                onClick = {
                                    onLogoutMenuChange(false)
                                    // Logout logic here
                                    val intent = Intent(context, SkillitLoginActivity::class.java)
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.TeacherFloatingBottomNav(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onAddClick: (String) -> Unit
) {
    var showAddMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
            .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            color = Color.White.copy(alpha = 0.9f),
            shape = RoundedCornerShape(50.dp),
            shadowElevation = 16.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItem(
                    icon = Icons.Default.Home,
                    label = "Home",
                    selected = selectedTab == 0,
                    onClick = { onTabSelected(0) }
                )

                NavItem(
                    icon = Icons.Default.LocalOffer,
                    label = "Bids",
                    selected = selectedTab == 1,
                    onClick = { onTabSelected(1) }
                )

                Box(modifier = Modifier.width(60.dp)) {
                    Box {
                        FloatingActionButton(
                            onClick = { showAddMenu = !showAddMenu },
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .size(64.dp),
                            containerColor = Color(0xFFEA2A33),
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showAddMenu,
                            onDismissRequest = { showAddMenu = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Schedule Session",
                                        fontSize = 14.sp,
                                        color = Color(0xFFEA2A33),
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                onClick = {
                                    showAddMenu = false
                                    onAddClick("session")
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Create Content",
                                        fontSize = 14.sp,
                                        color = Color(0xFFEA2A33),
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                onClick = {
                                    showAddMenu = false
                                    onAddClick("content")
                                }
                            )
                        }
                    }
                }

                NavItem(
                    icon = Icons.Default.Event,
                    label = "Sessions",
                    selected = selectedTab == 3,
                    onClick = { onTabSelected(3) }
                )

                NavItem(
                    icon = Icons.Default.AccountBalanceWallet,
                    label = "Earnings",
                    selected = selectedTab == 4,
                    onClick = { onTabSelected(4) }
                )
            }
        }
    }
}

@Composable
private fun ScheduleSessionScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var sessionTitle by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Design & Creative") }
    var difficulty by remember { mutableStateOf("Beginner") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var meetingLink by remember { mutableStateOf("") }
    var isPaid by remember { mutableStateOf(true) }
    var price by remember { mutableStateOf("29.99") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .paddingFromBaseline(top = 70.dp)
                .paddingFromBaseline(bottom = 100.dp)
        ) {
            // Thumbnail Upload Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 24.dp)
                ) {
                    Text(
                        "SESSION COVER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA0A0A0),
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .border(
                                2.dp,
                                Color(0xFFEA2A33).copy(alpha = 0.2f),
                                RoundedCornerShape(16.dp)
                            )
                            .background(Color(0xFFEA2A33).copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = "Upload",
                                tint = Color(0xFFEA2A33),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Upload high-res thumbnail",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF6B7280)
                            )
                            Text(
                                "16:9 ratio recommended (JPG, PNG)",
                                fontSize = 11.sp,
                                color = Color(0xFFBDBDBD),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // General Information Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp)
                ) {
                    Text(
                        "GENERAL INFORMATION",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA0A0A0),
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Session Title
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(
                            "Session Title",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        TextField(
                            value = sessionTitle,
                            onValueChange = { sessionTitle = it },
                            placeholder = { Text("e.g. Masterclass in Brand Typography") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = androidx.compose.material3.LocalTextStyle.current.copy(fontSize = 14.sp)
                        )
                    }

                    // Category & Difficulty
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Category",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF374151),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            SelectDropdown(
                                options = listOf("Design & Creative", "Technology", "Business", "Lifestyle"),
                                selectedOption = category,
                                onOptionSelected = { category = it }
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Difficulty",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF374151),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            SelectDropdown(
                                options = listOf("Beginner", "Intermediate", "Advanced"),
                                selectedOption = difficulty,
                                onOptionSelected = { difficulty = it }
                            )
                        }
                    }

                    // Description
                    Column {
                        Text(
                            "Description",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        TextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = { Text("What will students learn in this session?") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }

            // Date & Schedule Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp)
                ) {
                    Text(
                        "DATE & SCHEDULE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA0A0A0),
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Select Date",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF374151),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            TextField(
                                value = selectedDate,
                                onValueChange = { selectedDate = it },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Date",
                                        tint = Color(0xFFEA2A33),
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.White,
                                    focusedContainerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                placeholder = { Text("mm/dd/yyyy", fontSize = 12.sp) }
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Start Time",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF374151),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            TextField(
                                value = startTime,
                                onValueChange = { startTime = it },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = "Time",
                                        tint = Color(0xFFEA2A33),
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.White,
                                    focusedContainerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                placeholder = { Text("--:-- --", fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }

            // Meeting Link Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp)
                ) {
                    Text(
                        "MEETING PLATFORM",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA0A0A0),
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        surface(
                            modifier = Modifier
                                .size(32.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFEBF5FF)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VideoCall,
                                contentDescription = "Video",
                                tint = Color(0xFF2563EB),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp)
                            )
                        }

                        TextField(
                            value = meetingLink,
                            onValueChange = { meetingLink = it },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            shape = RoundedCornerShape(0.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            placeholder = { Text("Paste Zoom or Google Meet link", fontSize = 12.sp) }
                        )
                    }

                    Text(
                        "Students will receive this link 15 mins before start.",
                        fontSize = 10.sp,
                        color = Color(0xFF9CA3AF),
                        modifier = Modifier.padding(top = 8.dp),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            // Pricing Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(24.dp)
                        .border(1.dp, Color(0xFFEA2A33).copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                        .background(Color(0xFFEA2A33).copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Session Pricing",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                "Choose if this is a free or paid workshop",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(50.dp))
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            Button(
                                onClick = { isPaid = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(32.dp),
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isPaid) Color(0xFFEA2A33) else Color.Transparent
                                )
                            ) {
                                Text(
                                    "Paid",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPaid) Color.White else Color(0xFFA0A0A0)
                                )
                            }
                            Button(
                                onClick = { isPaid = false },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(32.dp),
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!isPaid) Color(0xFFEA2A33) else Color.Transparent
                                )
                            ) {
                                Text(
                                    "Free",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!isPaid) Color.White else Color(0xFFA0A0A0)
                                )
                            }
                        }
                    }

                    if (isPaid) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("$", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9CA3AF))
                            TextField(
                                value = price,
                                onValueChange = { price = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                shape = RoundedCornerShape(0.dp),
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                textStyle = androidx.compose.material3.LocalTextStyle.current.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text("per attendee", fontSize = 11.sp, color = Color(0xFF9CA3AF))
                        }
                    }
                }
            }

            // Schedule Button
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 16.dp)
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(2.dp, RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEA2A33)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Schedule Session",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Schedule",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Header
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding(),
            color = Color.White.copy(alpha = 0.8f),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFFEA2A33),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    "Schedule New Session",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        "Drafts",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFEA2A33)
                    )
                }
            }
        }
    }
}

@Composable
private fun UploadContentScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var videoTitle by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isPaid by remember { mutableStateOf(false) }
    var price by remember { mutableStateOf("999") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .paddingFromBaseline(top = 70.dp)
                .paddingFromBaseline(bottom = 80.dp)
        ) {
            // Video Upload Zone
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(top = 8.dp, bottom = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .border(
                                2.dp,
                                Color(0xFFD1D5DB),
                                RoundedCornerShape(12.dp)
                            )
                            .background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(
                                        Color(0xFFEA2A33).copy(alpha = 0.1f),
                                        RoundedCornerShape(32.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Movie,
                                    contentDescription = "Video",
                                    tint = Color(0xFFEA2A33),
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Select video file to upload",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Text(
                                "MP4, MOV or AVI (Max. 500MB)",
                                fontSize = 10.sp,
                                color = Color(0xFF9CA3AF),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Thumbnail Preview Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp)
                ) {
                    Text(
                        "Thumbnail Preview",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Thumbnail Preview Box
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF3F4F6))
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBzOcOi5ZO8m8o4VgSEyOAck80hwFmCmlXbewXqaWROi6qJLbgSCQ1DsDmF6fZ7_CooaOBGkhn59-vYVd6s1flL1sa67fzu3swlkDP0245wgG22u3dkGWoWL3P2ax-xkT4ZBxK5LIADty3CSP0rGB8Hi_ahvYIoxq6bBPc842UiOovSTN0QgJDuWxw4iQ0nzWYvgBhX0ugJ8Kzbj7S2ZdNqmVOkIFAo4hr87blNe-2jhd7CiRORdGruOZBTSYIT-b992mX1papIl5K_",
                                contentDescription = "Thumbnail",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .alpha(0.6f)
                            )
                        }

                        // Change Button Column
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                border = BorderStroke(1.dp, Color(0xFFEA2A33)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = "Change",
                                        tint = Color(0xFFEA2A33),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        "Change Thumbnail",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFEA2A33)
                                    )
                                }
                            }

                            Text(
                                "High-resolution images work best (1280x720).",
                                fontSize = 9.sp,
                                color = Color(0xFFBDBDBD)
                            )
                        }
                    }
                }
            }

            // Form Fields
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp)
                        .gap(16.dp)
                ) {
                    // Video Title
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Video Title",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        TextField(
                            value = videoTitle,
                            onValueChange = { videoTitle = it },
                            placeholder = { Text("e.g. Advanced UI Design Masterclass") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }

                    // Category
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Category",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        SelectDropdown(
                            options = listOf(
                                "Graphic Design",
                                "Web Development",
                                "Digital Marketing",
                                "Business Strategy"
                            ),
                            selectedOption = category.ifEmpty { "Select a category" },
                            onOptionSelected = { category = it }
                        )
                    }

                    // Description
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Description",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        TextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = { Text("Briefly describe what students will learn in this video...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }

            // Pricing Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp, bottom = 24.dp)
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        "Pricing Plan",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF374151),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Pricing Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(Color(0xFFF3F4F6), RoundedCornerShape(10.dp))
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        PricingToggleButton(
                            label = "Free",
                            isSelected = !isPaid,
                            onClick = { isPaid = false },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                        PricingToggleButton(
                            label = "Paid",
                            isSelected = isPaid,
                            onClick = { isPaid = true },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }

                    // Price Input (shown only when Paid is selected)
                    if (isPaid) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text(
                                "Price (NPR)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF374151),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .background(Color.White, RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    "Rs.",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF9CA3AF)
                                )
                                TextField(
                                    value = price,
                                    onValueChange = { price = it },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp),
                                    shape = RoundedCornerShape(0.dp),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    textStyle = androidx.compose.material3.LocalTextStyle.current.copy(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    placeholder = { Text("999", fontSize = 12.sp) }
                                )
                            }
                        }
                    }
                }
            }

            // Publish Button
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 16.dp)
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(2.dp, RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEA2A33)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Publish",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                "Publish Content",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Text(
                        "By publishing, you agree to SkillIt's Teacher Terms & Conditions.",
                        fontSize = 9.sp,
                        color = Color(0xFFBDBDBD),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Header
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding(),
            color = Color.White.copy(alpha = 0.8f),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    "Upload New Content",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        // Bottom Navigation Bar
        ContentBottomNavBar()
    }
}

@Composable
private fun PricingToggleButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.White else Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color(0xFFEA2A33) else Color(0xFF9CA3AF)
        )
    }
}

@Composable
private fun BoxScope.ContentBottomNavBar() {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ContentNavItem(
                    icon = Icons.Default.Home,
                    label = "Home",
                    onClick = { }
                )

                ContentNavItem(
                    icon = Icons.Default.Search,
                    label = "Search",
                    onClick = { }
                )

                Box(modifier = Modifier.width(60.dp)) {
                    FloatingActionButton(
                        onClick = { },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .size(56.dp),
                        containerColor = Color(0xFFEA2A33),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                ContentNavItem(
                    icon = Icons.Default.MenuBook,
                    label = "Courses",
                    onClick = { }
                )

                ContentNavItem(
                    icon = Icons.Default.Person,
                    label = "Profile",
                    onClick = { }
                )
            }
        }
    }
}

@Composable
private fun ContentNavItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF9CA3AF),
            modifier = Modifier.size(24.dp)
        )
        Text(
            label,
            fontSize = 9.sp,
            color = Color(0xFF9CA3AF),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun TeacherContentScreen(onBackClick: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = My Courses, 1 = Live Sessions

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .paddingFromBaseline(top = 80.dp)
                .paddingFromBaseline(bottom = 100.dp)
        ) {
            // Tabs
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    TabButton(
                        label = "My Courses",
                        isActive = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    TabButton(
                        label = "Live Sessions",
                        isActive = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                }
            }

            // Content Section
            if (selectedTab == 0) {
                // My Courses Tab
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Active Content",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.White, RoundedCornerShape(8.dp))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = "Filter",
                                        tint = Color(0xFF6B7280),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.White, RoundedCornerShape(8.dp))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = Color(0xFF6B7280),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Course Cards
                items(2) { index ->
                    val course = listOf(
                        Triple("Advanced UI Design Mastery", "1,280 Students", "$12,450 Revenue"),
                        Triple("Building Scalable Design Systems", "84 Students", "$2,100 Revenue")
                    )[index]

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Thumbnail
                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF3F4F6))
                                ) {
                                    AsyncImage(
                                        model = if (index == 0) {
                                            "https://lh3.googleusercontent.com/aida-public/AB6AXuBNeqw3QwqomdN87tc5UH1DX_7PsFs4nBnxVZ_HGQ58fxc8mWs5OzHjb23zspLVtKbGnWm2KkyQYlOhqEq7TFm9CLWndGHTAd4mRFS0tEBUP4oIaBTifnju-eZtCGv1YurryE_HRn2QrXS-NWOzn9tQMrHWyJWgZVti4yoqHdA0hlI_JDJQijtpBvlRB8mYi93zUMhC6SUjeV7BcCLmBoql_9woZUqDgUZLcJkzf4lqkcH4u2dTHSXZl6EiSCjXDQJViAcVtSJwm3-I"
                                        } else {
                                            "https://lh3.googleusercontent.com/aida-public/AB6AXuDULIDOOO68IZjoJFhrqK5rnstYPv6bcpdCZt_zY7odknbSbM1RlQQHPWMN4ChfzKyGegQjtBrPHWUe185CdICXk1N3CZu47v1yNcYrM9pY4CUn3J6l-y_pPhJZBhchmb8XbLJYubSJhNHc5eXIMem8zEwN5BqpXWIFshROJJy_Iv-4XPMGwarZ4fPEjafz5dYV_fjE0RCmNwindby00_k8sz7Ui_OUBYB7cOU4YX8OlGSdUne7FSn9NHSKDY5gzpMz_DH0r0SnjBcx"
                                        },
                                        contentDescription = "Course",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                // Content Info
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Text(
                                                course.first,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Surface(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(20.dp)),
                                                color = if (index == 0) Color(0xFFDCFCE7) else Color(0xFFDEE2E6)
                                            ) {
                                                Text(
                                                    if (index == 0) "Published" else "In Review",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (index == 0) Color(0xFF059669) else Color(0xFF4B5563),
                                                    modifier = Modifier.padding(
                                                        horizontal = 8.dp,
                                                        vertical = 4.dp
                                                    )
                                                )
                                            }
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        ) {
                                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Icon(
                                                    imageVector = Icons.Default.Group,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(14.sp.value.dp),
                                                    tint = Color(0xFF6B7280)
                                                )
                                                Text(
                                                    course.second,
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF6B7280)
                                                )
                                            }
                                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Icon(
                                                    imageVector = Icons.Default.Payment,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(14.sp.value.dp),
                                                    tint = Color(0xFF6B7280)
                                                )
                                                Text(
                                                    course.third,
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF6B7280)
                                                )
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(36.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFEA2A33)
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                if (index == 0) "Edit Content" else "Manage Access",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }

                                        IconButton(
                                            onClick = { },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.MoreHoriz,
                                                contentDescription = "More",
                                                tint = Color(0xFF6B7280),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Live Sessions Tab
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 20.dp)
                    ) {
                        Text(
                            "Upcoming Sessions",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Session 1 - Starting soon
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            "STARTS IN 15 MINS",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFEA2A33),
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            "Weekly Q&A: Advanced Layouts",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                        Text(
                                            "42 Students registered",
                                            fontSize = 12.sp,
                                            color = Color(0xFF6B7280),
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }

                                    Button(
                                        onClick = { },
                                        modifier = Modifier
                                            .height(40.dp)
                                            .padding(start = 8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFEA2A33)
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PlayArrow,
                                                contentDescription = "Start",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                "Start Live",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Session 2 - Scheduled
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                                .alpha(0.7f),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            "TOMORROW, 10:00 AM",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF6B7280),
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            "Portfolio Review Session",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF374151),
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                        Text(
                                            "12 Students registered",
                                            fontSize = 12.sp,
                                            color = Color(0xFF6B7280),
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }

                                    Surface(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFE5E7EB)),
                                        color = Color(0xFFE5E7EB)
                                    ) {
                                        Text(
                                            "Scheduled",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF6B7280),
                                            modifier = Modifier.padding(
                                                horizontal = 12.dp,
                                                vertical = 8.dp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Header
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding(),
            color = Color.White.copy(alpha = 0.8f),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEA2A33).copy(alpha = 0.1f))
                            .border(1.dp, Color(0xFFEA2A33).copy(alpha = 0.2f), CircleShape)
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDiUWMeHmcwauKcE6tB0XrUtD64Pmu79knpUlGyQwLi3G_9gXV-jzVMJ94XjOo2XzrgeC31W16G2DaOpUOXj0EXtoxXgS8ny5hykxMZgQM6qQulQG6AX5RIj_a5mXQdScex-xh_z20qv7mTQAp7TvQsz4z1tbwRt-diWibPZUN-GwgSU874IhoZCnyffSLn5RYLGdPFdPFgwAc4v4fYwBAVn7zHt9aoSRgFqLgDR8GSLb_sMKe1w82X7MbFn4TF5DWoaGmIVmBrCVwY",
                            contentDescription = "Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column {
                        Text(
                            "Teacher Dashboard",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            "Welcome back, Sarah",
                            fontSize = 10.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Bottom Navigation Bar
        ContentTeacherBottomNavBar()
    }
}

@Composable
private fun TabButton(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(bottom = 12.dp)
    ) {
        Text(
            label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isActive) Color(0xFFEA2A33) else Color(0xFF9CA3AF)
        )
        if (isActive) {
            Divider(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .height(3.dp)
                    .background(Color(0xFFEA2A33))
                    .width(label.length * 6.dp)
            )
        }
    }
}

@Composable
private fun BoxScope.ContentTeacherBottomNavBar() {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
            .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp)),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TeacherContentNavItem(
                    icon = Icons.Default.GridView,
                    label = "Dashboard",
                    isActive = true,
                    onClick = { }
                )

                TeacherContentNavItem(
                    icon = Icons.Default.MenuBook,
                    label = "Courses",
                    isActive = false,
                    onClick = { }
                )

                Box(modifier = Modifier.width(60.dp))

                TeacherContentNavItem(
                    icon = Icons.Default.BarChart,
                    label = "Stats",
                    isActive = false,
                    onClick = { }
                )

                TeacherContentNavItem(
                    icon = Icons.Default.Settings,
                    label = "Settings",
                    isActive = false,
                    onClick = { }
                )
            }
        }

        // Centered Add Button
        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(56.dp),
            containerColor = Color(0xFFEA2A33),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun TeacherContentNavItem(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isActive) Color(0xFFEA2A33) else Color(0xFF9CA3AF),
            modifier = Modifier.size(24.dp)
        )
        Text(
            label,
            fontSize = 9.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) Color(0xFFEA2A33) else Color(0xFF9CA3AF),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun TeacherBidsScreen(onBackClick: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Active, 1 = Pending, 2 = Completed
    var showCounterSheet by remember { mutableStateOf(false) }
    var selectedBidForCounter by remember { mutableStateOf<Pair<String, String>?>(null) }
    var counterPrice by remember { mutableStateOf(54f) }

    val bids = listOf(
        Triple("Advanced Calculus II", "Alex Johnson", Triple("$60.00", "$48.00", "URGENT")),
        Triple("Organic Chemistry Basics", "Sarah Mitchell", Triple("$45.00", "$40.00", "STANDARD")),
        Triple("Intro to Microeconomics", "David Chen", Triple("$35.00", "$32.50", "URGENT"))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .paddingFromBaseline(top = 80.dp)
                .paddingFromBaseline(bottom = 80.dp)
        ) {
            // Tabs
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color(0xFFEA2A33).copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TabButtonBid(
                        label = "Active (8)",
                        isActive = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    TabButtonBid(
                        label = "Pending",
                        isActive = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.weight(1f)
                    )
                    TabButtonBid(
                        label = "Completed",
                        isActive = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Bid Cards
            items(bids.size) { index ->
                val bid = bids[index]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Student Profile and Course Info
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF3F4F6))
                                ) {
                                    AsyncImage(
                                        model = when (index) {
                                            0 -> "https://lh3.googleusercontent.com/aida-public/AB6AXuDm6-EW8ou64k47Yki4j-S5snNWn8zJjSYfHlVqwJkfuqQ-vpZjZFCskOt2xlJ-MmmN6wt2a0MbcG6DKg99Di9g3VDqMJXXSLVYx-qlv_vxqR6Mn4QEDYHsblrNwBQTGPmJsrlM13IL1jIS0rI3LRiDADkkqSogh7PJ6zABTVzFlU4awcjozhsPv3E1aKuHxkopG5Bkp4gHt24kPPjGG7U9GGm2rgUnAYtyzVqGEDMQNTzRRhyEtKMmWJBYZ-CLqRJPkXT2H4eLhy7l"
                                            1 -> "https://lh3.googleusercontent.com/aida-public/AB6AXuCyrw1Y7zzJ-21w20vxZG1vBCTZ59TQl8pxz5Y0sDFmMqdXZgKVmP45Y-lracQ-5lJrvVRL9jkooFSGlGWadhNtMtywu9ktAK0Hx_PoBp6kqrDBGtvewzlVJBVh2029dPCFs02dygoiVy-LZZJOR-Op9Sii7RuPsdwX3fiT9eVzCBUzagQ8m-sgu3IJ4eyYeDN6BjbiaOXnV5psw3lp45YTvD3owMzcedx8t5z8GTT-VfHYMldsGTiScRaJAw25E-3ahjIt6EWIDM8C"
                                            else -> "https://lh3.googleusercontent.com/aida-public/AB6AXuAZNjIyIqTXv1WThsXHAYAKyYNWH2U2wOfSJcXiNMTwS2dXgcSVNSvDAd64GU8RxQ2dyUGgyKfcugFGFAtkFNyVgsEanW6iS3nWVoP0IvGrqBMWQlx4MKSQStdDIh_wLFEkVzJ3kwleOlwfiqWSXa-u5vsU5rs4_d2lTdJoTxAbxecXN4yGKFEap-_cKRFFtxLaTkhLlpVjmn-2uatBvaVIzfkvYwl2PqrS-Y1FBVFc4WNZwdfi8QEhj5SEEmCjjJhTgUCUPMS-seM7"
                                        },
                                        contentDescription = "Student",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(
                                            bid.first,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Surface(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(20.dp)),
                                            color = if (bid.third.third == "URGENT") {
                                                Color(0xFFEA2A33).copy(alpha = 0.1f)
                                            } else {
                                                Color(0xFFF3F4F6)
                                            }
                                        ) {
                                            Text(
                                                bid.third.third,
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (bid.third.third == "URGENT") {
                                                    Color(0xFFEA2A33)
                                                } else {
                                                    Color(0xFF6B7280)
                                                },
                                                modifier = Modifier.padding(
                                                    horizontal = 8.dp,
                                                    vertical = 4.dp
                                                )
                                            )
                                        }
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp),
                                            tint = Color(0xFF6B7280)
                                        )
                                        Text(
                                            bid.second,
                                            fontSize = 12.sp,
                                            color = Color(0xFF6B7280)
                                        )
                                    }
                                }
                            }

                            // Price Display
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF8F6F6), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "ORIGINAL PRICE",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF9CA3AF),
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        bid.third.first,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF6B7280),
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }

                                Divider(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(32.dp)
                                        .background(Color(0xFFE5E7EB))
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "STUDENT OFFER",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFEA2A33),
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        bid.third.second,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFEA2A33),
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }

                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = {
                                        selectedBidForCounter = Pair(bid.first, bid.second)
                                        counterPrice = bid.third.second.replace("$", "").toFloatOrNull() ?: 48f
                                        showCounterSheet = true
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF3F4F6)
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        "Counter Offer",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF374151)
                                    )
                                }

                                Button(
                                    onClick = { },
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .height(44.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFEA2A33)
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Accept",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            "Accept Bid",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Header
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding(),
            color = Color.White.copy(alpha = 0.8f),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFEA2A33), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Bids",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        "Bid Requests",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Box {
                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color(0xFF6B7280),
                            modifier = Modifier.size(20.dp)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(8.dp)
                                .background(Color(0xFFEA2A33), CircleShape)
                        )
                    }
                }
            }
        }

        // Counter Offer Sheet
        if (showCounterSheet && selectedBidForCounter != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(enabled = true) { showCounterSheet = false }
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White,
                shadowElevation = 16.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Handle Bar
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(48.dp)
                            .height(4.dp)
                            .background(Color(0xFFE5E7EB), RoundedCornerShape(2.dp))
                            .padding(bottom = 16.dp)
                    )

                    Text(
                        "Make a Counter Offer",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        "Set your price for ${selectedBidForCounter?.first}",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Price Display
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .background(Color(0xFFEA2A33).copy(alpha = 0.1f), RoundedCornerShape(24.dp))
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            "$${"%.2f".format(counterPrice)}",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFEA2A33)
                        )
                    }

                    // Slider
                    Column(
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Slider(
                            value = counterPrice,
                            onValueChange = { counterPrice = it },
                            valueRange = 40f..60f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFEA2A33),
                                activeTrackColor = Color(0xFFEA2A33),
                                inactiveTrackColor = Color(0xFFE5E7EB)
                            )
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Min $40.00",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF9CA3AF)
                            )
                            Text(
                                "Max $60.00",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                    }

                    // Fee Breakdown
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "YOU RECEIVE",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9CA3AF),
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    "$${"%.2f".format(counterPrice * 0.85f)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "PLATFORM FEE",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9CA3AF),
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    "$${"%.2f".format(counterPrice * 0.15f)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9CA3AF),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }

                    // Action Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextButton(
                            onClick = { showCounterSheet = false },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                "Cancel",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF9CA3AF)
                            )
                        }

                        Button(
                            onClick = { showCounterSheet = false },
                            modifier = Modifier
                                .weight(2f)
                                .height(44.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEA2A33)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                "Send Counter Offer",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Bottom Navigation Bar
        TeacherBidsBottomNavBar()
    }
}

@Composable
private fun TabButtonBid(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) Color.White else Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isActive) Color(0xFFEA2A33) else Color(0xFF9CA3AF)
        )
    }
}

@Composable
private fun BoxScope.TeacherBidsBottomNavBar() {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
            .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp)),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TeacherBidsNavItem(
                    icon = Icons.Default.Home,
                    label = "Home",
                    isActive = true,
                    onClick = { }
                )

                TeacherBidsNavItem(
                    icon = Icons.Default.ChatBubble,
                    label = "Messages",
                    isActive = false,
                    onClick = { }
                )

                Box(modifier = Modifier.width(60.dp))

                TeacherBidsNavItem(
                    icon = Icons.Default.Payment,
                    label = "Earnings",
                    isActive = false,
                    onClick = { }
                )

                TeacherBidsNavItem(
                    icon = Icons.Default.AccountCircle,
                    label = "Profile",
                    isActive = false,
                    onClick = { }
                )
            }
        }

        // Centered Add Button
        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(56.dp),
            containerColor = Color(0xFFEA2A33),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun TeacherBidsNavItem(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isActive) Color(0xFFEA2A33) else Color(0xFF9CA3AF),
            modifier = Modifier.size(24.dp)
        )
        Text(
            label,
            fontSize = 9.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) Color(0xFFEA2A33) else Color(0xFF9CA3AF),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun TeacherEarningsScreen(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .paddingFromBaseline(top = 70.dp)
                .paddingFromBaseline(bottom = 80.dp)
        ) {
            // Earnings Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(top = 8.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Text(
                                "Total Available Balance",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280),
                                fontWeight = FontWeight.Medium
                            )

                            Text(
                                "$12,450.80",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFEA2A33)
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Payment,
                                            contentDescription = "Withdraw",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            "Withdraw Now",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Button(
                                    onClick = { },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF3F4F6)
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.History,
                                            contentDescription = "History",
                                            tint = Color(0xFF6B7280),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            "History",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF374151)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Income Breakdown
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        "Income Breakdown",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Courses Card
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(Color(0xFFEA2A33), CircleShape)
                                    )
                                    Text(
                                        "COURSES",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF9CA3AF),
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Text(
                                    "$8,240",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingUp,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = Color(0xFF10B981)
                                    )
                                    Text(
                                        "+14%",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF10B981)
                                    )
                                }
                            }
                        }

                        // Live Sessions Card
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(Color(0xFF9CA3AF), CircleShape)
                                    )
                                    Text(
                                        "LIVE SESSIONS",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF9CA3AF),
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Text(
                                    "$4,210",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingUp,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = Color(0xFF10B981)
                                    )
                                    Text(
                                        "+8%",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF10B981)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Weekly Chart
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                val heights = listOf(0.65f, 0.80f, 0.45f, 0.90f, 0.70f)
                                val actual = listOf(0.40f, 0.55f, 0.30f, 0.75f, 0.50f)

                                repeat(5) { index ->
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        verticalArrangement = Arrangement.Bottom
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(heights[index])
                                                .background(Color(0xFFEA2A33).copy(alpha = 0.15f), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxHeight(actual[index] / heights[index])
                                                    .background(Color(0xFFEA2A33), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                    .align(Alignment.BottomCenter)
                                            )
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                listOf("MON", "TUE", "WED", "THU", "FRI").forEach { day ->
                                    Text(
                                        day,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF9CA3AF),
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Recent Payouts
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Recent Payouts",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Text(
                            "View All",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEA2A33),
                            modifier = Modifier.clickable { }
                        )
                    }
                }
            }

            // Transaction Items
            items(3) { index ->
                val transaction = listOf(
                    Triple("Withdrawal to Bank", "Oct 24, 2023 • 10:30 AM", Triple("-$1,200.00", "Completed", Icons.Default.AccountBalanceWallet)),
                    Triple("Course Sale: UI/UX Masterclass", "Oct 22, 2023 • 02:15 PM", Triple("+$450.00", "Processing", Icons.Default.AutoStories)),
                    Triple("Withdrawal to Bank", "Oct 18, 2023 • 09:00 AM", Triple("-$2,500.00", "Completed", Icons.Default.AccountBalanceWallet))
                )[index]

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            if (index == 1) Color(0xFFDBEAFE) else Color(0xFFDCFCE7),
                                            RoundedCornerShape(50.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = transaction.third.third,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = if (index == 1) Color(0xFF2563EB) else Color(0xFF059669)
                                    )
                                }

                                Column {
                                    Text(
                                        transaction.first,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )

                                    Text(
                                        transaction.second,
                                        fontSize = 10.sp,
                                        color = Color(0xFF9CA3AF),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }

                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.padding(start = 12.dp)
                            ) {
                                Text(
                                    transaction.third.first,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                                Surface(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .clip(RoundedCornerShape(20.dp)),
                                    color = if (transaction.third.second == "Completed") {
                                        Color(0xFFDCFCE7)
                                    } else {
                                        Color(0xFFF3F4F6)
                                    }
                                ) {
                                    Text(
                                        transaction.third.second,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (transaction.third.second == "Completed") {
                                            Color(0xFF059669)
                                        } else {
                                            Color(0xFF6B7280)
                                        },
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Header
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding(),
            color = Color.White.copy(alpha = 0.8f),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    "Wallet & Earnings",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More",
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Bottom Navigation Bar
        TeacherEarningsBottomNavBar()
    }
}

@Composable
private fun BoxScope.TeacherEarningsBottomNavBar() {
    Surface(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            EarningsNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                isActive = false,
                onClick = { }
            )

            EarningsNavItem(
                icon = Icons.Default.MenuBook,
                label = "Courses",
                isActive = false,
                onClick = { }
            )

            EarningsNavItem(
                icon = Icons.Default.AccountBalanceWallet,
                label = "Wallet",
                isActive = true,
                onClick = { }
            )

            EarningsNavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                isActive = false,
                onClick = { }
            )
        }
    }
}

@Composable
private fun EarningsNavItem(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isActive) Color(0xFFEA2A33) else Color(0xFF9CA3AF),
            modifier = Modifier.size(24.dp)
        )

        Text(
            label,
            fontSize = 8.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = if (isActive) Color(0xFFEA2A33) else Color(0xFF9CA3AF),
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SelectDropdown(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextField(
            value = selectedOption,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable { expanded = !expanded },
            shape = RoundedCornerShape(12.dp),
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    tint = Color(0xFFEA2A33)
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, fontSize = 14.sp) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun surface(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    color: Color = Color.White,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color
    ) {
        content()
    }
}

@Preview
@Composable
fun TeacherDashboardPreview() {
    TeacherDashboardScreen()
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) Color(0xFFEA2A33) else Color(0xFF9CA3AF),
            modifier = Modifier.size(24.dp)
        )
        Text(
            label,
            fontSize = 9.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) Color(0xFFEA2A33) else Color(0xFF9CA3AF),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { }
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.7f)
        ),
        border = CardDefaults.outlinedCardBorder(
            enabled = true,
            border = BorderStroke(1.dp, Color(0xFFEA2A33).copy(alpha = 0.1f))
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Surface(
                modifier = Modifier
                    .size(40.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFEA2A33).copy(alpha = 0.1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFFEA2A33),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }

            Column {
                Text(
                    title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    description,
                    fontSize = 10.sp,
                    color = Color(0xFF9CA3AF)
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    change: String,
    changeColor: Color
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(110.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(
            enabled = true,
            border = BorderStroke(1.dp, Color(0xFFE5E7EB))
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                label,
                fontSize = 11.sp,
                color = Color(0xFF9CA3AF),
                fontWeight = FontWeight.Medium
            )

            Column {
                Text(
                    value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = changeColor
                    )
                    Text(
                        change,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = changeColor
                    )
                }
            }
        }
    }
}
