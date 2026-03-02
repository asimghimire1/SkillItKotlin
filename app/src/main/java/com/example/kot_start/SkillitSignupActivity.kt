package com.example.kot_start

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.RecordVoiceOver
import com.example.kot_start.model.UserModel
import com.example.kot_start.repository.UserRepoImpl
import com.example.kot_start.viewmodel.UserViewModel

class SkillitSignupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            SkillitSignupPreview()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SkillitSignupBody() {
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val context = LocalContext.current
    val activity = context as Activity

    var selectedRole by remember { mutableStateOf("Learn") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Dynamic progress calculation
    val filledFields = listOf(
        selectedRole.isNotEmpty(),
        fullName.isNotBlank(),
        email.isNotBlank(),
        password.length >= 8
    ).count { it }

    // Password strength
    val passwordStrength = when {
        password.isEmpty() -> 0
        password.length < 6 -> 1
        password.length < 8 -> 2
        password.length >= 8 && password.any { it.isDigit() } && password.any { it.isUpperCase() } -> 4
        password.length >= 8 -> 3
        else -> 1
    }
    val strengthLabel = when (passwordStrength) {
        1 -> "Weak"
        2 -> "Fair"
        3 -> "Good"
        4 -> "Strong"
        else -> ""
    }
    val strengthColor = when (passwordStrength) {
        1 -> Color(0xFFEF4444)
        2 -> Color(0xFFF59E0B)
        3 -> Color(0xFF3B82F6)
        4 -> Color(0xFF10B981)
        else -> Color.Transparent
    }

    Scaffold { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Gradient accent bar at top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFFE63946), Color(0xFFFF6B6B), Color(0xFFE63946))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA))
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Spacer(modifier = Modifier.height(32.dp))

                // Title
                Text(
                    "SIGN UP",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE63946),
                        letterSpacing = 2.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Let's get you\nstarted",
                    style = TextStyle(
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827),
                        lineHeight = 40.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Join a community where skills create\nopportunities.",
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Color(0xFF9CA3AF),
                        lineHeight = 22.sp
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // I want to...
                Text(
                    "I want to...",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Learn / Teach Cards with Material Icons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RoleCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.School,
                        label = "Learn",
                        subtitle = "Browse & enroll",
                        isSelected = selectedRole == "Learn",
                        onClick = { selectedRole = "Learn" }
                    )
                    RoleCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.RecordVoiceOver,
                        label = "Teach",
                        subtitle = "Share your skills",
                        isSelected = selectedRole == "Teach",
                        onClick = { selectedRole = "Teach" }
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Full Name
                Text(
                    "FULL NAME",
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280), letterSpacing = 1.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. Asim Ghimire", color = Color(0xFFD1D5DB)) },
                    leadingIcon = {
                        Icon(painter = painterResource(R.drawable.baseline_person_24), contentDescription = "Name", tint = Color(0xFF9CA3AF))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color(0xFFE63946),
                        unfocusedIndicatorColor = Color(0xFFE5E7EB),
                        cursorColor = Color(0xFFE63946)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email
                Text(
                    "EMAIL ADDRESS",
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280), letterSpacing = 1.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("name@example.com", color = Color(0xFFD1D5DB)) },
                    leadingIcon = {
                        Icon(painter = painterResource(R.drawable.baseline_email_24), contentDescription = "Email", tint = Color(0xFF9CA3AF))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color(0xFFE63946),
                        unfocusedIndicatorColor = Color(0xFFE5E7EB),
                        cursorColor = Color(0xFFE63946)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Password
                Text(
                    "CREATE PASSWORD",
                    style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280), letterSpacing = 1.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Min. 8 characters", color = Color(0xFFD1D5DB)) },
                    leadingIcon = {
                        Icon(painter = painterResource(R.drawable.baseline_lock_24), contentDescription = "Password", tint = Color(0xFF9CA3AF))
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                painter = if (passwordVisibility) painterResource(R.drawable.baseline_visibility_24)
                                else painterResource(R.drawable.baseline_visibility_off_24),
                                contentDescription = "Toggle password",
                                tint = Color(0xFF9CA3AF)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color(0xFFE63946),
                        unfocusedIndicatorColor = Color(0xFFE5E7EB),
                        cursorColor = Color(0xFFE63946)
                    ),
                    singleLine = true
                )

                // Password strength indicator
                if (password.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(0xFFE5E7EB))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(passwordStrength / 4f)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(strengthColor)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            strengthLabel,
                            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = strengthColor)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Create Account Button with loading state
                Button(
                    onClick = {
                        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        } else if (password.length < 8) {
                            Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                        } else {
                            isLoading = true
                            userViewModel.register(email, password) { success, msg, userid ->
                                if (success) {
                                    val model = UserModel(
                                        userId = userid,
                                        email = email,
                                        firstName = fullName,
                                        lastName = "",
                                        dob = "",
                                        role = if (selectedRole == "Learn") "Student" else "Teacher"
                                    )
                                    userViewModel.addUserToDatabase(userid, model) { dbSuccess, dbMsg ->
                                        isLoading = false
                                        if (dbSuccess) {
                                            Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(context, SkillitLoginActivity::class.java)
                                            context.startActivity(intent)
                                            activity.finish()
                                        } else {
                                            Toast.makeText(context, dbMsg, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    isLoading = false
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE63946)),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Text("Create Account", style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White))
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Login Link
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(context, SkillitLoginActivity::class.java)
                            context.startActivity(intent)
                            activity.finish()
                        },
                    textAlign = TextAlign.Center,
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color(0xFF9CA3AF), fontSize = 15.sp)) {
                            append("Already have an account? ")
                        }
                        withStyle(SpanStyle(color = Color(0xFFE63946), fontSize = 15.sp, fontWeight = FontWeight.Bold)) {
                            append("Login")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun RoleCard(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    subtitle: String = "",
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable(onClick = onClick)
            .border(
                width = if (isSelected) 2.5.dp else 1.dp,
                color = if (isSelected) Color(0xFFE63946) else Color(0xFFE5E7EB),
                shape = RoundedCornerShape(18.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFFFF0F0) else Color.White
        ),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            // Checkmark badge
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(22.dp)
                        .background(Color(0xFFE63946), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Icon background circle
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            if (isSelected) Color(0xFFE63946).copy(alpha = 0.12f) else Color(0xFFF3F4F6),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = label,
                        tint = if (isSelected) Color(0xFFE63946) else Color(0xFF6B7280),
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(label, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF374151)))
                if (subtitle.isNotBlank()) {
                    Text(subtitle, style = TextStyle(fontSize = 11.sp, color = Color(0xFF9CA3AF)))
                }
            }
        }
    }
}

@Preview
@Composable
fun SkillitSignupPreview() {
    SkillitSignupBody()
}
