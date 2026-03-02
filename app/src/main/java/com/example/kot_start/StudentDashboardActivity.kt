package com.example.kot_start

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import java.util.Calendar

class StudentDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent { StudentApp() }
    }
}

data class EnrolledCourse(val name: String, val price: String)

@Composable
fun StudentApp() {
    var currentScreen by remember { mutableStateOf("main") }
    var selectedTab by remember { mutableStateOf(0) }
    val context = LocalContext.current
    var walletBalance by remember { mutableStateOf(1245.75) }
    val enrolledCourses = remember { mutableStateListOf<String>() }
    var viewingCourseIndex by remember { mutableStateOf(0) }

    BackHandler {
        when (currentScreen) {
            "main" -> { }
            "video_player" -> { currentScreen = "course_details" }
            else -> { currentScreen = "main" }
        }
    }

    when (currentScreen) {
        "main" -> StudentMainScreen(
            onScreenChange = { currentScreen = it },
            selectedTab = selectedTab,
            onTabChange = { selectedTab = it },
            walletBalance = walletBalance,
            enrolledCourses = enrolledCourses,
            onViewCourse = { idx -> viewingCourseIndex = idx; currentScreen = "course_details" },
            onLogout = {
                Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, SkillitLoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
                (context as? ComponentActivity)?.finish()
            }
        )
        "add_credits" -> StudentAddCreditsScreen(
            currentBalance = walletBalance,
            onBack = { currentScreen = "main" },
            onAddCredits = { amount ->
                walletBalance += amount
                Toast.makeText(context, "Rs ${"%.2f".format(amount)} added to wallet!", Toast.LENGTH_SHORT).show()
                currentScreen = "main"
            }
        )
        "course_details" -> StudentCourseDetailScreen(
            courseIndex = viewingCourseIndex,
            isEnrolled = enrolledCourses.contains("course_$viewingCourseIndex"),
            onBack = { currentScreen = "main" },
            onEnroll = {
                enrolledCourses.add("course_$viewingCourseIndex")
                Toast.makeText(context, "Enrolled successfully!", Toast.LENGTH_SHORT).show()
            },
            onWatchVideo = { currentScreen = "video_player" }
        )
        "video_player" -> StudentVideoPlayerScreen(onBack = { currentScreen = "course_details" })
        "make_bid" -> StudentMakeBidScreen(
            onBack = { currentScreen = "main"; selectedTab = 2 },
            onSubmit = { courseName, bidPrice ->
                Toast.makeText(context, "Bid of Rs $bidPrice placed on $courseName!", Toast.LENGTH_SHORT).show()
                currentScreen = "main"; selectedTab = 2
            }
        )
        "wallet_details" -> StudentWalletDetailScreen(
            balance = walletBalance,
            onBack = { currentScreen = "main" },
            onAddCredits = { currentScreen = "add_credits" }
        )
    }
}

// ======================== MAIN SCAFFOLD ========================
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentMainScreen(
    onScreenChange: (String) -> Unit,
    selectedTab: Int,
    onTabChange: (Int) -> Unit,
    walletBalance: Double,
    enrolledCourses: List<String>,
    onViewCourse: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val tabs = listOf("Home", "Learn", "Bids", "Wallet")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "SkillIt", modifier = Modifier.size(28.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("SkillIt", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                    }
                },
                actions = {
                    BadgedBox(badge = { Badge(containerColor = Color(0xFFEA2A33)) { Text("2", fontSize = 9.sp, color = Color.White) } }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color(0xFF6B7280))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color(0xFFEA2A33))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Default.Home, contentDescription = tab)
                                1 -> Icon(Icons.Default.MenuBook, contentDescription = tab)
                                2 -> Icon(Icons.Default.LocalOffer, contentDescription = tab)
                                else -> Icon(Icons.Default.AccountBalanceWallet, contentDescription = tab)
                            }
                        },
                        label = { Text(tab, fontSize = 10.sp) },
                        selected = selectedTab == index,
                        onClick = { onTabChange(index) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFFEA2A33),
                            selectedTextColor = Color(0xFFEA2A33),
                            indicatorColor = Color(0xFFEA2A33).copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> StudentHomeTab(LocalContext.current, walletBalance, onScreenChange)
                1 -> StudentLearnTab(LocalContext.current, enrolledCourses, onViewCourse)
                2 -> StudentBidsTab(LocalContext.current, onScreenChange)
                else -> StudentWalletTab(LocalContext.current, walletBalance, onScreenChange)
            }
        }
    }
}

