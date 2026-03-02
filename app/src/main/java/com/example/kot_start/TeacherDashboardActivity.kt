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
    val context = LocalContext.current
    val tabs = listOf("Dashboard", "Content", "Bids", "Earnings")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teacher Dashboard", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Logout successful", Toast.LENGTH_SHORT).show()
                        // Navigation to login would go here
                    }) {
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
            0 -> DashboardTab(context)
            1 -> ContentTab(context)
            2 -> BidsTab(context)
            else -> EarningsTab(context)
        }
    }
}

@Composable
fun DashboardTab(context: android.content.Context) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text("Welcome back!", fontSize = 18.sp, color = Color.Gray)
                Text("Professor Sarah", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
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
                    Text("Total Earnings", fontSize = 14.sp, color = Color.White)
                    Text("$4,280.50", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Add Credits feature coming soon", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text("Add Credits", color = Color(0xFFEA2A33), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Button(
                            onClick = {
                                Toast.makeText(context, "Withdraw initiated", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f))
                        ) {
                            Text("Withdraw", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
        
        item {
            Text("Quick Stats", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        
        items(3) { i ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Toast.makeText(context, "Stat ${i + 1} clicked", Toast.LENGTH_SHORT).show()
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
                            listOf("Active Students", "Total Earnings", "Sessions Completed")[i],
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            listOf("1,240", "$4,280.50", "28")[i],
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
fun ContentTab(context: android.content.Context) {
    var showAddContent by remember { mutableStateOf(false) }
    
    if (showAddContent) {
        AddContentDialog(
            onDismiss = { showAddContent = false },
            onAdd = { title, category ->
                Toast.makeText(context, "Content '$title' added successfully", Toast.LENGTH_SHORT).show()
                showAddContent = false
            }
        )
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("My Content", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                FloatingActionButton(
                    onClick = { showAddContent = true },
                    containerColor = Color(0xFFEA2A33),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
        
        items(4) { i ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Toast.makeText(context, "Course ${i + 1} selected", Toast.LENGTH_SHORT).show()
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
                        Text("Course ${i + 1}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text("${50 + i * 10} students", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            modifier = Modifier,
                            color = Color(0xFFEA2A33).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                "Published",
                                fontSize = 10.sp,
                                color = Color(0xFFEA2A33),
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                    IconButton(onClick = {
                        Toast.makeText(context, "Edit Course ${i + 1}", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFFEA2A33))
                    }
                }
            }
        }
    }
}

@Composable
fun BidsTab(context: android.content.Context) {
    var showAddSession by remember { mutableStateOf(false) }
    
    if (showAddSession) {
        AddSessionDialog(
            onDismiss = { showAddSession = false },
            onAdd = { title, date ->
                Toast.makeText(context, "Session '$title' scheduled for $date", Toast.LENGTH_SHORT).show()
                showAddSession = false
            }
        )
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFFEA2A33), modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Student Bids", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                FloatingActionButton(
                    onClick = { showAddSession = true },
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
                    Text("Student ${i + 1} Offer", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("Advanced Design Course", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Offered Price", fontSize = 11.sp, color = Color.Gray)
                            Text("$${40 + i * 5}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                        }
                        Text("Your Rate: $45", fontSize = 11.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Counter offer sent to Student ${i + 1}", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F4F6))
                        ) {
                            Text("Counter", fontSize = 11.sp, color = Color.Black)
                        }
                        Button(
                            onClick = {
                                Toast.makeText(context, "Bid accepted from Student ${i + 1}!", Toast.LENGTH_LONG).show()
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
fun EarningsTab(context: android.content.Context) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Wallet & Earnings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
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
                    Text("Total Available Balance", fontSize = 12.sp, color = Color.White)
                    Text("$4,280.50", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Withdrawal initiated - Pending verification", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Withdraw", color = Color(0xFFEA2A33), fontSize = 12.sp)
                        }
                        Button(
                            onClick = {
                                Toast.makeText(context, "Withdrawal history loaded", Toast.LENGTH_SHORT).show()
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
        
        items(3) { i ->
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
                            0 -> Icons.Default.AccountBalanceWallet
                            1 -> Icons.Default.MenuBook
                            else -> Icons.Default.TrendingUp
                        },
                        contentDescription = null,
                        tint = Color(0xFFEA2A33),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            listOf("Withdrawal", "Course Sale", "Session Fee")[i],
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Text("2 days ago", fontSize = 11.sp, color = Color.Gray)
                    }
                    Text(
                        "+\$${"%.2f".format(100 * (i + 1))}",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF10B981),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AddContentDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Design") }
    val categories = listOf("Design", "Technology", "Business", "Lifestyle")
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Upload Content") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Content Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                SelectDropdown(options = categories) { newCategory -> category = newCategory }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotEmpty()) onAdd(title, category) }
            ) {
                Text("Upload")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddSessionDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Schedule Session") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Session Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotEmpty() && date.isNotEmpty()) onAdd(title, date) }
            ) {
                Text("Schedule")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SelectDropdown(
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedValue by remember { mutableStateOf(options.firstOrNull() ?: "") }
    
    Box {
        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedValue, modifier = Modifier.weight(1f), textAlign = TextAlign.Start)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
        
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedValue = option
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
