package com.example.kot_start.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kot_start.model.UserModel
import com.example.kot_start.repository.UserRepo
import com.google.firebase.auth.FirebaseUser

//view ra repo ko bich ma communitace || this is kotlin dataclass


class UserViewModel(val repo: UserRepo): ViewModel() {
    fun login(email: String,
              password: String,
              callback: (Boolean,String)-> Unit){
        repo.login(email,password,callback);
    }

    fun register(
        email: String, password: String,
        callback: (Boolean, String, String)-> Unit
    ){
        repo.register(email,password,callback)

    }
    fun addUserToDatabase(
        userId: String,
        modal: UserModel,
        callback: (Boolean, String)-> Unit

    ){
        repo.addUserToDatabase(userId,modal,callback)
    }




    private val _users= MutableLiveData<UserModel?>()
    val users: MutableLiveData<UserModel?>
        get() = _users

    private val _allusers= MutableLiveData<List<UserModel>?>()

    val allusers: MutableLiveData<List<UserModel>?>
        get() = _allusers


    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean>
        get() = _loading

//    The postValue function puts the data fetched by getUserById into the private _users LiveData.
//    Then, the public users getter allows the UI to access that data.
    fun getUserById(userId: String){
        repo.getUserById(userId){
            success,msg,data->
            if (success){
                _loading.postValue(false)
                _users.postValue(data)
            }
            else{
                _loading.postValue(false)
                _users.postValue(data)
            }
        };
    }



    fun getAllUser(){
        repo.getAllUser(){
            success,msg,data->
            if (success){
                _loading.postValue(false)
                _allusers.postValue(data)
            }
            else{
                _loading.postValue(false)
                _allusers.postValue(data)
            }

        }
    }
    fun getCurrentUser(): FirebaseUser?{
        return  repo.getCurrentUser()
    }
    fun updateProfile(userId: String,modal: UserModel,callback: (Boolean, String) -> Unit    ){
        repo.updateProfile(userId,modal,callback)
    }
    fun deleteProfile(userId: String,UserModal: UserModel,callback: (Boolean, String) -> Unit){
        repo.deleteProfile(userId,UserModal,callback)
    }
    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit){
        repo.forgetPassword(email,callback)
    }
    fun logout(callback: (Boolean, String) -> Unit){
        repo.logout(callback)

    }

    fun getUserRole(userId: String, callback: (Boolean, String, String?) -> Unit) {
        repo.getUserRole(userId, callback)
    }
}