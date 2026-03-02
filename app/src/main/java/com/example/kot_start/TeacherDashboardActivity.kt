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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import java.util.Calendar

class TeacherDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent { TeacherApp() }
    }
}

data class TeacherContent(val title: String, val description: String, val category: String, val price: String, val isPaid: Boolean)
data class TeacherSession(val title: String, val date: String, val time: String, val duration: String)

@Composable
fun TeacherApp() {
    var currentScreen by remember { mutableStateOf("main") }
    var selectedTab by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val uploadedContent = remember { mutableStateListOf<TeacherContent>() }
    val scheduledSessions = remember { mutableStateListOf<TeacherSession>() }

    BackHandler {
        when (currentScreen) {
            "main" -> { }
            else -> { currentScreen = "main" }
        }
    }

    when (currentScreen) {
        "main" -> TeacherDashboardScreen(
            onScreenChange = { currentScreen = it },
            selectedTab = selectedTab,
            onTabChange = { selectedTab = it },
            uploadedContent = uploadedContent,
            scheduledSessions = scheduledSessions,
            onLogout = {
                Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, SkillitLoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
                (context as? ComponentActivity)?.finish()
            }
        )
        "add_content" -> TeacherAddContentScreen(
            onBack = { currentScreen = "main"; selectedTab = 1 },
            onSave = { content ->
                uploadedContent.add(content)
                Toast.makeText(context, "'${content.title}' uploaded!", Toast.LENGTH_SHORT).show()
                currentScreen = "main"; selectedTab = 1
            }
        )
        "add_session" -> TeacherAddSessionScreen(
            onBack = { currentScreen = "main"; selectedTab = 1 },
            onSave = { session ->
                scheduledSessions.add(session)
                Toast.makeText(context, "'${session.title}' scheduled!", Toast.LENGTH_SHORT).show()
                currentScreen = "main"; selectedTab = 1
            }
        )
        "bid_details" -> TeacherBidDetailsScreen(onBack = { currentScreen = "main"; selectedTab = 2 })
        "earnings_details" -> TeacherEarningsDetailsScreen(onBack = { currentScreen = "main"; selectedTab = 3 })
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(
    onScreenChange: (String) -> Unit,
    selectedTab: Int,
    onTabChange: (Int) -> Unit,
    uploadedContent: List<TeacherContent>,
    scheduledSessions: List<TeacherSession>,
    onLogout: () -> Unit
) {
    val tabs = listOf("Home", "Learning", "Bids", "Earnings")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "SkillIt", modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SkillIt", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                    }
                },
                actions = {
                    // Notification bell with badge
                    BadgedBox(badge = { Badge(containerColor = Color(0xFFEA2A33)) { Text("3", fontSize = 9.sp, color = Color.White) } }) {
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
                                0 -> Icon(Icons.Default.Dashboard, contentDescription = tab)
                                1 -> Icon(Icons.Default.MenuBook, contentDescription = tab)
                                2 -> Icon(Icons.Default.TrendingUp, contentDescription = tab)
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
                0 -> TeacherHomeTab(LocalContext.current, onScreenChange)
                1 -> TeacherLearningTab(LocalContext.current, onScreenChange, uploadedContent, scheduledSessions)
                2 -> TeacherBidsTab(LocalContext.current, onScreenChange)
                else -> TeacherEarningsTab(LocalContext.current, onScreenChange)
            }
        }
    }
}

