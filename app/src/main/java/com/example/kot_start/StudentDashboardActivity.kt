package com.example.kot_start

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun StudentApp() {
    var currentScreen by remember { mutableStateOf("main") }
    var selectedTab by remember { mutableStateOf(0) }
    val context = LocalContext.current
    
    when (currentScreen) {
        "main" -> StudentDashboardScreen(
            onScreenChange = { currentScreen = it },
            selectedTab = selectedTab,
            onTabChange = { selectedTab = it },
            onLogout = {
                Toast.makeText(context, "Logout successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, SkillitLoginActivity::class.java)
                context.startActivity(intent)
                (context as? ComponentActivity)?.finish()
            }
        )
        "course_details" -> CourseDetailsScreen(
            onBack = { currentScreen = "main" },
            onEnroll = {
                Toast.makeText(context, "Successfully enrolled!", Toast.LENGTH_SHORT).show()
                currentScreen = "main"
            }
        )
        "course_category" -> CourseCategoryScreen(
            onBack = { currentScreen = "main" }
        )
        "make_offer" -> MakeOfferScreen(
            onBack = { currentScreen = "main" },
            onSubmit = { price ->
                Toast.makeText(context, "Offer of $$price submitted!", Toast.LENGTH_SHORT).show()
                currentScreen = "main"
            }
        )
        "wallet_details" -> WalletDetailsScreen(
            onBack = { currentScreen = "main" }
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    onScreenChange: (String) -> Unit,
    selectedTab: Int,
    onTabChange: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val tabs = listOf("Dashboard", "Learn", "Offers", "Wallet")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color(0xFFEA2A33))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
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
    ) {
        when (selectedTab) {
            0 -> StudentDashboardTabContent(context, onScreenChange)
            1 -> StudentLearnTabContent(context, onScreenChange)
            2 -> StudentOffersTabContent(context, onScreenChange)
            else -> StudentWalletTabContent(context, onScreenChange)
        }
    }
}

@Composable
fun StudentDashboardTabContent(context: android.content.Context, onScreenChange: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(16.dp, 16.dp, 16.dp, 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text("Welcome back!", fontSize = 18.sp, color = Color.Gray)
                Text("Alex Johnson", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clickable { onScreenChange("wallet_details") },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Wallet Balance", fontSize = 14.sp, color = Color.White)
                    Text("$1,245.75", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Add funds opening...", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Funds", color = Color(0xFFEA2A33), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
        
        item {
            Text("My Status", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        
        items(3) { i ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Toast.makeText(context, "${listOf("Active Courses", "Pending Offers", "Completed")[i]} details", Toast.LENGTH_SHORT).show()
                    },
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            listOf("Active Courses", "Pending Offers", "Completed Courses")[i],
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            listOf("5", "3", "12")[i],
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEA2A33)
                        )
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun StudentLearnTabContent(context: android.content.Context, onScreenChange: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(16.dp, 16.dp, 16.dp, 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Browse Courses", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { onScreenChange("course_category") }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = Color(0xFFEA2A33))
                }
            }
        }
        
        items(4) { i ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onScreenChange("course_details") },
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Advanced Course ${i + 1}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("By Professor Sarah", fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("⭐ 4.8 (234 reviews)", fontSize = 11.sp, color = Color(0xFFEA2A33))
                        }
                        Text("$${40 + i * 15}", fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0xFFEA2A33).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text("Available", fontSize = 10.sp, color = Color(0xFFEA2A33), modifier = Modifier.padding(6.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StudentOffersTabContent(context: android.content.Context, onScreenChange: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(16.dp, 16.dp, 16.dp, 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("My Pending Offers", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                FloatingActionButton(
                    onClick = { onScreenChange("make_offer") },
                    containerColor = Color(0xFFEA2A33),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
        
        items(3) { i ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Course Offer ${i + 1}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Design Fundamentals", fontSize = 11.sp, color = Color.Gray)
                            Text("Prof. Sarah", fontSize = 11.sp, color = Color.Gray)
                        }
                        Text("Pending", fontSize = 10.sp, color = Color(0xFFF59E0B), fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Your Offer", fontSize = 11.sp, color = Color.Gray)
                            Text("$${35 + i * 5}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                        }
                        Text("vs \$${45 + i * 5}", fontSize = 11.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Offer cancelled", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F4F6))
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Cancel", fontSize = 11.sp, color = Color.Black)
                        }
                        Button(
                            onClick = {
                                Toast.makeText(context, "Offer ${i + 1} accepted!", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33))
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Accept", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudentWalletTabContent(context: android.content.Context, onScreenChange: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(16.dp, 16.dp, 16.dp, 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Wallet & Credits", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable { onScreenChange("wallet_details") },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Available Balance", fontSize = 12.sp, color = Color.White)
                    Text("$1,245.75", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Add funds initiated", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Funds", color = Color(0xFFEA2A33), fontSize = 12.sp)
                        }
                        Button(
                            onClick = {
                                Toast.makeText(context, "History opening...", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f))
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
            Text("Recent Transactions", fontWeight = FontWeight.SemiBold)
        }
        
        items(4) { i ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        when (i) {
                            0 -> Icons.Default.Add
                            1 -> Icons.Default.School
                            2 -> Icons.Default.CardGiftcard
                            else -> Icons.Default.TrendingDown
                        },
                        contentDescription = null,
                        tint = if (i == 3) Color.Red else Color(0xFFEA2A33),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            listOf("Added Funds", "Course Purchase", "Referral Bonus", "Course Fee")[i],
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Text("2 days ago", fontSize = 11.sp, color = Color.Gray)
                    }
                    Text(
                        if (i == 3) "-\$${"%.2f".format(50)}" else "+\$${"%.2f".format(50 * (i + 1))}",
                        fontWeight = FontWeight.SemiBold,
                        color = if (i == 3) Color.Red else Color(0xFF10B981),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CourseDetailsScreen(onBack: () -> Unit, onEnroll: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text("Course Details", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }
        }
        
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.OndemandVideo, contentDescription = null, tint = Color.White, modifier = Modifier.size(80.dp))
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                Text("Advanced UI/UX Design", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("By Professor Sarah", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text("⭐ 4.8 (234 reviews)", fontSize = 12.sp, color = Color(0xFFEA2A33))
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Course Info", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Duration:", fontSize = 12.sp)
                        Text("8 weeks", fontWeight = FontWeight.SemiBold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Level:", fontSize = 12.sp)
                        Text("Intermediate", fontWeight = FontWeight.SemiBold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Students:", fontSize = 12.sp)
                        Text("1,240 enrolled", fontWeight = FontWeight.SemiBold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Price:", fontSize = 12.sp)
                        Text("$49.99", fontWeight = FontWeight.SemiBold, color = Color(0xFFEA2A33))
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("What You'll Learn", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    repeat(3) { i ->
                        Row(modifier = Modifier.padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                            Text("Learning point ${i + 1}", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onEnroll,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33))
            ) {
                Text("Enroll Now", fontWeight = FontWeight.SemiBold)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
fun CourseCategoryScreen(onBack: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text("Categories", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }
        }
        
        items(5) { i ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBack() },
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            when (i) {
                                0 -> Icons.Default.Palette
                                1 -> Icons.Default.Computer
                                2 -> Icons.Default.BusinessCenter
                                3 -> Icons.Default.FitnessCenter
                                else -> Icons.Default.MoreHoriz
                            },
                            contentDescription = null,
                            tint = Color(0xFFEA2A33),
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text(
                                listOf("Design", "Technology", "Business", "Lifestyle", "Other")[i],
                                fontWeight = FontWeight.SemiBold
                            )
                            Text("${15 + i * 3} courses", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun MakeOfferScreen(onBack: () -> Unit, onSubmit: (String) -> Unit) {
    var offerPrice by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Make an Offer", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Course:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                Text("Advanced UI/UX Design", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Normal Price:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                Text("$49.99", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text("Your Offer Price", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        OutlinedTextField(
            value = offerPrice,
            onValueChange = { offerPrice = it },
            placeholder = { Text("Enter price in $") },
            prefix = { Text("$") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "Tips: Make a reasonable offer. Professors prefer competitive prices!",
                fontSize = 11.sp,
                color = Color(0xFFB45309),
                modifier = Modifier.padding(12.dp)
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { if (offerPrice.isNotEmpty()) onSubmit(offerPrice) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33))
        ) {
            Text("Submit Offer", fontWeight = FontWeight.SemiBold)
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Cancel")
        }
    }
}

@Composable
fun WalletDetailsScreen(onBack: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text("Wallet Details", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }
        }
        
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Available Balance", fontSize = 12.sp, color = Color.White)
                        Text("$1,245.75", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text("Last Updated: Today at 2:30 PM", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Quick Stats", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        
        items(3) { i ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(listOf("Spent on Courses", "Referral Earnings", "Available")[i], fontWeight = FontWeight.SemiBold)
                        Text("$${listOf("250", "125", "1245.75")[i]}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                    }
                }
            }
        }
    }
}
