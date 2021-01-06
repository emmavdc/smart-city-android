package com.smartcity.petsservices.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LoginViewModel(application: Application) : AndroidViewModel(application){

    private val _email = MutableLiveData<String>()
    val email :LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password :LiveData<String> = _password




}