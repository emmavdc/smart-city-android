package com.smartcity.petsservices.ui.viewModel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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

    val email = MutableLiveData<String>()
    private val validatorFormMediator = MediatorLiveData<Boolean>()

    init {
        validatorFormMediator.addSource(email) { validateForm() }
    }

    //TODO add connection error


    private var webService = RetrofitConfigurationService.getInstance(application).webService()
    private var userMapper  = UserMapper

    fun addUser(user: User){
        webService.postUser(userMapper.mapToUserDto(user)!!).enqueue(object : Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                if (response.isSuccessful) {
                    System.out.println("chouette")
                } else {
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

    private fun validateForm(){
        validatorFormMediator.value = validateEmail()
    }

    private fun validateEmail() : Boolean {
        return email.value!!.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
    }

    override fun onCleared() {
        // DO NOT forget to remove sources from mediator
        validatorFormMediator.removeSource(email)
    }

    fun getValidatorFormMediator(): LiveData<Boolean> {
        return validatorFormMediator
    }


}