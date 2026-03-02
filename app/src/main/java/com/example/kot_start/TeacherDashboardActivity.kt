package com.example.kot_start

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.view.WindowCompat

class TeacherDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent { TeacherDashboardScreen() }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Dashboard", "Content", "Bids", "Earnings")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teacher Dashboard", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Default.Dashboard, contentDescription = tab)
                                1 -> Icon(Icons.Default.OndemandVideo, contentDescription = tab)
                                2 -> Icon(Icons.Default.TrendingUp, contentDescription = tab)
                                else -> Icon(Icons.Default.AttachMoney, contentDescription = tab)
                            }
                        },
                        label = { Text(tab, fontSize = 10.sp) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
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
            0 -> DashboardTab()
            1 -> ContentTab()
            2 -> BidsTab()
            else -> EarningsTab()
        }
    }
}

@Composable
fun DashboardTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Welcome, Professor!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("This Month", fontSize = 12.sp, color = Color.White)
                    Text("$4,280.50", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Earnings", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }
        
        item {
            Text("Quick Stats", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        
        repeat(3) { i ->
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Stat ${i + 1}", fontWeight = FontWeight.SemiBold)
                            Text("Value: ${100 * (i + 1)}", fontSize = 12.sp, color = Color.Gray)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun ContentTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("My Content", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        
        repeat(4) { i ->
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Course ${i + 1}", fontWeight = FontWeight.SemiBold)
                            Text("${50 + i * 10} students enrolled", fontSize = 11.sp, color = Color.Gray)
                        }
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun BidsTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Student Bids", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        
        repeat(3) { i ->
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Student ${i + 1} Offer", fontWeight = FontWeight.SemiBold)
                        Text("Course: Advanced Design", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {}, modifier = Modifier.weight(1f)) {
                                Text("Counter", fontSize = 11.sp)
                            }
                            Button(onClick = {}, modifier = Modifier.weight(1f)) {
                                Text("Accept", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EarningsTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Earnings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text("Total Balance", fontSize = 12.sp, color = Color.White)
                    Text("$4,280.50", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("Withdraw", color = Color(0xFFEA2A33))
                    }
                }
            }
        }
        
        item {
            Text("Recent Transactions", fontWeight = FontWeight.SemiBold)
        }
        
        repeat(3) { i ->
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Transaction $i", fontWeight = FontWeight.SemiBold)
                            Text("2 days ago", fontSize = 11.sp, color = Color.Gray)
                        }
                        Text("+\$${"%.2f".format(100 * (i + 1))}", fontWeight = FontWeight.SemiBold, color = Color(0xFF10B981))
                    }
                }
            }
        }
    }
}

@Composable
fun SelectDropdown(
    selectedValue: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Box {
        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F4F6))
        ) {
            Text(selectedValue, color = Color.Black)
        }
        
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PricingToggleButton(
    isPaid: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = { onToggle(false) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isPaid) Color(0xFFEA2A33) else Color(0xFFF3F4F6)
            )
        ) {
            Text("Free", color = if (!isPaid) Color.White else Color.Black)
        }
        Button(
            onClick = { onToggle(true) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPaid) Color(0xFFEA2A33) else Color(0xFFF3F4F6)
            )
        ) {
            Text("Paid", color = if (isPaid) Color.White else Color.Black)
        }
    }
}

@Composable
fun TabButton(
    title: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) Color(0xFFEA2A33) else Color.Transparent
        )
    ) {
        Text(title, color = if (isActive) Color.White else Color.Black)
    }
}

@Composable
fun TabButtonBid(
    title: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) Color(0xFFEA2A33) else Color.Transparent
        )
    ) {
        Text(title, color = if (isActive) Color.White else Color.Black)
    }
}
