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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

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

    // Handle back: if on a sub-screen go back to main, if on main go back to previous tab or do nothing
    BackHandler {
        when (currentScreen) {
            "main" -> { /* do nothing – stay on dashboard */ }
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

// ══════════════════════════════════════════════════════════════
//  MAIN SCAFFOLD – Tabs: Home, Learning, Bids, Earnings
// ══════════════════════════════════════════════════════════════
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
    val context = LocalContext.current
    val tabs = listOf("Home", "Learning", "Bids", "Earnings")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "SkillIt",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SkillIt", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                    }
                },
                actions = {
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
                                else -> Icon(Icons.Default.AttachMoney, contentDescription = tab)
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
                0 -> TeacherHomeTab(context, onScreenChange)
                1 -> TeacherLearningTab(context, onScreenChange, uploadedContent, scheduledSessions)
                2 -> TeacherBidsTab(context, onScreenChange)
                else -> TeacherEarningsTab(context, onScreenChange)
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  TAB 0 – HOME (no names, just "Welcome back!")
// ══════════════════════════════════════════════════════════════
@Composable
fun TeacherHomeTab(ctx: android.content.Context, nav: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Welcome back!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Total Earnings", fontSize = 13.sp, color = Color.White)
                    Text("$4,280.50", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Button(
                        onClick = { nav("earnings_details") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Icon(Icons.Default.Payment, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Withdraw / View Earnings", color = Color(0xFFEA2A33), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
        item { Text("Quick Stats", fontSize = 15.sp, fontWeight = FontWeight.Bold) }
        val labels = listOf("Active Students", "Courses Published", "Sessions Done")
        val values = listOf("1,240", "6", "28")
        items(labels.size) { i ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(labels[i], fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text(values[i], fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  TAB 1 – LEARNING (two sections: My Content + My Sessions)
// ══════════════════════════════════════════════════════════════
@Composable
fun TeacherLearningTab(ctx: android.content.Context, nav: (String) -> Unit, uploaded: List<TeacherContent>, sessions: List<TeacherSession>) {
    var subTab by remember { mutableStateOf(0) } // 0 = My Content, 1 = My Sessions

    val demo = listOf(
        TeacherContent("UI/UX Fundamentals", "Learn design basics", "Design", "$29", true),
        TeacherContent("Kotlin Crash Course", "Android dev essentials", "Technology", "Free", false),
        TeacherContent("Brand Strategy", "Marketing your brand", "Business", "$19", true),
    )
    val allContent = demo + uploaded

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6))) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Text("Learning", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            // Sub-tab row
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { subTab = 0 },
                        modifier = Modifier.weight(1f).height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (subTab == 0) Color(0xFFEA2A33) else Color.White),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("My Content", fontSize = 12.sp, color = if (subTab == 0) Color.White else Color.Black, fontWeight = FontWeight.SemiBold) }
                    Button(
                        onClick = { subTab = 1 },
                        modifier = Modifier.weight(1f).height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (subTab == 1) Color(0xFFEA2A33) else Color.White),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("My Sessions", fontSize = 12.sp, color = if (subTab == 1) Color.White else Color.Black, fontWeight = FontWeight.SemiBold) }
                }
            }

            if (subTab == 0) {
                // ── My Content section ──
                item { Text("My Content (${allContent.size})", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray) }
                items(allContent.size) { i ->
                    val c = allContent[i]
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(c.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text(c.description, fontSize = 11.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Surface(color = Color(0xFFEA2A33).copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
                                        Text(c.category, fontSize = 10.sp, color = Color(0xFFEA2A33), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                    Surface(color = if (c.isPaid) Color(0xFF10B981).copy(alpha = 0.1f) else Color(0xFF3B82F6).copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
                                        Text(if (c.isPaid) c.price else "Free", fontSize = 10.sp, color = if (c.isPaid) Color(0xFF10B981) else Color(0xFF3B82F6), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                            }
                            Icon(Icons.Default.OndemandVideo, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(28.dp))
                        }
                    }
                }
            } else {
                // ── My Sessions section ──
                if (sessions.isEmpty()) {
                    item {
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Event, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(40.dp))
                                Spacer(Modifier.height(8.dp))
                                Text("No sessions scheduled yet", fontSize = 13.sp, color = Color.Gray)
                                Text("Tap + to schedule one", fontSize = 11.sp, color = Color.LightGray)
                            }
                        }
                    }
                } else {
                    item { Text("Scheduled Sessions (${sessions.size})", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray) }
                    items(sessions.size) { i ->
                        val s = sessions[i]
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Event, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(24.dp))
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(s.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text("${s.date} • ${s.time} • ${s.duration}", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
        // FAB – add content or session depending on sub-tab
        FloatingActionButton(
            onClick = { if (subTab == 0) nav("add_content") else nav("add_session") },
            containerColor = Color(0xFFEA2A33),
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
        ) { Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White) }
    }
}

// ══════════════════════════════════════════════════════════════
//  TAB 2 – BIDS (student bids only, no sessions here)
// ══════════════════════════════════════════════════════════════
@Composable
fun TeacherBidsTab(ctx: android.content.Context, nav: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Student Bids", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
        items(3) { i ->
            Card(modifier = Modifier.fillMaxWidth().clickable { nav("bid_details") }, colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Student ${i + 1} Offer", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("Advanced Design Course", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp)).padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column { Text("Offered", fontSize = 10.sp, color = Color.Gray); Text("$${40 + i * 5}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33)) }
                        Text("Your Rate: $45", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterVertically))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { Toast.makeText(ctx, "Counter offer sent", Toast.LENGTH_SHORT).show() }, modifier = Modifier.weight(1f).height(38.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F4F6))) { Text("Counter", fontSize = 11.sp, color = Color.Black) }
                        Button(onClick = { Toast.makeText(ctx, "Bid accepted!", Toast.LENGTH_SHORT).show() }, modifier = Modifier.weight(1f).height(38.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33))) { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)); Spacer(Modifier.width(4.dp)); Text("Accept", fontSize = 11.sp) }
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  TAB 3 – EARNINGS
// ══════════════════════════════════════════════════════════════
@Composable
fun TeacherEarningsTab(ctx: android.content.Context, nav: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { Text("Wallet & Earnings", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
        item {
            Card(modifier = Modifier.fillMaxWidth().clickable { nav("earnings_details") }, colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33)), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Total Available Balance", fontSize = 12.sp, color = Color.White)
                    Text("$4,280.50", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = { nav("earnings_details") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.White)) { Icon(Icons.Default.Payment, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text("Withdraw", color = Color(0xFFEA2A33), fontSize = 12.sp) }
                        Button(onClick = { nav("earnings_details") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f))) { Icon(Icons.Default.History, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text("History", color = Color.White, fontSize = 12.sp) }
                    }
                }
            }
        }
        item { Text("Recent Transactions", fontWeight = FontWeight.SemiBold, fontSize = 14.sp) }
        val txLabels = listOf("Withdrawal", "Course Sale", "Session Fee")
        val txAmounts = listOf("100.00", "200.00", "300.00")
        items(txLabels.size) { i ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(when (i) { 0 -> Icons.Default.AccountBalanceWallet; 1 -> Icons.Default.MenuBook; else -> Icons.Default.TrendingUp }, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) { Text(txLabels[i], fontWeight = FontWeight.SemiBold, fontSize = 13.sp); Text("2 days ago", fontSize = 11.sp, color = Color.Gray) }
                    Text("+$${txAmounts[i]}", fontWeight = FontWeight.SemiBold, color = Color(0xFF10B981), fontSize = 13.sp)
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  ADD CONTENT SCREEN
// ══════════════════════════════════════════════════════════════
@Composable
fun TeacherAddContentScreen(onBack: () -> Unit, onSave: (TeacherContent) -> Unit) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Design") }
    var isPaid by remember { mutableStateOf(false) }
    var price by remember { mutableStateOf("") }
    val categories = listOf("Design", "Technology", "Business", "Lifestyle", "Other")

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("Upload Content", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(20.dp))
        Text("Title *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        OutlinedTextField(value = title, onValueChange = { title = it }, placeholder = { Text("e.g. Advanced UI Design") }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp))
        Spacer(Modifier.height(12.dp))
        Text("Description", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        OutlinedTextField(value = desc, onValueChange = { desc = it }, placeholder = { Text("What will students learn?") }, modifier = Modifier.fillMaxWidth().height(100.dp).padding(vertical = 6.dp), maxLines = 4)
        Spacer(Modifier.height(12.dp))
        Text("Category", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) { Text(category, modifier = Modifier.weight(1f), textAlign = TextAlign.Start); Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) { categories.forEach { cat -> DropdownMenuItem(text = { Text(cat) }, onClick = { category = cat; expanded = false }) } }
        }
        Spacer(Modifier.height(16.dp))
        Text("Pricing", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { isPaid = false }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (!isPaid) Color(0xFFEA2A33) else Color(0xFFF3F4F6))) { Text("Free", color = if (!isPaid) Color.White else Color.Black) }
            Button(onClick = { isPaid = true }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = if (isPaid) Color(0xFFEA2A33) else Color(0xFFF3F4F6))) { Text("Paid", color = if (isPaid) Color.White else Color.Black) }
        }
        if (isPaid) {
            OutlinedTextField(value = price, onValueChange = { price = it }, placeholder = { Text("Price in $") }, prefix = { Text("$") }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp))
        }
        Spacer(Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth().height(120.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)), shape = RoundedCornerShape(12.dp)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(36.dp))
                    Spacer(Modifier.height(6.dp))
                    Text("Tap to upload video / file", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { if (title.isNotBlank()) onSave(TeacherContent(title, desc.ifBlank { "No description" }, category, if (isPaid) "$$price" else "Free", isPaid)) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33)),
            enabled = title.isNotBlank()
        ) { Text("Upload Content", fontWeight = FontWeight.SemiBold) }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(44.dp)) { Text("Cancel") }
        Spacer(Modifier.height(32.dp))
    }
}

