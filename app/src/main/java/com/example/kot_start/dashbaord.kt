package com.example.kot_start

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun dashboardBody() {

    val context = LocalContext.current
    val sharedPreferences =
        context.getSharedPreferences("User", Context.MODE_PRIVATE)


    var showAddDialog by remember { mutableStateOf(false) }
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productDesc by remember { mutableStateOf("") }

    fun addProduct() {
        if (productName.isNotBlank() && productPrice.isNotBlank()) {
            Toast.makeText(
                context,
                "Product Added Successfully",
                Toast.LENGTH_SHORT
            ).show()

            productName = ""
            productPrice = ""
            productDesc = ""
            showAddDialog = false
        } else {
            Toast.makeText(
                context,
                "Please fill all fields",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    /* ---------------------------------------------------- */

    data class NavItem(val title: String, val icon: Int)

    var selectedIndex by remember { mutableStateOf(0) }

    val NavList = listOf(
        NavItem("Home", R.drawable.baseline_home_24),
        NavItem("Apartment", R.drawable.baseline_apartment_24),
        NavItem("Device", R.drawable.baseline_devices_24),
        NavItem("Profile", R.drawable.baseline_person_24)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dashboard") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.LightGray
                ),
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
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
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },

        bottomBar = {
            NavigationBar {
                NavList.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index }
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


            if (showAddDialog) {
                AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    confirmButton = {
                        TextButton(onClick = { addProduct() }) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddDialog = false }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Add Product") },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = productName,
                                onValueChange = { productName = it },
                                label = { Text("Product Name") }
                            )
                            OutlinedTextField(
                                value = productPrice,
                                onValueChange = { productPrice = it },
                                label = { Text("Price") }
                            )
                            OutlinedTextField(
                                value = productDesc,
                                onValueChange = { productDesc = it },
                                label = { Text("Description") }
                            )
                        }
                    }
                )
            }


            when (selectedIndex) {
                0 -> {
                    Text("Home Screen", modifier = Modifier.padding(20.dp))
                }
                1 -> AppScreen()
                2 -> DeviceScreen()
                3 -> Text("Profile Screen", modifier = Modifier.padding(20.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun dashbaordPreview() {
    Kot_startTheme {
        dashboardBody()
    }
}
