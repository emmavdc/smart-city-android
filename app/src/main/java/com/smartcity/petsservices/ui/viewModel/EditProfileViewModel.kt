package com.smartcity.petsservices.ui.viewModel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.smartcity.petsservices.model.Error
import com.smartcity.petsservices.model.Token
import com.smartcity.petsservices.model.User
import com.smartcity.petsservices.repositories.web.configuration.RetrofitConfigurationService
import com.smartcity.petsservices.repositories.web.dto.TokenDto
import com.smartcity.petsservices.services.mappers.UserMapper
import com.smartcity.petsservices.utils.NoConnectivityException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel (application: Application) : AndroidViewModel(application){


    private val _error: MutableLiveData<Error> = MutableLiveData()
    val error: LiveData<Error> = _error
    private var webService = RetrofitConfigurationService.getInstance(application).webService()
    private var userMapper  = UserMapper

    fun updateUser(user : User, token: Token){
        webService.putUser(userMapper.mapToUserDto(user)!!, token.userId, "Bearer " + token.token).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    System.out.println("chouette " + response.code() + "  "+ response.body())
                    System.out.println(response.body()!!)

                    _error.value = Error.NO_ERROR
                } else {
                    System.out.println("pas chouette " + response.code())
                    if (response.code() == 409)
                        _error.value = Error.USER_ALREADY_EXIST
                    else
                        _error.value = Error.REQUEST_ERROR
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
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