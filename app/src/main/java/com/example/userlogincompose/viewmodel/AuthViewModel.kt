package com.example.userlogincompose.viewmodel

import androidx.lifecycle.ViewModel
import com.example.userlogincompose.repo.FirebaseAuthRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Exception

class AuthViewModel : ViewModel() {

    private val _userLoginStatus = MutableStateFlow<UserLoginStatus?>(null)
    val userLoginStatus = _userLoginStatus.asStateFlow()

    private val firebaseAuth = FirebaseAuth.getInstance()

    init {
        createAccount("a@gmail.com","123456")
    }


    fun performLogin(userName:String, password:String){
        FirebaseAuthRepo.login(firebaseAuth, userName, password,
            onSuccess = {
                        _userLoginStatus.value=UserLoginStatus.Successful

        }, onFailure = {
                    _userLoginStatus.value=UserLoginStatus.Failure(it)
        })
    }

    fun createAccount(userName:String, password:String){
        FirebaseAuthRepo.signUp(firebaseAuth, userName, password, onSuccess = {}, onFailure = {})
    }

    sealed class UserLoginStatus{
        object Successful:UserLoginStatus()
        class Failure(val exception: Exception?):UserLoginStatus()
    }
}