// ======================== HOME TAB ========================
@Composable
fun StudentHomeTab(ctx: android.content.Context, balance: Double, nav: (String) -> Unit) {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        hour < 12 -> "Good morning"
        hour < 17 -> "Good afternoon"
        else -> "Good evening"
    }

    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Greeting
        item {
            Column {
                Text("$greeting!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                Spacer(modifier = Modifier.height(4.dp))
                Text("Ready to learn something new?", fontSize = 14.sp, color = Color(0xFF9CA3AF))
            }
        }
        // Wallet card with gradient
        item {
            Card(
                modifier = Modifier.fillMaxWidth().clickable { nav("wallet_details") },
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(Color(0xFFE63946), Color(0xFFFF6B6B))))
                        .padding(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Wallet Balance", fontSize = 13.sp, color = Color.White.copy(alpha = 0.9f))
                        }
                        Text("Rs ${"%.2f".format(balance)}", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { nav("add_credits") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp)) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Add Credits", color = Color(0xFFEA2A33), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Button(onClick = { nav("wallet_details") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.25f)), shape = RoundedCornerShape(12.dp)) {
                                Icon(Icons.Default.Visibility, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("View", color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
        // Continue Learning section
        item { Text("Continue Learning", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827)) }
        val courseProgress = listOf(
            Triple("Advanced UI/UX Design", 0.65f, "Design"),
            Triple("Kotlin for Android", 0.30f, "Technology"),
            Triple("Brand Strategy 101", 0.85f, "Business")
        )
        items(courseProgress.size) { i ->
            val (name, progress, category) = courseProgress[i]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(48.dp).background(Color(0xFFEA2A33).copy(alpha = 0.08f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(24.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF111827), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = Color(0xFFEA2A33).copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
                                Text(category, fontSize = 10.sp, color = Color(0xFFEA2A33), fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                            Spacer(Modifier.width(8.dp))
                            Text("${(progress * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF10B981))
                        }
                        Spacer(Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp)),
                            color = Color(0xFF10B981),
                            trackColor = Color(0xFFE5E7EB)
                        )
                    }
                }
            }
        }
        // Learning streak card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(44.dp).background(Color(0xFFF59E0B).copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(24.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text("3-Day Learning Streak!", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                        Text("Keep it up! Learn daily to maintain your streak.", fontSize = 12.sp, color = Color(0xFFB45309))
                    }
                }
            }
        }
        // My Status
        item { Text("My Status", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827)) }
        val statIcons = listOf(Icons.Default.MenuBook, Icons.Default.LocalOffer, Icons.Default.CheckCircle)
        val statLabels = listOf("Active Courses", "Pending Offers", "Completed")
        val statValues = listOf("5", "3", "12")
        val statColors = listOf(Color(0xFF3B82F6), Color(0xFFF59E0B), Color(0xFF10B981))
        items(statLabels.size) { i ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { Toast.makeText(ctx, "${statLabels[i]}: ${statValues[i]}", Toast.LENGTH_SHORT).show() },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(44.dp).background(statColors[i].copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(statIcons[i], contentDescription = null, tint = statColors[i], modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(statLabels[i], fontWeight = FontWeight.Medium, fontSize = 13.sp, color = Color(0xFF6B7280))
                        Text(statValues[i], fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFD1D5DB))
                }
            }
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

