package com.example.kot_start

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentBidsScreen() {
    var selectedBid by remember { mutableStateOf<Int?>(null) }
    var showNegotiationSheet by remember { mutableStateOf(false) }
    var bidAmount by remember { mutableStateOf(0f) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "My Bids",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }

        items(5) { index ->
            BidCard(
                itemTitle = listOf(
                    "Advanced UI Design Course",
                    "Mobile App Dev Session",
                    "Figma Mastery Course",
                    "React Native Workshop",
                    "Web Design Bootcamp"
                )[index],
                originalPrice = listOf(5000, 3000, 4500, 6000, 5500)[index],
                yourBid = listOf(4000, 2500, 4000, 5000, 4500)[index],
                status = listOf("Pending", "Accepted", "Countered", "Rejected", "Pending")[index],
                negotiationProgress = listOf(0.6f, 1f, 0.75f, 0f, 0.5f)[index],
                onClick = {
                    selectedBid = index
                    showNegotiationSheet = true
                }
            )
        }
    }

    // Negotiation Bottom Sheet
    if (showNegotiationSheet && selectedBid != null) {
        BidNegotiationSheet(
            itemTitle = listOf(
                "Advanced UI Design Course",
                "Mobile App Dev Session",
                "Figma Mastery Course",
                "React Native Workshop",
                "Web Design Bootcamp"
            )[selectedBid!!],
            originalPrice = listOf(5000, 3000, 4500, 6000, 5500)[selectedBid!!],
            currentBid = listOf(4000, 2500, 4000, 5000, 4500)[selectedBid!!],
            minPrice = listOf(3500, 2000, 3800, 5000, 4000)[selectedBid!!],
            onDismiss = { showNegotiationSheet = false }
        )
    }
}

@Composable
fun BidCard(
    itemTitle: String,
    originalPrice: Int,
    yourBid: Int,
    status: String,
    negotiationProgress: Float,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Item with Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    color = Color(0xFFEA2A33).copy(alpha = 0.1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            itemTitle.first().toString(),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEA2A33)
                            )
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        itemTitle,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Original: NPR $originalPrice",
                        style = TextStyle(
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    )
                }
            }

            // Price Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Your Bid",
                        style = TextStyle(
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    )
                    Text(
                        "NPR $yourBid",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEA2A33)
                        )
                    )
                }

                Text(
                    "Save ₨${originalPrice - yourBid}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                )
            }

            // Negotiation Progress
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Negotiation Progress",
                        style = TextStyle(
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    )
                    Text(
                        "${(negotiationProgress * 100).toInt()}%",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                LinearProgressIndicator(
                    progress = negotiationProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = Color(0xFFEA2A33),
                    trackColor = Color.LightGray
                )
            }

            // Status Badge
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status)

                Text(
                    "Tap to negotiate",
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = Color(0xFFEA2A33),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bgColor, textColor, icon) = when (status) {
        "Accepted" -> Triple(Color(0xFF4CAF50).copy(alpha = 0.1f), Color(0xFF4CAF50), Icons.Filled.Check)
        "Rejected" -> Triple(Color(0xFFEA2A33).copy(alpha = 0.1f), Color(0xFFEA2A33), Icons.Filled.Close)
        "Countered" -> Triple(Color(0xFFFFA500).copy(alpha = 0.1f), Color(0xFFFFA500), null)
        else -> Triple(Color(0xFF2196F3).copy(alpha = 0.1f), Color(0xFF2196F3), null)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    icon,
                    contentDescription = status,
                    tint = textColor,
                    modifier = Modifier.size(14.dp)
                )
            }
            Text(
                status,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BidNegotiationSheet(
    itemTitle: String,
    originalPrice: Int,
    currentBid: Int,
    minPrice: Int,
    onDismiss: () -> Unit
) {
    var newBidAmount by remember { mutableStateOf(currentBid.toString()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                "Negotiate Your Bid",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )

            // Item Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        itemTitle,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Original Price: ", fontSize = 11.sp, color = Color.Gray)
                        Text("NPR $originalPrice", fontWeight = FontWeight.Bold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Minimum: ", fontSize = 11.sp, color = Color.Gray)
                        Text("NPR $minPrice", fontWeight = FontWeight.Bold, color = Color(0xFFEA2A33))
                    }
                }
            }

            // Bid Slider
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Your New Bid: NPR ${newBidAmount.toIntOrNull() ?: currentBid}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEA2A33)
                    )
                )

                Slider(
                    value = (newBidAmount.toIntOrNull() ?: currentBid).toFloat(),
                    onValueChange = { newBidAmount = it.toInt().toString() },
                    valueRange = minPrice.toFloat()..originalPrice.toFloat(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFEA2A33),
                        activeTrackColor = Color(0xFFEA2A33)
                    )
                )

                Text(
                    "Range: NPR $minPrice - NPR $originalPrice",
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                )
            }

            // Custom Bid Input
            OutlinedTextField(
                value = newBidAmount,
                onValueChange = { newBidAmount = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter custom bid") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFEA2A33),
                    unfocusedBorderColor = Color.LightGray
                ),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                )
            )

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFEA2A33)
                    )
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEA2A33)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Counter Offer", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
