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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

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

    // Handle back: sub-screens → main, main → stay (don't logout)
    BackHandler {
        when (currentScreen) {
            "main" -> { /* stay on dashboard */ }
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
                Toast.makeText(context, "$${"%.2f".format(amount)} added to wallet!", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, "Bid of $$bidPrice placed on $courseName!", Toast.LENGTH_SHORT).show()
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

// ══════════════════════════════════════════════════════════════
//  MAIN SCAFFOLD – nav: Home, Learn, Bids, Wallet
// ══════════════════════════════════════════════════════════════
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
    val context = LocalContext.current
    val tabs = listOf("Home", "Learn", "Bids", "Wallet")

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
                        Spacer(Modifier.width(8.dp))
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
                0 -> StudentHomeTab(context, walletBalance, onScreenChange)
                1 -> StudentLearnTab(context, enrolledCourses, onViewCourse)
                2 -> StudentBidsTab(context, onScreenChange)
                else -> StudentWalletTab(context, walletBalance, onScreenChange)
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  TAB 0 – HOME (no names, just "Welcome back!")
// ══════════════════════════════════════════════════════════════
@Composable
fun StudentHomeTab(ctx: android.content.Context, balance: Double, nav: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Welcome back!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth().clickable { nav("wallet_details") }, colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33)), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Wallet Balance", fontSize = 13.sp, color = Color.White)
                    Text("$${"%.2f".format(balance)}", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { nav("add_credits") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Add Credits", color = Color(0xFFEA2A33), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Button(onClick = { nav("wallet_details") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f))) {
                            Icon(Icons.Default.Visibility, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("View", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
        item { Text("My Status", fontSize = 15.sp, fontWeight = FontWeight.Bold) }
        val labels = listOf("Active Courses", "Pending Offers", "Completed")
        val values = listOf("5", "3", "12")
        items(labels.size) { i ->
            Card(modifier = Modifier.fillMaxWidth().clickable { Toast.makeText(ctx, "${labels[i]}: ${values[i]}", Toast.LENGTH_SHORT).show() }, colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) { Text(labels[i], fontWeight = FontWeight.SemiBold, fontSize = 13.sp); Text(values[i], fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33)) }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  TAB 1 – LEARN (two sub-sections: Browse Content + Sessions)
// ══════════════════════════════════════════════════════════════
@Composable
fun StudentLearnTab(ctx: android.content.Context, enrolled: List<String>, onViewCourse: (Int) -> Unit) {
    var subTab by remember { mutableStateOf(0) } // 0 = Browse Content, 1 = Sessions

    val courseNames = listOf("Advanced UI/UX Design", "Kotlin for Android", "Brand Strategy 101", "Mobile Photography")
    val coursePrices = listOf("$40", "$55", "$70", "$85")
    val courseTeachers = listOf("Prof. Sarah", "Prof. Mike", "Prof. Lisa", "Prof. Raj")

    val sessionTitles = listOf("Live Q&A: Design Principles", "Kotlin Workshop", "Brand Building Session")
    val sessionDates = listOf("2026-03-05", "2026-03-08", "2026-03-12")
    val sessionTimes = listOf("10:00 AM", "2:00 PM", "11:00 AM")

    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Learn", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
        // Sub-tab switcher
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { subTab = 0 },
                    modifier = Modifier.weight(1f).height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (subTab == 0) Color(0xFFEA2A33) else Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Browse Content", fontSize = 12.sp, color = if (subTab == 0) Color.White else Color.Black, fontWeight = FontWeight.SemiBold) }
                Button(
                    onClick = { subTab = 1 },
                    modifier = Modifier.weight(1f).height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (subTab == 1) Color(0xFFEA2A33) else Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Sessions", fontSize = 12.sp, color = if (subTab == 1) Color.White else Color.Black, fontWeight = FontWeight.SemiBold) }
            }
        }

        if (subTab == 0) {
            // ── Browse Content ──
            items(courseNames.size) { i ->
                val isEnrolled = enrolled.contains("course_$i")
                Card(modifier = Modifier.fillMaxWidth().clickable { onViewCourse(i) }, colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(courseNames[i], fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("By ${courseTeachers[i]}", fontSize = 11.sp, color = Color.Gray)
                                Spacer(Modifier.height(4.dp))
                                Text("⭐ 4.${7 + i % 3} (${120 + i * 50} reviews)", fontSize = 11.sp, color = Color(0xFFEA2A33))
                            }
                            Text(coursePrices[i], fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33), fontSize = 16.sp)
                        }
                        Spacer(Modifier.height(10.dp))
                        Surface(color = if (isEnrolled) Color(0xFF10B981).copy(alpha = 0.1f) else Color(0xFFEA2A33).copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
                            Text(if (isEnrolled) "✓ Enrolled" else "Available", fontSize = 10.sp, color = if (isEnrolled) Color(0xFF10B981) else Color(0xFFEA2A33), modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                        }
                    }
                }
            }
        } else {
            // ── Sessions ──
            items(sessionTitles.size) { i ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Event, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(28.dp))
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(sessionTitles[i], fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("${sessionDates[i]} • ${sessionTimes[i]}", fontSize = 11.sp, color = Color.Gray)
                        }
                        Surface(color = Color(0xFF10B981).copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
                            Text("Upcoming", fontSize = 10.sp, color = Color(0xFF10B981), fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                        }
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  TAB 2 – BIDS
// ══════════════════════════════════════════════════════════════
@Composable
fun StudentBidsTab(ctx: android.content.Context, nav: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6))) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Text("My Bids", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            items(3) { i ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Bid on Course ${i + 1}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("Design Fundamentals", fontSize = 11.sp, color = Color.Gray)
                            }
                            Surface(color = Color(0xFFF59E0B).copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp)) {
                                Text("Pending", fontSize = 10.sp, color = Color(0xFFF59E0B), fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp)).padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column { Text("Your Bid", fontSize = 10.sp, color = Color.Gray); Text("$${35 + i * 5}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33)) }
                            Text("vs $${45 + i * 5}", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterVertically))
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(72.dp)) }
        }
        FloatingActionButton(onClick = { nav("make_bid") }, containerColor = Color(0xFFEA2A33), modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)) {
            Icon(Icons.Default.Add, contentDescription = "New Bid", tint = Color.White)
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  TAB 3 – WALLET
// ══════════════════════════════════════════════════════════════
@Composable
fun StudentWalletTab(ctx: android.content.Context, balance: Double, nav: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { Text("Wallet & Credits", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
        item {
            Card(modifier = Modifier.fillMaxWidth().clickable { nav("wallet_details") }, colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33)), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Available Balance", fontSize = 12.sp, color = Color.White)
                    Text("$${"%.2f".format(balance)}", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = { nav("add_credits") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(16.dp)); Spacer(Modifier.width(4.dp))
                            Text("Add Credits", color = Color(0xFFEA2A33), fontSize = 12.sp)
                        }
                        Button(onClick = { nav("wallet_details") }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f))) {
                            Icon(Icons.Default.History, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(4.dp))
                            Text("History", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
        item { Text("Recent Transactions", fontWeight = FontWeight.SemiBold, fontSize = 14.sp) }
        val txLabels = listOf("Added Funds", "Course Purchase", "Referral Bonus", "Course Fee")
        val txAmounts = listOf("+$50.00", "-$40.00", "+$25.00", "-$55.00")
        val txColors = listOf(Color(0xFF10B981), Color.Red, Color(0xFF10B981), Color.Red)
        items(txLabels.size) { i ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(when (i) { 0 -> Icons.Default.Add; 1 -> Icons.Default.School; 2 -> Icons.Default.CardGiftcard; else -> Icons.Default.TrendingDown }, contentDescription = null, tint = txColors[i], modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) { Text(txLabels[i], fontWeight = FontWeight.SemiBold, fontSize = 13.sp); Text("2 days ago", fontSize = 11.sp, color = Color.Gray) }
                    Text(txAmounts[i], fontWeight = FontWeight.SemiBold, color = txColors[i], fontSize = 13.sp)
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  ADD CREDITS PAGE
// ══════════════════════════════════════════════════════════════
@Composable
fun StudentAddCreditsScreen(currentBalance: Double, onBack: () -> Unit, onAddCredits: (Double) -> Unit) {
    var selectedAmount by remember { mutableStateOf(0.0) }
    var customAmount by remember { mutableStateOf("") }
    val presets = listOf(10.0, 25.0, 50.0, 100.0, 250.0, 500.0)

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("Add Credits", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33)), shape = RoundedCornerShape(12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Current Balance", fontSize = 12.sp, color = Color.White)
                Text("$${"%.2f".format(currentBalance)}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
        Spacer(Modifier.height(20.dp))
        Text("Select Amount", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Spacer(Modifier.height(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            for (row in presets.chunked(3)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { amount ->
                        val isSelected = selectedAmount == amount && customAmount.isBlank()
                        Button(
                            onClick = { selectedAmount = amount; customAmount = "" },
                            modifier = Modifier.weight(1f).height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = if (isSelected) Color(0xFFEA2A33) else Color.White),
                            shape = RoundedCornerShape(10.dp)
                        ) { Text("$${"%.0f".format(amount)}", color = if (isSelected) Color.White else Color.Black, fontWeight = FontWeight.SemiBold) }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Or enter custom amount", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        OutlinedTextField(value = customAmount, onValueChange = { customAmount = it; selectedAmount = 0.0 }, placeholder = { Text("Custom amount") }, prefix = { Text("$") }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp))
        Spacer(Modifier.height(16.dp))
        Text("Payment Method", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Spacer(Modifier.height(8.dp))
        var method by remember { mutableStateOf("card") }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("card" to "Credit Card", "paypal" to "PayPal", "bank" to "Bank").forEach { (key, label) ->
                Button(
                    onClick = { method = key },
                    modifier = Modifier.weight(1f).height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (method == key) Color(0xFFEA2A33) else Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) { Text(label, fontSize = 10.sp, color = if (method == key) Color.White else Color.Black, fontWeight = FontWeight.SemiBold) }
            }
        }
        Spacer(Modifier.height(24.dp))
        val finalAmount = if (customAmount.isNotBlank()) customAmount.toDoubleOrNull() ?: 0.0 else selectedAmount
        Button(
            onClick = { if (finalAmount > 0) onAddCredits(finalAmount) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33)),
            enabled = finalAmount > 0
        ) { Text("Add $${"%.2f".format(finalAmount)} to Wallet", fontWeight = FontWeight.SemiBold, fontSize = 15.sp) }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(44.dp)) { Text("Cancel") }
        Spacer(Modifier.height(24.dp))
    }
}

// ══════════════════════════════════════════════════════════════
//  COURSE DETAILS
// ══════════════════════════════════════════════════════════════
@Composable
fun StudentCourseDetailScreen(courseIndex: Int, isEnrolled: Boolean, onBack: () -> Unit, onEnroll: () -> Unit, onWatchVideo: () -> Unit) {
    val names = listOf("Advanced UI/UX Design", "Kotlin for Android", "Brand Strategy 101", "Mobile Photography")
    val prices = listOf("$40", "$55", "$70", "$85")
    val name = names.getOrElse(courseIndex) { "Course" }
    val price = prices.getOrElse(courseIndex) { "$50" }

    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                Text("Course Details", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth().height(200.dp).clickable { if (isEnrolled) onWatchVideo() }, colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E)), shape = RoundedCornerShape(12.dp)) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.PlayCircle, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(56.dp))
                        Spacer(Modifier.height(8.dp))
                        Text(if (isEnrolled) "Tap to watch" else "Enroll to watch", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        }
        item {
            Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("⭐ 4.8 (234 reviews)", fontSize = 12.sp, color = Color(0xFFEA2A33))
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Course Info", fontWeight = FontWeight.Bold, fontSize = 14.sp); Spacer(Modifier.height(8.dp))
                    listOf("Duration:" to "8 weeks", "Level:" to "Intermediate", "Students:" to "1,240 enrolled", "Price:" to price).forEach { (k, v) ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(k, fontSize = 12.sp); Text(v, fontWeight = FontWeight.SemiBold, color = if (k == "Price:") Color(0xFFEA2A33) else Color.Unspecified)
                        }
                    }
                }
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("What You'll Learn", fontWeight = FontWeight.Bold, fontSize = 14.sp); Spacer(Modifier.height(8.dp))
                    listOf("Master advanced design principles", "Create responsive layouts", "Build real-world projects").forEach { point ->
                        Row(Modifier.padding(vertical = 3.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                            Text(point, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
        item {
            if (isEnrolled) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF10B981).copy(alpha = 0.1f))) {
                    Text("✓ Enrolled", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(16.dp).fillMaxWidth(), textAlign = TextAlign.Center)
                }
                Spacer(Modifier.height(8.dp))
                Button(onClick = onWatchVideo, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33))) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp)); Spacer(Modifier.width(6.dp))
                    Text("Watch Course", fontWeight = FontWeight.SemiBold)
                }
            } else {
                Button(onClick = onEnroll, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33))) {
                    Text("Enroll Now", fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(44.dp)) { Text("Back") }
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  VIDEO PLAYER (YouTube-style)
// ══════════════════════════════════════════════════════════════
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
                Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(80.dp))
                Spacer(Modifier.height(12.dp))
                Text("Advanced UI/UX Design", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("Video content would load here", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
            }
        }
        LinearProgressIndicator(progress = { 0.35f }, modifier = Modifier.fillMaxWidth().height(3.dp), color = Color(0xFFEA2A33), trackColor = Color.Gray)
        Row(modifier = Modifier.fillMaxWidth().background(Color(0xFF111111)).padding(12.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {}) { Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = Color.White) }
            IconButton(onClick = {}) { Icon(Icons.Default.Replay10, contentDescription = "Rewind", tint = Color.White) }
            IconButton(onClick = {}) {
                Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFEA2A33)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Pause, contentDescription = "Play/Pause", tint = Color.White, modifier = Modifier.size(28.dp))
                }
            }
            IconButton(onClick = {}) { Icon(Icons.Default.Forward10, contentDescription = "Forward", tint = Color.White) }
            IconButton(onClick = {}) { Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = Color.White) }
        }
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFF1A1A1A)).padding(16.dp)) {
            Text("Advanced UI/UX Design", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Lesson 1 of 12", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
        }
    }
}