// ======================== LEARN TAB ========================
@Composable
fun StudentLearnTab(ctx: android.content.Context, enrolled: List<String>, onViewCourse: (Int) -> Unit) {
    var subTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Design", "Technology", "Business", "Lifestyle")

    val courseNames = listOf("Advanced UI/UX Design", "Kotlin for Android", "Brand Strategy 101", "Mobile Photography")
    val coursePrices = listOf("Rs 40", "Rs 55", "Rs 70", "Rs 85")
    val courseTeachers = listOf("Prof. Sarah", "Prof. Mike", "Prof. Lisa", "Prof. Raj")
    val courseCategories = listOf("Design", "Technology", "Business", "Lifestyle")

    val filteredCourses = courseNames.indices.filter { i ->
        val matchesSearch = searchQuery.isBlank() || courseNames[i].contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "All" || courseCategories[i] == selectedCategory
        matchesSearch && matchesCategory
    }

    val sessionTitles = listOf("Live Q&A: Design Principles", "Kotlin Workshop", "Brand Building Session")
    val sessionDates = listOf("2026-03-05", "2026-03-08", "2026-03-12")
    val sessionTimes = listOf("10:00 AM", "2:00 PM", "11:00 AM")

    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Learn", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827)) }

        // Search bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search courses...", color = Color(0xFFD1D5DB)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF9CA3AF), modifier = Modifier.size(20.dp)) },
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFFE63946),
                    unfocusedIndicatorColor = Color(0xFFE5E7EB)
                ),
                singleLine = true
            )
        }

        // Sub-tab switcher
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { subTab = 0 },
                    modifier = Modifier.weight(1f).height(42.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (subTab == 0) Color(0xFFEA2A33) else Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = if (subTab == 0) 4.dp else 0.dp)
                ) { Text("Browse Content", fontSize = 13.sp, color = if (subTab == 0) Color.White else Color(0xFF6B7280), fontWeight = FontWeight.SemiBold) }
                Button(
                    onClick = { subTab = 1 },
                    modifier = Modifier.weight(1f).height(42.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (subTab == 1) Color(0xFFEA2A33) else Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = if (subTab == 1) 4.dp else 0.dp)
                ) { Text("Sessions", fontSize = 13.sp, color = if (subTab == 1) Color.White else Color(0xFF6B7280), fontWeight = FontWeight.SemiBold) }
            }
        }

        if (subTab == 0) {
            // Category filter chips
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        Surface(
                            modifier = Modifier.clickable { selectedCategory = cat },
                            color = if (isSelected) Color(0xFFEA2A33) else Color.White,
                            shape = RoundedCornerShape(20.dp),
                            shadowElevation = if (isSelected) 2.dp else 0.dp
                        ) {
                            Text(
                                cat,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF6B7280),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // Course cards
            items(filteredCourses.size) { idx ->
                val i = filteredCourses[idx]
                val isEnrolled = enrolled.contains("course_$i")
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onViewCourse(i) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        // Thumbnail
                        Box(
                            modifier = Modifier.size(60.dp).background(Color(0xFFEA2A33).copy(alpha = 0.08f), RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(28.dp))
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(courseNames[i], fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF111827))
                            Text("By ${courseTeachers[i]}", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                            Spacer(Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Surface(color = Color(0xFFEA2A33).copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp)) {
                                    Text(courseCategories[i], fontSize = 10.sp, color = Color(0xFFEA2A33), fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                }
                                Text("4.${7 + i % 3}", fontSize = 12.sp, color = Color(0xFFF59E0B), fontWeight = FontWeight.SemiBold)
                                if (isEnrolled) {
                                    Surface(color = Color(0xFF10B981).copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp)) {
                                        Text("Enrolled", fontSize = 10.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(coursePrices[i], fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33), fontSize = 16.sp)
                        }
                    }
                }
            }
        } else {
            items(sessionTitles.size) { i ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(44.dp).background(Color(0xFF10B981).copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Event, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(22.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(sessionTitles[i], fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF111827))
                            Text("${sessionDates[i]} * ${sessionTimes[i]}", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                        }
                        Surface(color = Color(0xFF10B981).copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                            Text("Upcoming", fontSize = 11.sp, color = Color(0xFF10B981), fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                        }
                    }
                }
            }
        }
    }
}

