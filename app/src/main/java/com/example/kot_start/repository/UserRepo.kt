package com.example.kot_start.repository

import com.example.kot_start.model.UserModel
import com.google.firebase.auth.FirebaseUser
import javax.security.auth.callback.PasswordCallback

//repo should be interface
interface UserRepo {
    fun login(email: String,
              password: String,
              callback: (Boolean,String)-> Unit)

    fun register(
        email: String, password: String,
        callback: (Boolean, String, String)-> Unit
    )
    fun addUserToDatabase(
        userId: String,
        modal: UserModel,
        callback: (Boolean, String)-> Unit

        )

    fun getUserById(userId: String, callback: (Boolean, String,UserModel?) -> Unit)
    fun getCurrentUser(): FirebaseUser?
    fun getAllUser(callback: (Boolean, String, List<UserModel>?) -> Unit)



    fun updateProfile(userId: String,modal: UserModel,callback: (Boolean, String) -> Unit    )

    fun deleteProfile(userId: String,UserModel: UserModel,callback: (Boolean, String) -> Unit)

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit)
    fun logout(callback: (Boolean, String) -> Unit)
    fun getUserRole(userId: String, callback: (Boolean, String, String?) -> Unit)

}