// ══════════════════════════════════════════════════════════════
//  ADD SESSION SCREEN
// ══════════════════════════════════════════════════════════════
@Composable
fun TeacherAddSessionScreen(onBack: () -> Unit, onSave: (TeacherSession) -> Unit) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("1 hour") }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("Schedule Session", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(20.dp))
        Text("Session Title *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        OutlinedTextField(value = title, onValueChange = { title = it }, placeholder = { Text("e.g. Live Q&A: Design Principles") }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp))
        Spacer(Modifier.height(12.dp))
        Text("Date *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        OutlinedTextField(value = date, onValueChange = { date = it }, placeholder = { Text("YYYY-MM-DD") }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp))
        Spacer(Modifier.height(12.dp))
        Text("Time *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        OutlinedTextField(value = time, onValueChange = { time = it }, placeholder = { Text("e.g. 10:00 AM") }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp))
        Spacer(Modifier.height(12.dp))
        Text("Duration", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) { Text(duration, modifier = Modifier.weight(1f), textAlign = TextAlign.Start); Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                listOf("30 min", "1 hour", "1.5 hours", "2 hours").forEach { d -> DropdownMenuItem(text = { Text(d) }, onClick = { duration = d; expanded = false }) }
            }
        }
        Spacer(Modifier.height(28.dp))
        Button(
            onClick = { if (title.isNotBlank() && date.isNotBlank() && time.isNotBlank()) onSave(TeacherSession(title, date, time, duration)) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33)),
            enabled = title.isNotBlank() && date.isNotBlank() && time.isNotBlank()
        ) { Text("Schedule Session", fontWeight = FontWeight.SemiBold) }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(44.dp)) { Text("Cancel") }
        Spacer(Modifier.height(32.dp))
    }
}