// ======================== BIDS TAB ========================
@Composable
fun StudentBidsTab(ctx: android.content.Context, nav: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Column {
                    Text("My Bids", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                    Text("3 pending bids", fontSize = 13.sp, color = Color(0xFF9CA3AF))
                }
            }
            items(3) { i ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(40.dp).background(Color(0xFFF59E0B).copy(alpha = 0.1f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.LocalOffer, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(20.dp))
                                }
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text("Bid on Course ${i + 1}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF111827))
                                    Text("Design Fundamentals", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                                }
                            }
                            Surface(color = Color(0xFFF59E0B).copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)) {
                                Text("Pending", fontSize = 11.sp, color = Color(0xFFF59E0B), fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFF9FAFB), RoundedCornerShape(10.dp)).padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column { Text("Your Bid", fontSize = 10.sp, color = Color(0xFF9CA3AF)); Text("Rs ${35 + i * 5}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33)) }
                            Column(horizontalAlignment = Alignment.End) { Text("Original", fontSize = 10.sp, color = Color(0xFF9CA3AF)); Text("Rs ${45 + i * 5}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827)) }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(72.dp)) }
        }
        FloatingActionButton(
            onClick = { nav("make_bid") },
            containerColor = Color(0xFFEA2A33),
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "New Bid", tint = Color.White)
        }
    }
}

// ======================== WALLET TAB ========================
@Composable
fun StudentWalletTab(ctx: android.content.Context, balance: Double, nav: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { Text("Wallet & Credits", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827)) }
        item {
            Card(
                modifier = Modifier.fillMaxWidth().clickable { nav("wallet_details") },
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(Color(0xFFE63946), Color(0xFFFF6B6B))))
                        .padding(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Available Balance", fontSize = 13.sp, color = Color.White.copy(alpha = 0.9f))
                        Text("Rs ${"%.2f".format(balance)}", fontSize = 38.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(onClick = { nav("add_credits") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.White), shape = RoundedCornerShape(10.dp)) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Add Credits", color = Color(0xFFEA2A33), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Button(onClick = { nav("wallet_details") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.25f)), shape = RoundedCornerShape(10.dp)) {
                                Icon(Icons.Default.History, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("History", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
        item { Text("Recent Transactions", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF111827)) }
        val txLabels = listOf("Added Funds", "Course Purchase", "Referral Bonus", "Course Fee")
        val txAmounts = listOf("+Rs 50.00", "-Rs 40.00", "+Rs 25.00", "-Rs 55.00")
        val txColors = listOf(Color(0xFF10B981), Color(0xFFEF4444), Color(0xFF10B981), Color(0xFFEF4444))
        val txIcons = listOf(Icons.Default.Add, Icons.Default.School, Icons.Default.CardGiftcard, Icons.Default.TrendingDown)
        items(txLabels.size) { i ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(42.dp).background(txColors[i].copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(txIcons[i], contentDescription = null, tint = txColors[i], modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(txLabels[i], fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF111827))
                        Text("2 days ago", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                    }
                    Text(txAmounts[i], fontWeight = FontWeight.Bold, color = txColors[i], fontSize = 14.sp)
                }
            }
        }
    }
}

// ======================== ADD CREDITS ========================
@Composable
fun StudentAddCreditsScreen(currentBalance: Double, onBack: () -> Unit, onAddCredits: (Double) -> Unit) {
    var selectedAmount by remember { mutableStateOf(0.0) }
    var customAmount by remember { mutableStateOf("") }
    val presets = listOf(10.0, 25.0, 50.0, 100.0, 250.0, 500.0)

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF374151)) }
            Text("Add Credits", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
        }
        Spacer(Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
            Box(modifier = Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(Color(0xFFE63946), Color(0xFFFF6B6B)))).padding(20.dp)) {
                Column {
                    Text("Current Balance", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                    Text("Rs ${"%.2f".format(currentBalance)}", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Text("Select Amount", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF111827))
        Spacer(Modifier.height(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            for (row in presets.chunked(3)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { amount ->
                        val isSelected = selectedAmount == amount && customAmount.isBlank()
                        Button(
                            onClick = { selectedAmount = amount; customAmount = "" },
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = if (isSelected) Color(0xFFEA2A33) else Color.White),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
                        ) { Text("Rs ${"%.0f".format(amount)}", color = if (isSelected) Color.White else Color(0xFF374151), fontWeight = FontWeight.SemiBold) }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Or enter custom amount", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF6B7280))
        OutlinedTextField(value = customAmount, onValueChange = { customAmount = it; selectedAmount = 0.0 }, placeholder = { Text("Custom amount") }, prefix = { Text("Rs ") }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(16.dp))
        Text("Payment Method", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF111827))
        Spacer(Modifier.height(8.dp))
        var method by remember { mutableStateOf("card") }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("card" to "Credit Card", "paypal" to "PayPal", "bank" to "Bank").forEach { (key, label) ->
                Button(
                    onClick = { method = key },
                    modifier = Modifier.weight(1f).height(46.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (method == key) Color(0xFFEA2A33) else Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) { Text(label, fontSize = 11.sp, color = if (method == key) Color.White else Color(0xFF374151), fontWeight = FontWeight.SemiBold) }
            }
        }
        Spacer(Modifier.height(24.dp))
        val finalAmount = if (customAmount.isNotBlank()) customAmount.toDoubleOrNull() ?: 0.0 else selectedAmount
        Button(
            onClick = { if (finalAmount > 0) onAddCredits(finalAmount) },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33)),
            shape = RoundedCornerShape(14.dp),
            enabled = finalAmount > 0
        ) { Text("Add Rs ${"%.2f".format(finalAmount)} to Wallet", fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(46.dp), shape = RoundedCornerShape(14.dp)) { Text("Cancel") }
        Spacer(Modifier.height(24.dp))
    }
}

// ======================== COURSE DETAILS ========================
@Composable
fun StudentCourseDetailScreen(courseIndex: Int, isEnrolled: Boolean, onBack: () -> Unit, onEnroll: () -> Unit, onWatchVideo: () -> Unit) {
    val names = listOf("Advanced UI/UX Design", "Kotlin for Android", "Brand Strategy 101", "Mobile Photography")
    val prices = listOf("Rs 40", "Rs 55", "Rs 70", "Rs 85")
    val name = names.getOrElse(courseIndex) { "Course" }
    val price = prices.getOrElse(courseIndex) { "Rs 50" }

    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF374151)) }
                Text("Course Details", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth().height(200.dp).clickable { if (isEnrolled) onWatchVideo() }, colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)), shape = RoundedCornerShape(16.dp)) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier.size(64.dp).background(Color.White.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayCircle, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(40.dp))
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(if (isEnrolled) "Tap to watch" else "Enroll to watch", fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        }
        item {
            Column {
                Text(name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("4.8", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF59E0B))
                    Spacer(Modifier.width(4.dp))
                    Text("(234 reviews)", fontSize = 13.sp, color = Color(0xFF9CA3AF))
                }
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Course Info", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF111827))
                    Spacer(Modifier.height(12.dp))
                    listOf("Duration:" to "8 weeks", "Level:" to "Intermediate", "Students:" to "1,240 enrolled", "Price:" to price).forEach { (k, v) ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(k, fontSize = 13.sp, color = Color(0xFF6B7280))
                            Text(v, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = if (k == "Price:") Color(0xFFEA2A33) else Color(0xFF111827))
                        }
                    }
                }
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("What You'll Learn", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF111827))
                    Spacer(Modifier.height(12.dp))
                    listOf("Master advanced design principles", "Create responsive layouts", "Build real-world projects").forEach { point ->
                        Row(Modifier.padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier.size(22.dp).background(Color(0xFF10B981).copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
                            }
                            Text(point, fontSize = 13.sp, color = Color(0xFF374151))
                        }
                    }
                }
            }
        }
        item {
            if (isEnrolled) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF10B981).copy(alpha = 0.1f)), shape = RoundedCornerShape(14.dp)) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Enrolled", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Button(onClick = onWatchVideo, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33)), shape = RoundedCornerShape(14.dp)) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Watch Course", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            } else {
                Button(onClick = onEnroll, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33)), shape = RoundedCornerShape(14.dp)) {
                    Text("Enroll Now", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(46.dp), shape = RoundedCornerShape(14.dp)) { Text("Back") }
        }
    }
}