// ======================== HOME TAB ========================
@Composable
fun TeacherHomeTab(ctx: android.content.Context, nav: (String) -> Unit) {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        hour < 12 -> "Good morning"
        hour < 17 -> "Good afternoon"
        else -> "Good evening"
    }
    val tips = listOf(
        "Record shorter videos (5-10 min) for better engagement.",
        "Reply to student bids within 24 hours for higher ratings.",
        "Add preview thumbnails to increase course enrollments.",
        "Schedule weekly live sessions to build a loyal audience."
    )
    val tipIndex = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % tips.size

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Greeting
        item {
            Column {
                Text("$greeting!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                Spacer(modifier = Modifier.height(4.dp))
                Text("Here's your teaching overview", fontSize = 14.sp, color = Color(0xFF9CA3AF))
            }
        }
        // Earnings card with gradient
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(Color(0xFFE63946), Color(0xFFFF6B6B))))
                        .padding(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Total Earnings", fontSize = 13.sp, color = Color.White.copy(alpha = 0.9f))
                        }
                        Text("Rs 4,280.50", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Button(
                            onClick = { nav("earnings_details") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Withdraw / View Earnings", color = Color(0xFFEA2A33), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
        // Quick Actions
        item { Text("Quick Actions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827)) }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionCard(Modifier.weight(1f), Icons.Default.CloudUpload, "Upload", Color(0xFF3B82F6)) { nav("add_content") }
                QuickActionCard(Modifier.weight(1f), Icons.Default.Event, "Schedule", Color(0xFF10B981)) { nav("add_session") }
                QuickActionCard(Modifier.weight(1f), Icons.Default.TrendingUp, "Bids", Color(0xFFF59E0B)) { nav("bid_details") }
            }
        }
        // Tip of the day
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier.size(36.dp).background(Color(0xFFF59E0B).copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Tip of the Day", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                        Spacer(Modifier.height(4.dp))
                        Text(tips[tipIndex], fontSize = 13.sp, color = Color(0xFFB45309), lineHeight = 18.sp)
                    }
                }
            }
        }
        // Quick Stats
        item { Text("Quick Stats", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827)) }
        val statIcons = listOf(Icons.Default.People, Icons.Default.MenuBook, Icons.Default.VideoCall)
        val statLabels = listOf("Active Students", "Courses Published", "Sessions Done")
        val statValues = listOf("1,240", "6", "28")
        val statColors = listOf(Color(0xFF3B82F6), Color(0xFF10B981), Color(0xFFF59E0B))
        items(statLabels.size) { i ->
            Card(
                modifier = Modifier.fillMaxWidth(),
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

@Composable
fun QuickActionCard(modifier: Modifier, icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(44.dp).background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF374151))
        }
    }
}

