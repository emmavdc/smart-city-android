package com.smartcity.petsservices.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smartcity.petsservices.model.User
import com.smartcity.petsservices.repositories.web.configuration.RetrofitConfigurationService
import com.smartcity.petsservices.services.mappers.UserMapper
import retrofit2.Call
import retrofit2.Callback

class RegistrationViewModel(application: Application) : AndroidViewModel(application){

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user


    //TODO add connection error


    private var webService = RetrofitConfigurationService.getInstance(application).webService()
    private var userMapper  = UserMapper

    fun addUser(){
        /*webService.postUser().enqueue(object Callback<UserDto>{

        })*/
    }


}