package com.example.kot_start

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------------- ACTIVITY ---------------- */

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { DashboardScreen() }
    }
}

/* ---------------- DATA ---------------- */

data class Skill(
    val title: String,
    val teacher: String,
    val price: String,
    val rating: String,
    val imageRes: Int
)

data class Teacher(
    val name: String,
    val skill: String,
    val rating: String
)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen() {

    val skills = listOf(
        Skill("Advanced Pottery", "Sarah J.", "$30/hr", "5.0", R.drawable.skill1),
        Skill("React Native", "Mike T.", "$50/hr", "4.8", R.drawable.skill2),
        Skill("Digital Marketing", "Emma W.", "$40/hr", "4.9", R.drawable.skill3)
    )

    val teachers = listOf(
        Teacher("Maria Garcia", "Graphic Design", "4.9"),
        Teacher("John Smith", "Web Development", "5.0")
    )

    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F6F6))
        ) {

            /* -------- HEADER -------- */
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.person),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.width(12.dp))

                        Column {
                            Text(
                                "Welcome back, Asim 👋",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Ready to learn today?",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.notification),
                            contentDescription = "Notification"
                        )
                    }
                }
            }

            /* -------- QUICK ACTIONS -------- */
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionCard("🔍", "Browse", Color(0xFFE3F2FD), Modifier.weight(1f))
                    ActionCard("💰", "My Bids", Color(0xFFE8F5E9), Modifier.weight(1f))
                }
            }

            /* -------- TRENDING -------- */
            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    "Trending Skills 🔥",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(skills.size) {
                        SkillCard(skills[it])
                    }
                }
            }

            /* -------- TEACHERS -------- */
            item {
                Text(
                    "Recommended Teachers 🎓",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(teachers.size) {
                TeacherCard(teachers[it])
            }

            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}

/* ---------------- COMPONENTS ---------------- */

@Composable
fun ActionCard(icon: String, title: String, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(color)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 32.sp)
            Text(title, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun SkillCard(skill: Skill) {
    Card(
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Image(
                painter = painterResource(skill.imageRes),
                contentDescription = skill.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(12.dp)) {
                Text(skill.title, fontWeight = FontWeight.Bold)
                Text("with ${skill.teacher}", fontSize = 12.sp, color = Color.Gray)
                Text(skill.price, fontWeight = FontWeight.Bold, color = Color(0xFFE63946))
            }
        }
    }
}

@Composable
fun TeacherCard(teacher: Teacher) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.teacher),
                contentDescription = teacher.name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(teacher.name, fontWeight = FontWeight.Bold)
                Text(teacher.skill, fontSize = 13.sp, color = Color.Gray)
                Text("⭐ ${teacher.rating}", fontSize = 12.sp)
            }

            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFE63946), CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_24),
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar {
        listOf(
            R.drawable.baseline_home_24,
            R.drawable.baseline_explore_24,
            R.drawable.baseline_message_24,
            R.drawable.baseline_person_24
        ).forEach {
            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = { Icon(painterResource(it), null) }
            )
        }
    }
}

/* ---------------- PREVIEW ---------------- */

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardScreen()
}
