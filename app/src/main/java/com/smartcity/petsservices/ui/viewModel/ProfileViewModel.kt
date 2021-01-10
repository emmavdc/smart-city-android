package com.smartcity.petsservices.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smartcity.petsservices.model.Error
import com.smartcity.petsservices.model.Token
import com.smartcity.petsservices.model.User
import com.smartcity.petsservices.repositories.web.configuration.RetrofitConfigurationService
import com.smartcity.petsservices.repositories.web.dto.UserDto
import com.smartcity.petsservices.services.mappers.UserMapper
import com.smartcity.petsservices.utils.NoConnectivityException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel (application: Application) : AndroidViewModel(application){

    private val _error: MutableLiveData<Error> = MutableLiveData()
    val error: LiveData<Error> = _error

    private var _user : MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    private var webService = RetrofitConfigurationService.getInstance(application).webService()
    private var userMapper  = UserMapper

    fun getUser(token: Token){
          webService.getUser(token.userId, "Bearer "+ token.token).enqueue(object : Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                if (response.isSuccessful) {
                    _user.value = userMapper.mapToUser(response.body()!!)
                    _error.value = Error.NO_ERROR

                } else {
                    _error.value = Error.REQUEST_ERROR
                }
            }
            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                if (t is NoConnectivityException) {
                    _error.value = Error.NO_CONNECTION
                } else {
                    System.out.println(t)
                    _error.value = Error.TECHNICAL_ERROR
                }
            }

        })
    }
}