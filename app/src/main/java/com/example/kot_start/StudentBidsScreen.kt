package com.example.kot_start

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kot_start.model.Bid
import com.example.kot_start.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentBidsScreen(viewModel: StudentViewModel) {
    val bids by viewModel.bids.collectAsState()
    var showCounterOfferSheet by remember { mutableStateOf(false) }
    var selectedCounterBid by remember { mutableStateOf<Bid?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    "My Bids",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFFEA2A33)
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
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Step-by-Step Guide Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "STEP-BY-STEP GUIDE",
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEA2A33),
                                letterSpacing = 0.5.sp
                            )
                        )

                        Text(
                            text = "How to Bid",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            BidGuideStep(
                                number = "1",
                                text = "Find a Course you love"
                            )
                            BidGuideStep(
                                number = "2",
                                text = "Place your fair Offer"
                            )
                            BidGuideStep(
                                number = "3",
                                text = "Wait for Mentor Approval"
                            )
                        }

                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEA2A33)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "Learn More",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Active Bids Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Active Bids",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Text(
                        "${bids.size} TOTAL",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    )
                }
            }

            // Bid Cards
            items(bids) { bid ->
                ActiveBidCard(
                    bid = bid,
                    onCounterOfferClick = {
                        selectedCounterBid = bid
                        showCounterOfferSheet = true
                    }
                )
            }

            // Empty State
            if (bids.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No active bids yet",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        )
                    }
                }
            }
        }
    }

    // Counter Offer Received Sheet
    if (showCounterOfferSheet && selectedCounterBid != null) {
        CounterOfferReceivedSheet(
            bid = selectedCounterBid!!,
            onDismiss = { showCounterOfferSheet = false }
        )
    }
}

@Composable
fun BidGuideStep(
    number: String,
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = RoundedCornerShape(50.dp),
            color = Color(0xFFEA2A33)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    number,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }

        Text(
            text,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Black
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ActiveBidCard(
    bid: Bid,
    onCounterOfferClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onCounterOfferClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Course Thumbnail
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFDDDDDD), RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = "https://via.placeholder.com/80",
                    contentDescription = bid.contentTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Bid Details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Title
                Text(
                    bid.contentTitle,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    maxLines = 1
                )

                // Instructor
                Text(
                    "Mentor: ${bid.teacherName}",
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = Color.Gray
                    ),
                    maxLines = 1
                )

                // Status Badge - Text Only
                Text(
                    bid.status.uppercase(),
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = getStatusColor(bid.status)
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )

                // Spacer to push bottom content down
                Spacer(modifier = Modifier.weight(1f))

                // Bid Amount
                Text(
                    "YOUR BID",
                    style = TextStyle(
                        fontSize = 9.sp,
                        color = Color.Gray
                    )
                )
                Text(
                    "NPR ${String.format("%.0f", bid.bidAmount)}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEA2A33)
                    )
                )

                // Original Price
                Text(
                    "ORIGINAL $${String.format("%.0f", bid.originalPrice)}",
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

private fun getStatusColor(status: String): Color {
    return when (status.uppercase()) {
        "ACCEPTED" -> Color(0xFF4CAF50)
        "COUNTERED", "COUNTER OFFER" -> Color(0xFFFFA500)
        "REJECTED" -> Color(0xFFEA2A33)
        "PENDING" -> Color(0xFF2196F3)
        else -> Color.Gray
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterOfferReceivedSheet(
    bid: Bid,
    onDismiss: () -> Unit
) {
    var newOfferAmount by remember { mutableStateOf(bid.bidAmount.toFloat()) }
    val counterOfferAmount = (bid.originalPrice * 0.85).toFloat() // Example: teacher counter at 85%

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                "Counter Offer Received",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )

            // Course Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Course Icon
                    Surface(
                        modifier = Modifier.size(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF009688)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Course",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            "SKILL UPGRADE",
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF009688)
                            )
                        )
                        Text(
                            bid.contentTitle,
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                            maxLines = 1
                        )
                        Text(
                            "by ${bid.teacherName}",
                            style = TextStyle(
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        )
                    }
                }
            }

            // Bid Amounts Section
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Your Bid
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Your Bid",
                        style = TextStyle(
                            fontSize = 10.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        "$${String.format("%.0f", bid.bidAmount)}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }

                // Teacher's Counter
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFFFCDD2), RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Teacher's Counter",
                        style = TextStyle(
                            fontSize = 10.sp,
                            color = Color(0xFFC62828),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        "$${String.format("%.0f", counterOfferAmount)}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828)
                        )
                    )
                }
            }

            // Adjust Section
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Adjust Your New Offer",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

                Text(
                    "Change to close counter price",
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                )

                // Slider
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Slider(
                        value = newOfferAmount,
                        onValueChange = { newOfferAmount = it },
                        valueRange = bid.bidAmount.toFloat()..bid.originalPrice.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFEA2A33),
                            activeTrackColor = Color(0xFFEA2A33),
                            inactiveTrackColor = Color.LightGray
                        )
                    )

                    // Price Range Labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "$${String.format("%.0f", bid.bidAmount)} MIN",
                            style = TextStyle(
                                fontSize = 9.sp,
                                color = Color.Gray
                            )
                        )

                        Text(
                            "$${String.format("%.0f", newOfferAmount)}",
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEA2A33)
                            )
                        )

                        Text(
                            "$${String.format("%.0f", bid.originalPrice)} MAX",
                            style = TextStyle(
                                fontSize = 9.sp,
                                color = Color.Gray
                            )
                        )
                    }
                }
            }

            // Accept Button
            Button(
                onClick = { onDismiss() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEA2A33)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Accept Counter Offer",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Accept",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Send New Counter
            Text(
                "Send New Counter",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFEA2A33)
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onDismiss() }
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
