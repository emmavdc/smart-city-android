package com.smartcity.petsservices.ui.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smartcity.petsservices.model.Error
import com.smartcity.petsservices.model.Login
import com.smartcity.petsservices.model.Token
import com.smartcity.petsservices.model.User
import com.smartcity.petsservices.repositories.web.configuration.RetrofitConfigurationService
import com.smartcity.petsservices.repositories.web.dto.TokenDto
import com.smartcity.petsservices.repositories.web.dto.UserDto
import com.smartcity.petsservices.services.mappers.LoginMapper
import com.smartcity.petsservices.services.mappers.TokenMapper
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
          webService.getUser(token.userId, "Bearer " + token.token).enqueue(object : Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                if (response.isSuccessful) {
                    System.out.println("chouette " + response.code() + "  "+ response.body())
                    _user.value = userMapper.mapToUser(response.body()!!)
                    _error.value = Error.NO_ERROR

                } else {
                    System.out.println("pas chouette " + response.code())
                    _error.value = Error.REQUEST_ERROR
                }
            }

            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                if (t is NoConnectivityException) {
                    System.out.println("error connectivity")
                    //result.message = R.string.connectivity_error.toString()
                    _error.value = Error.NO_CONNECTION
                } else {
                    System.out.println("******************* " + t)
                    //result = R.string.user_added.toString()
                    _error.value = Error.TECHNICAL_ERROR
                }
            }

        })
    }
}