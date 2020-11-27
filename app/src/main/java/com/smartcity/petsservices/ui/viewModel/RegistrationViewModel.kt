package com.smartcity.petsservices.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smartcity.petsservices.model.User
import com.smartcity.petsservices.repositories.web.configuration.RetrofitConfigurationService
import com.smartcity.petsservices.repositories.web.dto.UserDto
import com.smartcity.petsservices.services.mappers.UserMapper
import com.smartcity.petsservices.utils.NoConnectivityException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel(application: Application) : AndroidViewModel(application){

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user


    //TODO add connection error


    private var webService = RetrofitConfigurationService.getInstance(application).webService()
    private var userMapper  = UserMapper

    fun addUser(user: User){
        webService.postUser(userMapper.mapToUserDto(user)!!).enqueue(object : Callback<UserDto>{
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                if (response.isSuccessful){
                    System.out.println("chouette")
                }
                else{
                    System.out.println("pas chouette")
                }
            }

            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                if (t is NoConnectivityException) {
                    System.out.println("error connectivity")
                } else {
                    System.out.println("error error")
                }
            }

        })
    }


}