// ══════════════════════════════════════════════════════════════
//  MAKE BID
// ══════════════════════════════════════════════════════════════
@Composable
fun StudentMakeBidScreen(onBack: () -> Unit, onSubmit: (String, String) -> Unit) {
    val courses = listOf("Advanced UI/UX Design ($40)", "Kotlin for Android ($55)", "Brand Strategy 101 ($70)", "Mobile Photography ($85)")
    var selectedCourse by remember { mutableStateOf(courses[0]) }
    var bidPrice by remember { mutableStateOf(30f) }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("Make a Bid", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(20.dp))
        Text("Select Course", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) { Text(selectedCourse, modifier = Modifier.weight(1f), textAlign = TextAlign.Start, fontSize = 12.sp); Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) { courses.forEach { c -> DropdownMenuItem(text = { Text(c, fontSize = 12.sp) }, onClick = { selectedCourse = c; expanded = false }) } }
        }
        Spacer(Modifier.height(20.dp))
        Text("Your Bid Price", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Spacer(Modifier.height(8.dp))
        Text("$${"%.0f".format(bidPrice)}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Slider(
            value = bidPrice, onValueChange = { bidPrice = it }, valueRange = 5f..200f, steps = 38,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            colors = SliderDefaults.colors(thumbColor = Color(0xFFEA2A33), activeTrackColor = Color(0xFFEA2A33))
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("$5", fontSize = 11.sp, color = Color.Gray); Text("$200", fontSize = 11.sp, color = Color.Gray) }
        Spacer(Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)), shape = RoundedCornerShape(8.dp)) {
            Text("Tip: Competitive bids have a higher chance of being accepted!", fontSize = 11.sp, color = Color(0xFFB45309), modifier = Modifier.padding(12.dp))
        }
        Spacer(Modifier.height(28.dp))
        Button(onClick = { onSubmit(selectedCourse, "${"%.0f".format(bidPrice)}") }, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33))) {
            Text("Submit Bid", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(44.dp)) { Text("Cancel") }
        Spacer(Modifier.height(24.dp))
    }
}

