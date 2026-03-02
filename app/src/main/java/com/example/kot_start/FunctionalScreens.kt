package com.example.kot_start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==================== SIMPLIFIED FUNCTIONAL SCREENS ====================
// These are minimal implementations for teacher demo purposes

@Composable
fun ScheduleSessionScreenFunctional(
    viewModel: TeacherViewModel,
    onBackClick: () -> Unit,
    onSessionCreated: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Schedule Session", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Create a new live session for your students", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Session Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
fun UploadContentScreenFunctional(
    viewModel: TeacherViewModel,
    videoLauncher: Any,
    thumbnailLauncher: Any,
    onBackClick: () -> Unit,
    onVideoUploaded: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Upload Content", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Upload videos for your courses", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Select Video")
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
fun TeacherContentScreenFunctional(
    viewModel: TeacherViewModel,
    onBackClick: () -> Unit,
    onVideoClick: (Video) -> Unit,
    onSessionClick: (Session) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("My Content", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("View all your courses and sessions", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))
            
            repeat(2) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Sample Course $it", fontWeight = FontWeight.SemiBold)
                            Text("100 students", fontSize = 12.sp, color = Color.Gray)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
fun TeacherBidsScreenFunctional(
    viewModel: TeacherViewModel,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Student Bids", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("View and manage student bid requests", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))
            
            repeat(3) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Student $it Offer", fontWeight = FontWeight.SemiBold)
                        Text("Course: Advanced Design", fontSize = 12.sp, color = Color.Gray)
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
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
fun TeacherEarningsScreenFunctional(
    viewModel: TeacherViewModel,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Earnings", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEA2A33))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text("Total Balance", fontSize = 14.sp, color = Color.White)
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
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Recent Transactions", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))
            
            repeat(3) { i ->
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
                            Text("2 days ago", fontSize = 12.sp, color = Color.Gray)
                        }
                        Text("+\$${"%.2f".format(100 * (i + 1))}", fontWeight = FontWeight.SemiBold, color = Color(0xFF10B981))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
fun SessionDetailsScreen(
    session: Session,
    onBackClick: () -> Unit,
    onJoinClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(session.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Date: ${session.date}", fontSize = 14.sp)
                    Text("Time: ${session.time}", fontSize = 14.sp)
                    Text("Students: ${session.studentsRegistered}", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    if (session.isPaid) {
                        Text("Price: \$${String.format("%.2f", session.price)}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFEA2A33))
                    } else {
                        Text("Free Session", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF10B981))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(onClick = onJoinClick, modifier = Modifier.fillMaxWidth()) {
                Text("Join Session")
            }
        }
    }
}
