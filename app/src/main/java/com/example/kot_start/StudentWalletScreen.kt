package com.example.kot_start

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kot_start.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentWalletScreen(viewModel: StudentViewModel) {
    val stats by viewModel.stats.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    
    var selectedAmount by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFEA2A33),
                                Color(0xFFD71F28)
                            )
                        )
                    )
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Your Wallet",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                // Premium Wallet Card
                PremiumWalletCard()
            }
        }

        // Quick Add Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Quick Top-up",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(6) { index ->
                        QuickAddButton(
                            amount = listOf(500, 1000, 2000, 5000, 10000, 20000)[index],
                            isSelected = selectedAmount == index,
                            onClick = { selectedAmount = if (selectedAmount == index) null else index }
                        )
                    }
                }

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEA2A33)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = selectedAmount != null
                ) {
                    Text(
                        "Proceed to Payment",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }

        // Transactions Section
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
                        "Transaction History",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    TextButton(onClick = { }) {
                        Text(
                            "View All",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEA2A33)
                            )
                        )
                    }
                }
            }
        }

        items(8) { index ->
            TransactionItem(
                title = listOf(
                    "Course Purchase",
                    "Wallet Top-up",
                    "Refund",
                    "Session Booking",
                    "Bid Accepted",
                    "Cashback",
                    "Course Purchase",
                    "Wallet Top-up"
                )[index],
                description = listOf(
                    "Advanced UI/UX Design",
                    "Credit Card Payment",
                    "Cancelled Course",
                    "React Advanced Session",
                    "Figma Mastery",
                    "Promo Code SAVE20",
                    "Kotlin Basics",
                    "Bank Transfer"
                )[index],
                amount = listOf(
                    -4500,
                    5000,
                    3000,
                    -3000,
                    -2500,
                    500,
                    -3500,
                    10000
                )[index],
                timestamp = listOf(
                    "Today, 2:30 PM",
                    "Yesterday, 1:15 PM",
                    "Mar 25, 11:45 AM",
                    "Mar 24, 6:30 PM",
                    "Mar 23, 3:20 PM",
                    "Mar 22, 9:00 AM",
                    "Mar 21, 4:15 PM",
                    "Mar 20, 2:00 PM"
                )[index]
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun PremiumWalletCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFEA2A33).copy(alpha = 0.9f),
                            Color(0xFF8E1F27)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "SkillIt Card",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        )
                        Text(
                            "Premium",
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .size(40.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                "SK",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Available Balance",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    )

                    Text(
                        "NPR 15,000.00",
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "Card Number",
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            )
                            Text(
                                "**** **** **** 2024",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "Valid Till",
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            )
                            Text(
                                "12/28",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickAddButton(
    amount: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFEA2A33) else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        border = if (isSelected) {
            null
        } else {
            CardDefaults.outlinedCardBorder()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add",
                tint = if (isSelected) Color.White else Color(0xFFEA2A33),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "₨$amount",
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Color.Black
                )
            )
        }
    }
}

@Composable
fun TransactionItem(
    title: String,
    description: String,
    amount: Int,
    timestamp: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Transaction Icon
            Surface(
                modifier = Modifier.size(48.dp),
                color = if (amount > 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        if (amount > 0) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                        contentDescription = if (amount > 0) "Inflow" else "Outflow",
                        tint = if (amount > 0) Color(0xFF4CAF50) else Color(0xFFEA2A33),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Transaction Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    title,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Text(
                    description,
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                )
                Text(
                    timestamp,
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = Color.LightGray
                    )
                )
            }

            // Amount
            Text(
                if (amount > 0) "+₨$amount" else "-₨${Math.abs(amount)}",
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (amount > 0) Color(0xFF4CAF50) else Color(0xFFEA2A33)
                )
            )
        }
    }
}
