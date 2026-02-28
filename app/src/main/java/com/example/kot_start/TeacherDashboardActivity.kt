package com.example.kot_start

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class TeacherDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { TeacherDashboardScreen() }
    }
}

/* -------- DATA CLASSES -------- */

data class CourseData(
    val courseId: String,
    val title: String,
    val description: String,
    val students: Int,
    val rating: String,
    val imageRes: Int
)

data class StudentEnrollment(
    val studentId: String,
    val name: String,
    val email: String,
    val enrollmentDate: String
)

/* -------- MAIN SCREEN -------- */

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TeacherDashboardScreen() {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("home") }

    val courses = listOf(
        CourseData("1", "Advanced Pottery", "Learn advanced pottery techniques", 24, "4.9", R.drawable.skill1),
        CourseData("2", "Digital Marketing", "Master digital marketing strategies", 18, "4.8", R.drawable.skill3),
        CourseData("3", "Web Development", "Full-stack web development", 32, "5.0", R.drawable.skill2)
    )

    val students = listOf(
        StudentEnrollment("s1", "John Doe", "john@example.com", "2025-01-15"),
        StudentEnrollment("s2", "Sarah Smith", "sarah@example.com", "2025-02-10"),
        StudentEnrollment("s3", "Mike Johnson", "mike@example.com", "2025-02-12")
    )

    Scaffold(
        bottomBar = { TeacherBottomNavigationBar(selectedTab) { selectedTab = it } }
    ) {
        when (selectedTab) {
            "home" -> TeacherHomeContent(courses, students)
            "add" -> AddCourseContent()
            "profile" -> TeacherProfileContent()
            "settings" -> TeacherSettingsContent(context as ComponentActivity)
        }
    }
}

/* -------- HOME TAB CONTENT -------- */

@Composable
fun TeacherHomeContent(courses: List<CourseData>, students: List<StudentEnrollment>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Welcome back!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        "Your Courses",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }

                Image(
                    painter = painterResource(R.drawable.person),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Stats Cards
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Total Courses",
                    value = "${courses.size}",
                    backgroundColor = Color(0xFFE63946),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Total Students",
                    value = "${students.size}",
                    backgroundColor = Color(0xFF457B9D),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        // Courses Section
        item {
            Text(
                "Your Courses",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(courses.size) { index ->
                    CourseCard(courses[index])
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // Recent Students Section
        item {
            Text(
                "Recent Enrollments",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(students.size) { index ->
            StudentEnrollmentCard(students[index])
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

/* -------- ADD COURSE TAB CONTENT -------- */

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddCourseContent() {
    var courseTitle by remember { mutableStateOf("") }
    var courseDescription by remember { mutableStateOf("") }
    var coursePrice by remember { mutableStateOf("") }
    var courseDuration by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(24.dp)
    ) {
        item {
            Text(
                "Create New Course",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        item {
            OutlinedTextField(
                value = courseTitle,
                onValueChange = { courseTitle = it },
                label = { Text("Course Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = courseDescription,
                onValueChange = { courseDescription = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 5
            )
        }

        item {
            OutlinedTextField(
                value = coursePrice,
                onValueChange = { coursePrice = it },
                label = { Text("Price per Hour") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            OutlinedTextField(
                value = courseDuration,
                onValueChange = { courseDuration = it },
                label = { Text("Duration (weeks)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            Button(
                onClick = { /* TODO: Save course */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE63946)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Create Course",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

/* -------- PROFILE TAB CONTENT -------- */

@Composable
fun TeacherProfileContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(modifier = Modifier.height(24.dp)) }

        item {
            Image(
                painter = painterResource(R.drawable.person),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            Text(
                "John Teacher",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        item {
            Text(
                "john.teacher@example.com",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        item {
            ProfileInfoCard(label = "Courses Created", value = "3")
        }

        item {
            ProfileInfoCard(label = "Total Students", value = "74")
        }

        item {
            ProfileInfoCard(label = "Average Rating", value = "4.9")
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        item {
            Button(
                onClick = { /* TODO: Edit profile */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE63946)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Edit Profile", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/* -------- SETTINGS TAB CONTENT -------- */

@Composable
fun TeacherSettingsContent(activity: ComponentActivity) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F6))
    ) {
        item {
            Text(
                "Settings",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            SettingItem(
                title = "Notifications",
                subtitle = "Manage notification settings",
                onClick = { /* TODO */ }
            )
        }

        item {
            SettingItem(
                title = "Payment Methods",
                subtitle = "Add or update payment methods",
                onClick = { /* TODO */ }
            )
        }

        item {
            SettingItem(
                title = "Privacy & Security",
                subtitle = "Manage your privacy settings",
                onClick = { /* TODO */ }
            )
        }

        item {
            Divider(modifier = Modifier.padding(16.dp))
        }

        item {
            SettingItem(
                title = "Logout",
                subtitle = "Sign out from your account",
                onClick = { /* TODO: Logout and redirect to login */ },
                isDestructive = true
            )
        }
    }
}

/* -------- COMPOSABLE COMPONENTS -------- */

@Composable
fun StatCard(title: String, value: String, backgroundColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun CourseCard(course: CourseData) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable { /* TODO */ },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(course.imageRes),
                contentDescription = course.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                course.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${course.students} students",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Text(
                    "⭐ ${course.rating}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun StudentEnrollmentCard(student: StudentEnrollment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    student.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    student.email,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Text(
                    "Enrolled: ${student.enrollmentDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileInfoCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun SettingItem(title: String, subtitle: String, onClick: () -> Unit, isDestructive: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isDestructive) Color(0xFFE63946) else Color.Black
                )
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
        Text(">", color = Color.Gray)
    }
}

@Composable
fun TeacherBottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = selectedTab == "home",
            onClick = { onTabSelected("home") },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedTab == "add",
            onClick = { onTabSelected("add") },
            icon = { Icon(Icons.Filled.Add, contentDescription = "Add") },
            label = { Text("Add") }
        )
        NavigationBarItem(
            selected = selectedTab == "profile",
            onClick = { onTabSelected("profile") },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
        NavigationBarItem(
            selected = selectedTab == "settings",
            onClick = { onTabSelected("settings") },
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") }
        )
    }
}

@Preview
@Composable
fun TeacherDashboardPreview() {
    TeacherDashboardScreen()
}