// ======================== LEARNING TAB ========================
@Composable
fun TeacherLearningTab(ctx: android.content.Context, nav: (String) -> Unit, uploaded: List<TeacherContent>, sessions: List<TeacherSession>) {
    var subTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    val demo = listOf(
        TeacherContent("UI/UX Fundamentals", "Learn design basics", "Design", "Rs 29", true),
        TeacherContent("Kotlin Crash Course", "Android dev essentials", "Technology", "Free", false),
        TeacherContent("Brand Strategy", "Marketing your brand", "Business", "Rs 19", true),
    )
    val allContent = (demo + uploaded).filter {
        searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Text("Learning", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827)) }

            // Search bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search content...", color = Color(0xFFD1D5DB)) },
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

            // Sub-tab row
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { subTab = 0 },
                        modifier = Modifier.weight(1f).height(42.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (subTab == 0) Color(0xFFEA2A33) else Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = if (subTab == 0) 4.dp else 0.dp)
                    ) { Text("My Content", fontSize = 13.sp, color = if (subTab == 0) Color.White else Color(0xFF6B7280), fontWeight = FontWeight.SemiBold) }
                    Button(
                        onClick = { subTab = 1 },
                        modifier = Modifier.weight(1f).height(42.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (subTab == 1) Color(0xFFEA2A33) else Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = if (subTab == 1) 4.dp else 0.dp)
                    ) { Text("My Sessions", fontSize = 13.sp, color = if (subTab == 1) Color.White else Color(0xFF6B7280), fontWeight = FontWeight.SemiBold) }
                }
            }

            if (subTab == 0) {
                item { Text("My Content (${allContent.size})", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF9CA3AF)) }
                items(allContent.size) { i ->
                    val c = allContent[i]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            // Thumbnail placeholder
                            Box(
                                modifier = Modifier.size(52.dp).background(Color(0xFFEA2A33).copy(alpha = 0.08f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.OndemandVideo, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(24.dp))
                            }
                            Spacer(Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(c.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF111827))
                                Text(c.description, fontSize = 12.sp, color = Color(0xFF9CA3AF))
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Surface(color = Color(0xFFEA2A33).copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp)) {
                                        Text(c.category, fontSize = 10.sp, color = Color(0xFFEA2A33), fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                    }
                                    Surface(color = if (c.isPaid) Color(0xFF10B981).copy(alpha = 0.1f) else Color(0xFF3B82F6).copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp)) {
                                        Text(if (c.isPaid) c.price else "Free", fontSize = 10.sp, fontWeight = FontWeight.Medium, color = if (c.isPaid) Color(0xFF10B981) else Color(0xFF3B82F6), modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (sessions.isEmpty()) {
                    item {
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(14.dp)) {
                            Column(modifier = Modifier.padding(32.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier.size(56.dp).background(Color(0xFFF3F4F6), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Event, contentDescription = null, tint = Color(0xFF9CA3AF), modifier = Modifier.size(28.dp))
                                }
                                Spacer(Modifier.height(12.dp))
                                Text("No sessions scheduled yet", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF6B7280))
                                Text("Tap + to schedule your first session", fontSize = 12.sp, color = Color(0xFFD1D5DB))
                            }
                        }
                    }
                } else {
                    item { Text("Scheduled Sessions (${sessions.size})", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF9CA3AF)) }
                    items(sessions.size) { i ->
                        val s = sessions[i]
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(14.dp)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(44.dp).background(Color(0xFF10B981).copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Event, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(22.dp))
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(s.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF111827))
                                    Text("${s.date} * ${s.time} * ${s.duration}", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                                }
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
        FloatingActionButton(
            onClick = { if (subTab == 0) nav("add_content") else nav("add_session") },
            containerColor = Color(0xFFEA2A33),
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            shape = RoundedCornerShape(16.dp)
        ) { Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White) }
    }
}

