package com.example.kot_start

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashScreenPreview()
        }

        // Navigate to Welcome after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }, 3000)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SplashScreenBody() {
    // Animation values
    val logoScale = remember { Animatable(0f) }
    val logoRotation = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val sparkle1 = remember { Animatable(0f) }
    val sparkle2 = remember { Animatable(0f) }
    val sparkle3 = remember { Animatable(0f) }

    // Launch animations
    LaunchedEffect(Unit) {
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = LinearEasing)
        )
        logoRotation.animateTo(
            targetValue = 360f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600)
        )
        sparkle1.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(200)
        sparkle2.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1800),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(400)
        sparkle3.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFF6B6B),
                            Color(0xFFE63946),
                            Color(0xFFD62828)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Sparkle decorations
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .offset(x = 120.dp, y = (-200).dp)
                    .alpha(sparkle1.value * 0.3f)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                    .rotate(45f)
            )

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .offset(x = (-100).dp, y = 180.dp)
                    .alpha(sparkle2.value * 0.25f)
                    .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(25.dp))
                    .rotate(30f)
            )

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .offset(x = (-140).dp, y = (-50).dp)
                    .alpha(sparkle3.value * 0.35f)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(15.dp))
                    .rotate(60f)
            )

            // Main content
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Logo
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .scale(logoScale.value)
                        .rotate(logoRotation.value * 0.1f)
                        .background(Color.White, RoundedCornerShape(35.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE63946), RoundedCornerShape(28.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        // PUT YOUR LOGO HERE: Replace R.drawable.communication
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "Skillit Logo",
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Skillit",
                    modifier = Modifier.alpha(textAlpha.value),
                    style = TextStyle(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Swap Skills. Share Knowledge.",
                    modifier = Modifier.alpha(textAlpha.value),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                )

                Spacer(modifier = Modifier.height(100.dp))

                Box(modifier = Modifier.alpha(textAlpha.value)) {
                    LoadingDots()
                }
            }
        }
    }
}

@Composable
fun LoadingDots() {
    val dot1 = remember { Animatable(0f) }
    val dot2 = remember { Animatable(0f) }
    val dot3 = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            dot1.animateTo(1f, tween(400))
            dot1.animateTo(0f, tween(400))
        }
    }

    LaunchedEffect(Unit) {
        delay(150)
        while (true) {
            dot2.animateTo(1f, tween(400))
            dot2.animateTo(0f, tween(400))
        }
    }

    LaunchedEffect(Unit) {
        delay(300)
        while (true) {
            dot3.animateTo(1f, tween(400))
            dot3.animateTo(0f, tween(400))
        }
    }

    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .alpha(0.4f + (dot1.value * 0.6f))
                .background(Color.White, RoundedCornerShape(6.dp))
        )
        Box(
            modifier = Modifier
                .size(12.dp)
                .alpha(0.4f + (dot2.value * 0.6f))
                .background(Color.White, RoundedCornerShape(6.dp))
        )
        Box(
            modifier = Modifier
                .size(12.dp)
                .alpha(0.4f + (dot3.value * 0.6f))
                .background(Color.White, RoundedCornerShape(6.dp))
        )
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreenBody()
}