// ======================== VIDEO PLAYER ========================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentVideoPlayerScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Row(modifier = Modifier.fillMaxWidth().background(Color.Black).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
            Text("Now Playing", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        Box(modifier = Modifier.fillMaxWidth().weight(1f).background(Color(0xFF1A1A2E)), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(80.dp).background(Color.White.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(56.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text("Advanced UI/UX Design", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("Video content would load here", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
            }
        }
        LinearProgressIndicator(progress = { 0.35f }, modifier = Modifier.fillMaxWidth().height(3.dp), color = Color(0xFFEA2A33), trackColor = Color.Gray)
        Row(modifier = Modifier.fillMaxWidth().background(Color(0xFF111111)).padding(12.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {}) { Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = Color.White) }
            IconButton(onClick = {}) { Icon(Icons.Default.Replay10, contentDescription = "Rewind", tint = Color.White) }
            IconButton(onClick = {}) {
                Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(Color(0xFFEA2A33)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Pause, contentDescription = "Play/Pause", tint = Color.White, modifier = Modifier.size(30.dp))
                }
            }
            IconButton(onClick = {}) { Icon(Icons.Default.Forward10, contentDescription = "Forward", tint = Color.White) }
            IconButton(onClick = {}) { Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = Color.White) }
        }
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFF1A1A1A)).padding(16.dp)) {
            Text("Advanced UI/UX Design", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("Lesson 1 of 12", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
        }
    }
}

