package com.example.kot_start.repository

import com.example.kot_start.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

// implement userRepo


class UserRepoImpl: UserRepo {

    val auth : FirebaseAuth= FirebaseAuth.getInstance()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    val ref: DatabaseReference=database.getReference("users")

    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Login Successful")
            }
            else{
                callback(false,it.exception?.message.toString())
            }
        }

    }

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) { auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Register Successful","${auth.currentUser?.uid}")
             }
            else{
                callback(false,it.exception?.message.toString(),"")
            }
     }
    }

    override fun addUserToDatabase(
        userId: String,
        modal: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).setValue(modal).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"User Added")
            }
            else{
                callback(false,it.exception?.message.toString())
            }
        }
    }
    override fun updateProfile(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).updateChildren(model.toMap()).addOnCompleteListener {
                   }
    }

    override fun deleteProfile(
        userId: String,
        UserModal: UserModel,

        callback: (Boolean, String) -> Unit
    ) {
       ref.child(userId).removeValue().addOnCompleteListener {
           if(it.isSuccessful){
               callback(true,"Profile Deleted")
           }
       }
    }

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Email send")
            }
            else{
                callback(false,it.exception?.message.toString())
            }
        }
    }

    override fun logout(callback: (Boolean, String) -> Unit) {
        try {
            auth.signOut()
            callback(true,"logout sucuesfull")
        }catch (e: Exception){
            callback(false,"${e.message}")
        }
    }

    override fun getUserRole(
        userId: String,
        callback: (Boolean, String, String?) -> Unit
    ) {
        ref.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UserModel::class.java)
                    if (user != null) {
                        callback(true, "Role found", user.role)
                    } else {
                        callback(false, "User not found", null)
                    }
                } else {
                    callback(false, "User does not exist", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }


    override fun getUserById(
        userId: String,
        callback: (Boolean, String, UserModel?) -> Unit
    ) {
        ref.child(userId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val users = snapshot.getValue(UserModel::class.java)
                    if (users != null) {
                        callback(true, "User Found", users)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false,error.message,null)
            }
        })
    }


    override fun getCurrentUser(): FirebaseUser? {
       return auth.currentUser
    }

    override fun getAllUser(callback: (Boolean, String, List<UserModel>?) -> Unit) {
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                val allUsers= mutableListOf<UserModel>()
                    for(data in snapshot.children){
                        val users=data.getValue(UserModel::class.java)
                    if(users !=null){
                        allUsers.add(users)
                    }
            }

            }}

            override fun onCancelled(error: DatabaseError) {
                callback(false,error.message,null)
            }
        })
    }









}