// ======================== BIDS TAB ========================
@Composable
fun TeacherBidsTab(ctx: android.content.Context, nav: (String) -> Unit) {
    val bidStates = remember { mutableStateMapOf<Int, String>() }
    val counterValues = remember { mutableStateMapOf<Int, Float>() }
    var showCounterDialog by remember { mutableStateOf(-1) }

    if (showCounterDialog >= 0) {
        val idx = showCounterDialog
        var counterPrice by remember { mutableStateOf(counterValues[idx] ?: 45f) }
        AlertDialog(
            onDismissRequest = { showCounterDialog = -1 },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Counter Offer", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Student offered Rs ${40 + idx * 5}", fontSize = 13.sp, color = Color(0xFF6B7280))
                    Spacer(Modifier.height(16.dp))
                    Text("Your Counter Price", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF374151))
                    Spacer(Modifier.height(8.dp))
                    Text("Rs ${"%.0f".format(counterPrice)}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                    Spacer(Modifier.height(8.dp))
                    Slider(
                        value = counterPrice,
                        onValueChange = { counterPrice = it },
                        valueRange = 5f..200f,
                        steps = 38,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(thumbColor = Color(0xFFEA2A33), activeTrackColor = Color(0xFFEA2A33))
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Rs 5", fontSize = 10.sp, color = Color(0xFF9CA3AF))
                        Text("Rs 200", fontSize = 10.sp, color = Color(0xFF9CA3AF))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        counterValues[idx] = counterPrice
                        bidStates[idx] = "countered"
                        Toast.makeText(ctx, "Counter offer of Rs ${"%.0f".format(counterPrice)} sent!", Toast.LENGTH_SHORT).show()
                        showCounterDialog = -1
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33)),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Send Counter") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCounterDialog = -1 }, shape = RoundedCornerShape(10.dp)) { Text("Cancel") }
            }
        )
    }

    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Column {
                Text("Student Bids", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                Text("3 pending bids", fontSize = 13.sp, color = Color(0xFF9CA3AF))
            }
        }
        items(3) { i ->
            val state = bidStates[i]
            Card(
                modifier = Modifier.fillMaxWidth().clickable { nav("bid_details") },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(40.dp).background(Color(0xFFEA2A33).copy(alpha = 0.08f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("S${i + 1}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("Student ${i + 1} Offer", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF111827))
                                Text("Advanced Design Course", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                            }
                        }
                        if (state != null) {
                            Surface(
                                color = if (state == "accepted") Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFF3B82F6).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    if (state == "accepted") "Accepted" else "Countered",
                                    fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                                    color = if (state == "accepted") Color(0xFF10B981) else Color(0xFF3B82F6),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFF9FAFB), RoundedCornerShape(10.dp)).padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Offered", fontSize = 10.sp, color = Color(0xFF9CA3AF))
                            Text("Rs ${40 + i * 5}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Your Rate", fontSize = 10.sp, color = Color(0xFF9CA3AF))
                            Text(if (state == "countered") "Rs ${"%.0f".format(counterValues[i] ?: 45f)}" else "Rs 45", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    if (state == "accepted") {
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF10B981).copy(alpha = 0.1f)), shape = RoundedCornerShape(10.dp)) {
                            Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Accepted", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    } else if (state == "countered") {
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF3B82F6).copy(alpha = 0.1f)), shape = RoundedCornerShape(10.dp)) {
                            Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = Color(0xFF3B82F6), modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Counter Sent - Rs ${"%.0f".format(counterValues[i] ?: 45f)}", color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    } else {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedButton(
                                onClick = { showCounterDialog = i },
                                modifier = Modifier.weight(1f).height(42.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Counter", fontSize = 13.sp)
                            }
                            Button(
                                onClick = { bidStates[i] = "accepted"; Toast.makeText(ctx, "Bid accepted!", Toast.LENGTH_SHORT).show() },
                                modifier = Modifier.weight(1f).height(42.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Accept", fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ======================== EARNINGS TAB ========================
@Composable
fun TeacherEarningsTab(ctx: android.content.Context, nav: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { Text("Wallet & Earnings", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827)) }
        item {
            Card(
                modifier = Modifier.fillMaxWidth().clickable { nav("earnings_details") },
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(Color(0xFFE63946), Color(0xFFFF6B6B))))
                        .padding(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Total Available Balance", fontSize = 13.sp, color = Color.White.copy(alpha = 0.9f))
                        Text("Rs 4,280.50", fontSize = 38.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(onClick = { nav("earnings_details") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.White), shape = RoundedCornerShape(10.dp)) {
                                Icon(Icons.Default.Payment, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Withdraw", color = Color(0xFFEA2A33), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Button(onClick = { nav("earnings_details") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.25f)), shape = RoundedCornerShape(10.dp)) {
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
        val txLabels = listOf("Withdrawal", "Course Sale", "Session Fee")
        val txAmounts = listOf("100.00", "200.00", "300.00")
        val txIcons = listOf(Icons.Default.AccountBalanceWallet, Icons.Default.MenuBook, Icons.Default.TrendingUp)
        val txColors = listOf(Color(0xFFEF4444), Color(0xFF10B981), Color(0xFF10B981))
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(txLabels[i], fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF111827))
                        Text("2 days ago", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                    }
                    Text(if (i == 0) "-Rs ${txAmounts[i]}" else "+Rs ${txAmounts[i]}", fontWeight = FontWeight.Bold, color = txColors[i], fontSize = 14.sp)
                }
            }
        }
    }
}

// ======================== ADD CONTENT SCREEN ========================
@Composable
fun TeacherAddContentScreen(onBack: () -> Unit, onSave: (TeacherContent) -> Unit) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Design") }
    var isPaid by remember { mutableStateOf(false) }
    var price by remember { mutableStateOf("") }
    val categories = listOf("Design", "Technology", "Business", "Lifestyle", "Other")

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF374151)) }
            Text("Upload Content", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
        }
        Spacer(Modifier.height(20.dp))
        Text("Title *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF374151))
        OutlinedTextField(value = title, onValueChange = { title = it }, placeholder = { Text("e.g. Advanced UI Design") }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(12.dp))
        Text("Description", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF374151))
        OutlinedTextField(value = desc, onValueChange = { desc = it }, placeholder = { Text("What will students learn?") }, modifier = Modifier.fillMaxWidth().height(100.dp).padding(vertical = 6.dp), maxLines = 4, shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(12.dp))
        Text("Category", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF374151))
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) { Text(category, modifier = Modifier.weight(1f), textAlign = TextAlign.Start); Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) { categories.forEach { cat -> DropdownMenuItem(text = { Text(cat) }, onClick = { category = cat; expanded = false }) } }
        }
        Spacer(Modifier.height(16.dp))
        Text("Pricing", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF374151))
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { isPaid = false }, modifier = Modifier.weight(1f).height(42.dp), colors = ButtonDefaults.buttonColors(containerColor = if (!isPaid) Color(0xFFEA2A33) else Color.White), shape = RoundedCornerShape(10.dp)) { Text("Free", color = if (!isPaid) Color.White else Color(0xFF374151)) }
            Button(onClick = { isPaid = true }, modifier = Modifier.weight(1f).height(42.dp), colors = ButtonDefaults.buttonColors(containerColor = if (isPaid) Color(0xFFEA2A33) else Color.White), shape = RoundedCornerShape(10.dp)) { Text("Paid", color = if (isPaid) Color.White else Color(0xFF374151)) }
        }
        if (isPaid) {
            OutlinedTextField(value = price, onValueChange = { price = it }, placeholder = { Text("Price in Rs") }, prefix = { Text("Rs ") }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(12.dp))
        }
        Spacer(Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth().height(130.dp), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(50.dp).background(Color(0xFFEA2A33).copy(alpha = 0.08f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(28.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Tap to upload video / file", fontSize = 13.sp, color = Color(0xFF9CA3AF))
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { if (title.isNotBlank()) onSave(TeacherContent(title, desc.ifBlank { "No description" }, category, if (isPaid) "Rs $price" else "Free", isPaid)) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33)),
            shape = RoundedCornerShape(14.dp),
            enabled = title.isNotBlank()
        ) { Text("Upload Content", fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(46.dp), shape = RoundedCornerShape(14.dp)) { Text("Cancel") }
        Spacer(Modifier.height(32.dp))
    }
}

// ======================== ADD SESSION SCREEN ========================
@Composable
fun TeacherAddSessionScreen(onBack: () -> Unit, onSave: (TeacherSession) -> Unit) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("1 hour") }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF374151)) }
            Text("Schedule Session", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
        }
        Spacer(Modifier.height(20.dp))
        Text("Session Title *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF374151))
        OutlinedTextField(value = title, onValueChange = { title = it }, placeholder = { Text("e.g. Live Q&A: Design Principles") }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(12.dp))
        Text("Date *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF374151))
        OutlinedTextField(value = date, onValueChange = { date = it }, placeholder = { Text("YYYY-MM-DD") }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(12.dp))
        Text("Time *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF374151))
        OutlinedTextField(value = time, onValueChange = { time = it }, placeholder = { Text("e.g. 10:00 AM") }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(12.dp))
        Text("Duration", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF374151))
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) { Text(duration, modifier = Modifier.weight(1f), textAlign = TextAlign.Start); Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                listOf("30 min", "1 hour", "1.5 hours", "2 hours").forEach { d -> DropdownMenuItem(text = { Text(d) }, onClick = { duration = d; expanded = false }) }
            }
        }
        Spacer(Modifier.height(28.dp))
        Button(
            onClick = { if (title.isNotBlank() && date.isNotBlank() && time.isNotBlank()) onSave(TeacherSession(title, date, time, duration)) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33)),
            shape = RoundedCornerShape(14.dp),
            enabled = title.isNotBlank() && date.isNotBlank() && time.isNotBlank()
        ) { Text("Schedule Session", fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(46.dp), shape = RoundedCornerShape(14.dp)) { Text("Cancel") }
        Spacer(Modifier.height(32.dp))
    }
}

