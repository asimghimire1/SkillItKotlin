package com.example.kot_start

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.ui.draw.shadow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCreditsScreen(
    currentBalance: Double = 2450.00,
    onBackClick: () -> Unit = {},
    onProceedClick: (amount: Double, method: String) -> Unit = { _, _ -> }
) {
    var selectedPackage by remember { mutableStateOf(0) }
    var selectedPaymentMethod by remember { mutableStateOf("") }

    val packages = listOf(
        Triple("NPR 500", "Starter Pack", 0),
        Triple("NPR 1,000", "Standard Pack + 50 Bonus", 1),
        Triple("NPR 5,000", "Premium Pack + 500 Bonus", 2)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    "Add Credits",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            ),
            modifier = Modifier.shadow(elevation = 2.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current Balance Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEA2A33)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Current Balance",
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Text(
                            "NPR ${String.format("%.2f", currentBalance)}",
                            style = TextStyle(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Secure",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                "Secure Student Wallet",
                                style = TextStyle(
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            )
                        }
                    }
                }
            }

            // Select Credit Package Section
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Select Credit Package",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Text(
                            "Best Value",
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEA2A33)
                            )
                        )
                    }

                    // Package Cards
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        repeat(3) { index ->
                            CreditPackageCard(
                                title = packages[index].first,
                                subtitle = packages[index].second,
                                icon = when (index) {
                                    0 -> "🎁"
                                    1 -> "🚀"
                                    else -> "💎"
                                },
                                isSelected = selectedPackage == index,
                                isBestValue = index == 1,
                                onClick = { selectedPackage = index }
                            )
                        }
                    }
                }
            }

            // Supported Payment Methods Section
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "SUPPORTED PAYMENT METHODS",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            letterSpacing = 0.5.sp
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PaymentMethodButton(
                            icon = "📱",
                            label = "Esewa",
                            isSelected = selectedPaymentMethod == "esewa",
                            onClick = { selectedPaymentMethod = "esewa" }
                        )

                        PaymentMethodButton(
                            icon = "📲",
                            label = "Khalti",
                            isSelected = selectedPaymentMethod == "khalti",
                            onClick = { selectedPaymentMethod = "khalti" }
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "🏦 BANK",
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                            )
                        }
                    }
                }
            }

            // Proceed Button
            item {
                Button(
                    onClick = {
                        val amount = when (selectedPackage) {
                            0 -> 500.0
                            1 -> 1000.0
                            else -> 5000.0
                        }
                        onProceedClick(amount, selectedPaymentMethod)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEA2A33),
                        disabledContainerColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = selectedPaymentMethod.isNotEmpty()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Proceed to Payment",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            "→",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun CreditPackageCard(
    title: String,
    subtitle: String,
    icon: String,
    isSelected: Boolean,
    isBestValue: Boolean = false,
    onClick: () -> Unit = {}
) {
    val borderColor = if (isSelected) Color(0xFFEA2A33) else Color.LightGray
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        icon,
                        style = TextStyle(fontSize = 28.sp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            title,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Text(
                            subtitle,
                            style = TextStyle(
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        )
                    }
                }

                // Radio Button
                RadioButton(
                    selected = isSelected,
                    onClick = onClick,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFFEA2A33),
                        unselectedColor = Color.LightGray
                    )
                )
            }

            // Best Value Badge
            if (isBestValue) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    color = Color(0xFFEA2A33),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        "RECOMMENDED",
                        style = TextStyle(
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentMethodButton(
    icon: String,
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val borderColor = if (isSelected) Color(0xFFEA2A33) else Color.LightGray
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Box(
        modifier = Modifier
            .height(50.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                icon,
                style = TextStyle(fontSize = 20.sp)
            )
            Text(
                label,
                style = TextStyle(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
    }
}