// ======================== MAKE BID ========================
@Composable
fun StudentMakeBidScreen(onBack: () -> Unit, onSubmit: (String, String) -> Unit) {
    val courses = listOf("Advanced UI/UX Design (Rs 40)", "Kotlin for Android (Rs 55)", "Brand Strategy 101 (Rs 70)", "Mobile Photography (Rs 85)")
    var selectedCourse by remember { mutableStateOf(courses[0]) }
    var bidPrice by remember { mutableStateOf(30f) }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF374151)) }
            Text("Make a Bid", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
        }
        Spacer(Modifier.height(20.dp))
        Text("Select Course", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF374151))
        Spacer(Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) { Text(selectedCourse, modifier = Modifier.weight(1f), textAlign = TextAlign.Start, fontSize = 12.sp); Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) { courses.forEach { c -> DropdownMenuItem(text = { Text(c, fontSize = 12.sp) }, onClick = { selectedCourse = c; expanded = false }) } }
        }
        Spacer(Modifier.height(24.dp))
        Text("Your Bid Price", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF374151))
        Spacer(Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Rs ${"%.0f".format(bidPrice)}", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                Spacer(Modifier.height(12.dp))
                Slider(
                    value = bidPrice, onValueChange = { bidPrice = it }, valueRange = 5f..200f, steps = 38,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(thumbColor = Color(0xFFEA2A33), activeTrackColor = Color(0xFFEA2A33))
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Rs 5", fontSize = 11.sp, color = Color(0xFF9CA3AF))
                    Text("Rs 200", fontSize = 11.sp, color = Color(0xFF9CA3AF))
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)), shape = RoundedCornerShape(12.dp)) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(10.dp))
                Text("Tip: Competitive bids have a higher chance of being accepted!", fontSize = 12.sp, color = Color(0xFFB45309))
            }
        }
        Spacer(Modifier.height(28.dp))
        Button(onClick = { onSubmit(selectedCourse, "${"%.0f".format(bidPrice)}") }, modifier = Modifier.fillMaxWidth().height(54.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33)), shape = RoundedCornerShape(14.dp)) {
            Text("Submit Bid", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(46.dp), shape = RoundedCornerShape(14.dp)) { Text("Cancel") }
        Spacer(Modifier.height(24.dp))
    }
}

// ======================== WALLET DETAILS ========================
@Composable
fun StudentWalletDetailScreen(balance: Double, onBack: () -> Unit, onAddCredits: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF374151)) }
                Text("Wallet Details", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(Color(0xFFE63946), Color(0xFFFF6B6B)))).padding(24.dp)) {
                    Column {
                        Text("Available Balance", fontSize = 13.sp, color = Color.White.copy(alpha = 0.9f))
                        Text("Rs ${"%.2f".format(balance)}", fontSize = 38.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(Modifier.height(14.dp))
                        Button(onClick = onAddCredits, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp)) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Add More Credits", color = Color(0xFFEA2A33), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        item { Text("Spending Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF111827)) }
        val summaryLabels = listOf("Spent on Courses", "Referral Earnings", "Total Added")
        val summaryValues = listOf("Rs 250.00", "Rs 125.00", "Rs ${"%.2f".format(balance + 250 - 125)}")
        val summaryIcons = listOf(Icons.Default.School, Icons.Default.CardGiftcard, Icons.Default.AccountBalanceWallet)
        val summaryColors = listOf(Color(0xFFEF4444), Color(0xFF10B981), Color(0xFF3B82F6))
        items(summaryLabels.size) { i ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(42.dp).background(summaryColors[i].copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(summaryIcons[i], contentDescription = null, tint = summaryColors[i], modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Text(summaryLabels[i], fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF374151), modifier = Modifier.weight(1f))
                    Text(summaryValues[i], fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33), fontSize = 15.sp)
                }
            }
        }
        item { Text("Transaction History", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF111827)) }
        val txLabels = listOf("Added Rs 100", "Purchased Course", "Referral Bonus", "Added Rs 50", "Course Fee")
        val txAmounts = listOf("+Rs 100.00", "-Rs 40.00", "+Rs 25.00", "+Rs 50.00", "-Rs 55.00")
        val txColors = listOf(Color(0xFF10B981), Color(0xFFEF4444), Color(0xFF10B981), Color(0xFF10B981), Color(0xFFEF4444))
        items(txLabels.size) { i ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(36.dp).background(txColors[i].copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(if (txColors[i] == Color(0xFFEF4444)) Icons.Default.TrendingDown else Icons.Default.TrendingUp, contentDescription = null, tint = txColors[i], modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(txLabels[i], fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF111827))
                        Text("${5 - i} days ago", fontSize = 11.sp, color = Color(0xFF9CA3AF))
                    }
                    Text(txAmounts[i], fontWeight = FontWeight.Bold, color = txColors[i], fontSize = 13.sp)
                }
            }
        }
    }
}