// ======================== BID DETAILS ========================
@Composable
fun TeacherBidDetailsScreen(onBack: () -> Unit) {
    val ctx = LocalContext.current
    var accepted by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF374151)) }
            Text("Bid Details", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
        }
        Spacer(Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(44.dp).background(Color(0xFF3B82F6).copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF3B82F6), modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Student Information", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF111827))
                        Row {
                            Text("Rating: 4.8/5 ", fontSize = 13.sp, color = Color(0xFF6B7280))
                            Text("*", fontSize = 13.sp, color = Color(0xFFF59E0B))
                        }
                        Text("Previous Bids: 5", fontSize = 13.sp, color = Color(0xFF6B7280))
                    }
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Price Comparison", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF111827))
                Spacer(Modifier.height(14.dp))
                Row(Modifier.fillMaxWidth().background(Color(0xFFF9FAFB), RoundedCornerShape(10.dp)).padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text("Your Rate:", fontSize = 13.sp, color = Color(0xFF6B7280)); Text("Rs 45", fontWeight = FontWeight.Bold, color = Color(0xFF111827)) }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth().background(Color(0xFFFFF7ED), RoundedCornerShape(10.dp)).padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text("Student Offer:", fontSize = 13.sp, color = Color(0xFF6B7280)); Text("Rs 40", fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33)) }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth().background(Color(0xFFFEF2F2), RoundedCornerShape(10.dp)).padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text("Difference:", fontSize = 13.sp, color = Color(0xFF6B7280)); Text("-Rs 5", fontWeight = FontWeight.Bold, color = Color(0xFFEF4444)) }
            }
        }
        Spacer(Modifier.weight(1f))
        if (accepted) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF10B981).copy(alpha = 0.1f)), shape = RoundedCornerShape(14.dp)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Bid Accepted", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        } else {
            Button(onClick = { accepted = true; Toast.makeText(ctx, "Bid accepted!", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33)), shape = RoundedCornerShape(14.dp)) { Text("Accept Bid", fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(46.dp), shape = RoundedCornerShape(14.dp)) { Text("Back") }
        Spacer(Modifier.height(16.dp))
    }
}

