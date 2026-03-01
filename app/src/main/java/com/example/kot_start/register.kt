package com.example.kot_start

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Size
import android.widget.Toast
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.unit.sp
import com.example.kot_start.model.UserModel
import com.example.kot_start.repository.UserRepoImpl
//import com.example.kot_start.ui.theme.Blue
import com.example.kot_start.ui.theme.PurpleGrey80
import com.example.kot_start.ui.theme.LightBlue
import com.example.kot_start.ui.theme.follow
import com.example.kot_start.viewmodel.UserViewModel
import java.util.Calendar

class register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            registerPreview()
        }
    }
}



@SuppressLint("RememberReturnType")
@Composable
fun RegisterBody() {
    val userViewModel= remember{UserViewModel(UserRepoImpl())};

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
    var terms by remember { mutableStateOf(false) }
    var context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }
    val options = listOf("abc", "ijk", "qrs", "xyz")

    var textFieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }



    var calendar = Calendar.getInstance()
    var year = calendar.get(Calendar.YEAR)
    var month = calendar.get(Calendar.MONTH)
    var day = calendar.get(Calendar.DAY_OF_MONTH)
    var selectedDate by remember { mutableStateOf("") }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, y: Int, m: Int, d: Int ->
                selectedDate = "$y/${m + 1}/$d"
            },
            year,
            month,
            day
        )
    }

    // LOCAL STORAGE
    val sharedPreference= context.getSharedPreferences("User", Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()


    Scaffold { padding ->
        Column {
            Spacer(modifier = Modifier.padding(25.dp))
            Text(
                "Register",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightBlue
                ),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.padding(vertical = 12.dp))



            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = { data ->
                        email = data
                    },
                    shape = RoundedCornerShape(15.dp),
                    placeholder = {
                        Text("abc@gmail.com")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = PurpleGrey80,
                        unfocusedContainerColor = PurpleGrey80,
                        focusedIndicatorColor = follow,
                        unfocusedIndicatorColor = Color.Transparent
                    )

                )

                Spacer(modifier = Modifier.padding(vertical = 12.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { data ->
                        password = data
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            visibility = !visibility
                        }) {
                            Icon(
                                painter = if (visibility)
                                    painterResource(R.drawable.baseline_visibility_24)
                                else
                                    painterResource(R.drawable.baseline_visibility_off_24),
                                contentDescription = null
                            )
                        }

                    },
                    visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(15.dp),
                    placeholder = {
                        Text("********")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = PurpleGrey80,
                        unfocusedContainerColor = PurpleGrey80,
                        focusedIndicatorColor = LightBlue,
                        unfocusedIndicatorColor = Color.Transparent
                    )

                )
                Spacer(modifier = Modifier.padding(vertical = 12.dp))
                OutlinedTextField(
                    value=selectedDate,
                    onValueChange ={data -> selectedDate= data},
                    shape = RoundedCornerShape(15.dp),
                    placeholder = {
                        Text("yyyy/mm/dd")
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() }
                        .padding(horizontal = 15.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = PurpleGrey80,
                        unfocusedContainerColor = PurpleGrey80,
                        focusedIndicatorColor = follow,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    enabled = false,
                    readOnly = true

                )

                    Spacer(Modifier.height(20.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)) {
                    OutlinedTextField(value=selectedOptionText, onValueChange = { selectedOptionText = it },
                        modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }
                        .onGloballyPositioned { coordinates -> textFieldSize = coordinates.size.toSize() }
                        ,
                        trailingIcon = {
                            Icon(
                                painter = if (expanded) painterResource(R.drawable.baseline_arrow_drop_up_24) else painterResource(R.drawable.baseline_arrow_drop_down_24),
                                contentDescription = "Dropdown arrow",
                                Modifier.clickable { expanded = !expanded }
                            )
                        }, placeholder = {
                            Text("Choose")
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = PurpleGrey80,
                            unfocusedContainerColor = PurpleGrey80,
                            focusedIndicatorColor = follow,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        enabled = false
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {expanded = false},
                        modifier = Modifier.width(with(LocalDensity.current){textFieldSize.width.toDp()})
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    selectedOptionText = option
                                    expanded = false
                                })
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        if(!terms){
                            Toast.makeText(context, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show()
                        }else{
                            userViewModel.register(email,password,){
                                success,msg,userid ->
                                if(success){
                                    val model = UserModel(
                                        userId = userid,
                                        email = email,
                                        firstName = "",
                                        lastName = "",
                                        dob = selectedDate)
                                    userViewModel.addUserToDatabase (userid, model){
                                            success,msg ->
                                        if(success){
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        }else{
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }

//                            editor.putString("email",email)
//                            editor.putString("password",password)
//                            editor.putString("dob",selectedDate)
//
//                            editor.apply()
//                            Toast.makeText(context, "LOGINN SUCESS", Toast.LENGTH_SHORT).show()

                        }
                    },
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth().height(60.dp)
                        .padding(horizontal = 15.dp),
                    shape = RoundedCornerShape(10.dp),
                    ) {
                    Text("Sign In")
                }

                Row (modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically){
                    Checkbox(checked = terms
                        , onCheckedChange = {
                            terms = it
                        }, )
                    Text(
                        "I agree to terms and condition",
                        modifier = Modifier.clickable {
                            terms = !terms
                        }
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 12.dp))

                Text(
                    modifier = Modifier.fillMaxWidth().clickable{
//                        val intent = Intent(context,
//                            login::class.java)
//                        context.startActivity(intent)
                    },
                    textAlign = TextAlign.Center,
                    text = buildAnnotatedString {
                        append("Already have an account? ")

                        withStyle(
                            style = SpanStyle(
                                color = Blue,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Sign in")
                        }
                    }
                )

            }


        }
    }
}





@Preview
@Composable
fun registerPreview(){
    RegisterBody()
}