// ══════════════════════════════════════════════════════════════
//  WALLET DETAILS
// ══════════════════════════════════════════════════════════════
@Composable
fun StudentWalletDetailScreen(balance: Double, onBack: () -> Unit, onAddCredits: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F6F6)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                Text("Wallet Details", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33)), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Available Balance", fontSize = 12.sp, color = Color.White)
                    Text("$${"%.2f".format(balance)}", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = onAddCredits, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp))
                        Text("Add More Credits", color = Color(0xFFEA2A33), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
        item { Text("Spending Summary", fontWeight = FontWeight.Bold, fontSize = 14.sp) }
        val summaryLabels = listOf("Spent on Courses", "Referral Earnings", "Total Added")
        val summaryValues = listOf("$250.00", "$125.00", "$${"%.2f".format(balance + 250 - 125)}")
        items(summaryLabels.size) { i ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(summaryLabels[i], fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text(summaryValues[i], fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33), fontSize = 14.sp)
                }
            }
        }
        item { Text("Transaction History", fontWeight = FontWeight.Bold, fontSize = 14.sp) }
        val txLabels = listOf("Added $100", "Purchased Course", "Referral Bonus", "Added $50", "Course Fee")
        val txAmounts = listOf("+$100.00", "-$40.00", "+$25.00", "+$50.00", "-$55.00")
        val txColors = listOf(Color(0xFF10B981), Color.Red, Color(0xFF10B981), Color(0xFF10B981), Color.Red)
        items(txLabels.size) { i ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (txColors[i] == Color.Red) Icons.Default.TrendingDown else Icons.Default.TrendingUp, contentDescription = null, tint = txColors[i], modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) { Text(txLabels[i], fontWeight = FontWeight.SemiBold, fontSize = 12.sp); Text("${5 - i} days ago", fontSize = 10.sp, color = Color.Gray) }
                    Text(txAmounts[i], fontWeight = FontWeight.SemiBold, color = txColors[i], fontSize = 12.sp)
                }
            }
        }
    }
}