// ======================== EARNINGS DETAILS ========================
@Composable
fun TeacherEarningsDetailsScreen(onBack: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF374151)) }
                Text("Earnings Details", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(Color(0xFFE63946), Color(0xFFFF6B6B)))).padding(24.dp)) {
                    Column {
                        Text("Total Balance", fontSize = 13.sp, color = Color.White.copy(alpha = 0.9f))
                        Text("Rs 4,280.50", fontSize = 38.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(Modifier.height(6.dp))
                        Text("Last Updated: Today at 2:30 PM", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }
        }
        item { Text("Income Breakdown", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF111827)) }
        val breakLabels = listOf("Courses", "Live Sessions", "Consultations")
        val breakValues = listOf("Rs 2,840", "Rs 1,200", "Rs 240")
        val breakCounts = listOf("8", "5", "12")
        val breakIcons = listOf(Icons.Default.MenuBook, Icons.Default.VideoCall, Icons.Default.Chat)
        val breakColors = listOf(Color(0xFF3B82F6), Color(0xFF10B981), Color(0xFFF59E0B))
        items(breakLabels.size) { i ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(44.dp).background(breakColors[i].copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(breakIcons[i], contentDescription = null, tint = breakColors[i], modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(breakLabels[i], fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF111827))
                        Text("${breakCounts[i]} completed", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                    }
                    Text(breakValues[i], fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33), fontSize = 15.sp)
                }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}