// ══════════════════════════════════════════════════════════════
//  BID DETAILS
// ══════════════════════════════════════════════════════════════
@Composable
fun TeacherBidDetailsScreen(onBack: () -> Unit) {
    val ctx = LocalContext.current
    var accepted by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("Bid Details", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Student Information", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                Text("Rating: 4.8/5 ⭐", fontSize = 12.sp); Text("Previous Bids: 5", fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(14.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Price Comparison", fontWeight = FontWeight.Bold, fontSize = 14.sp); Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Your Rate:", fontSize = 12.sp); Text("$45", fontWeight = FontWeight.Bold) }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Student Offer:", fontSize = 12.sp); Text("$40", fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33)) }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Difference:", fontSize = 12.sp); Text("-$5", fontWeight = FontWeight.Bold, color = Color.Red) }
            }
        }
        Spacer(Modifier.weight(1f))
        if (accepted) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF10B981).copy(alpha = 0.1f))) {
                Text("✓ Bid Accepted", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp).fillMaxWidth(), textAlign = TextAlign.Center)
            }
        } else {
            Button(onClick = { accepted = true; Toast.makeText(ctx, "Bid accepted!", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33))) { Text("Accept Bid", fontWeight = FontWeight.SemiBold) }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(44.dp)) { Text("Back") }
        Spacer(Modifier.height(16.dp))
    }
}

// ══════════════════════════════════════════════════════════════
//  EARNINGS DETAILS
// ══════════════════════════════════════════════════════════════
@Composable
fun TeacherEarningsDetailsScreen(onBack: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                Text("Earnings Details", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33)), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total Balance", fontSize = 12.sp, color = Color.White)
                    Text("$4,280.50", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(6.dp))
                    Text("Last Updated: Today at 2:30 PM", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }
        item { Text("Income Breakdown", fontWeight = FontWeight.Bold, fontSize = 14.sp) }
        val breakLabels = listOf("Courses", "Live Sessions", "Consultations")
        val breakValues = listOf("$2,840", "$1,200", "$240")
        val breakCounts = listOf("8", "5", "12")
        items(breakLabels.size) { i ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) { Text(breakLabels[i], fontWeight = FontWeight.SemiBold); Text("${breakCounts[i]} completed", fontSize = 11.sp, color = Color.Gray) }
                    Text(breakValues[i], fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}
