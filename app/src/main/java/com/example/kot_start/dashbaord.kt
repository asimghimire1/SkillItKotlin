package com.example.kot_start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.kot_start.ui.theme.Kot_startTheme

class dashbaord : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kot_startTheme {
                dashboardBody()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dashboardBody(){
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
    val localEmail: String? = sharedPreferences.getString("email", null)
    val localPassword: String? = sharedPreferences.getString("password", null)


    data class NavItem(val title: String, val icon: Int, )
    var selectedIndex by remember { mutableStateOf(0) }
    val NavList= listOf(
        NavItem(title = "Home", icon = R.drawable.baseline_home_24),
        NavItem(title = "Apartment", icon = R.drawable.baseline_apartment_24),
        NavItem(title = "Device", icon = R.drawable.baseline_devices_24),
        NavItem(title = "Profile", icon = R.drawable.baseline_person_24),

    )


    Scaffold (topBar = {
        CenterAlignedTopAppBar(
            title={Text("Testtt")},
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.LightGray
            ),
            navigationIcon = {
                IconButton(onClick = {

                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_history_24),
                        contentDescription = "History"
                    )
                }
            }
        )
    },
        floatingActionButton = {
            FloatingActionButton(onClick ={
//                val intent= Intent(context,AddProduct::class.java)
//                context.startActivity(intent)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
    bottomBar = {
        NavigationBar {
            NavList.forEachIndexed { index,item->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(item.title)
                    },
                    selected = selectedIndex == index,
                    onClick = {
                        selectedIndex = index
                    }
                )
            }
        }
    }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedIndex) {
//                0 -> HomeScreen()
                1 -> AppScreen()
                2 -> DeviceScreen()
//                3 -> ProfileScreen()
//                else -> HomeScreen()
            }
        }
    }
}
@Composable
@Preview
fun dashbaordPreview(){
    Kot_startTheme {
        dashboardBody()
    }
}