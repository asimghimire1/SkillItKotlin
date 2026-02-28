package com.example.kot_start.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============ HEADER ============

@Composable
fun SkillItHeader(
    walletBalance: String = "NPR 0",
    onNotificationClick: () -> Unit = {},
    onAddCreditsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Wallet Balance",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
                Text(
                    walletBalance,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        Icons.Filled.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White
                    )
                }

                Box {
                    IconButton(
                        onClick = { showMenu = !showMenu },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }

                    // Dropdown Menu
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Add Credits",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                )
                            },
                            onClick = {
                                showMenu = false
                                onAddCreditsClick()
                            }
                        )

                        Divider()

                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Logout",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color(0xFFEA2A33),
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            onClick = {
                                showMenu = false
                                onLogoutClick()
                            }
                        )
                    }
                }
            }
        }
    }
}

// ============ NAV GRID ITEM ============

@Composable
fun NavGridItem(
    title: String,
    subtitle: String,
    iconPainter: Int,
    backgroundColor: Color,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                painter = painterResource(iconPainter),
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            Column {
                Text(
                    title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    subtitle,
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
            }
        }
    }
}

// ============ QUICK ACTION BUTTON ============

@Composable
fun QuickActionButton(
    title: String,
    icon: Int,
    iconBackgroundColor: Color = Color(0xFF4CAF50),
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                title,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBackgroundColor, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ============ SESSION CARD ============

@Composable
fun SessionCard(
    month: String,
    day: String,
    title: String,
    time: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date Section
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .background(Color(0xFFEA2A33), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        month,
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        day,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }

            // Session Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
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
                    time,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                )
            }

            Icon(
                painter = painterResource(android.R.drawable.ic_media_play),
                contentDescription = "Play",
                tint = Color(0xFFEA2A33),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ============ SEARCH BAR ============

@Composable
fun SearchBar(
    hint: String = "Search...",
    onSearchChange: (String) -> Unit = {},
    onFilterClick: () -> Unit = {}
) {
    var searchQuery = ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it; onSearchChange(it) },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            placeholder = { Text(hint, fontSize = 12.sp) },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search", modifier = Modifier.size(20.dp))
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFFEA2A33)
            ),
            singleLine = true
        )

        IconButton(
            onClick = onFilterClick,
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFEA2A33), RoundedCornerShape(12.dp))
        ) {
            Icon(
                painter = painterResource(android.R.drawable.ic_menu_manage),
                contentDescription = "Filter",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ============ SECTION HEADER ============

@Composable
fun SectionHeader(
    title: String,
    actionText: String? = null,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        if (actionText != null) {
            TextButton(onClick = onActionClick) {
                Text(
                    actionText,
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

// ============ STATS CARD ============

@Composable
fun StatsCard(
    title: String,
    value: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Text(
                title,
                style = TextStyle(
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            )
        }
    }
}

// ============ STUDENT COURSE CARD ============

@Composable
fun StudentCourseCard(
    courseId: String,
    courseName: String,
    instructor: String,
    progress: Float,
    assignments: Int,
    daysLeft: Int,
    onCardClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Course Header
            Column {
                Text(
                    courseName,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Text(
                    "by $instructor",
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                )
            }

            // Progress Bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Progress",
                        style = TextStyle(fontSize = 10.sp, color = Color.Gray)
                    )
                    Text(
                        "${(progress * 100).toInt()}%",
                        style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    )
                }
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = Color(0xFFEA2A33),
                    trackColor = Color.LightGray
                )
            }

            // Course Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatBadge("$assignments Assignments", Color(0xFFE3F2FD))
                StatBadge("$daysLeft Days Left", Color(0xFFFFE0B2))
            }
        }
    }
}

// ============ ASSIGNMENT CARD ============

@Composable
fun AssignmentCard(
    title: String,
    subject: String,
    dueDate: String,
    status: String,
    onCardClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onCardClick),
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
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        if (status == "pending") Color(0xFFFFB74D) else Color(0xFF4CAF50),
                        RoundedCornerShape(6.dp)
                    )
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Text(
                    subject,
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                )
                Text(
                    dueDate,
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                )
            }

            Icon(
                painter = painterResource(android.R.drawable.ic_menu_view),
                contentDescription = "View",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ============ EMPTY STATE ============

@Composable
fun EmptyState(
    title: String,
    description: String,
    actionText: String? = null,
    onActionClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Empty Icon Placeholder
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.LightGray, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("∅", fontSize = 40.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            title,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            description,
            style = TextStyle(
                fontSize = 13.sp,
                color = Color.Gray,
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        if (actionText != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onActionClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA2A33))
            ) {
                Text(
                    actionText,
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

// ============ QUICK ACTION CARD ============

@Composable
fun QuickActionCard(
    title: String,
    description: String,
    backgroundColor: Color,
    onCardClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                title,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (backgroundColor == Color.Gray) Color.White else Color.Black
                )
            )
            Text(
                description,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = if (backgroundColor == Color.Gray) Color.White.copy(alpha = 0.8f) else Color.Gray
                )
            )
        }
    }
}

// ============ STAT BADGE ============

@Composable
fun StatBadge(
    text: String,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )
    }
}
