package com.smartcity.petsservices.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smartcity.petsservices.model.Error
import com.smartcity.petsservices.model.Login
import com.smartcity.petsservices.model.Token
import com.smartcity.petsservices.repositories.web.configuration.RetrofitConfigurationService
import com.smartcity.petsservices.repositories.web.dto.TokenDto
import com.smartcity.petsservices.services.mappers.LoginMapper
import com.smartcity.petsservices.services.mappers.TokenMapper
import com.smartcity.petsservices.services.mappers.UserMapper
import com.smartcity.petsservices.utils.NoConnectivityException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application){

    private val _error: MutableLiveData<Error> = MutableLiveData()
    val error: LiveData<Error> = _error

    private val _jwt : MutableLiveData<Token> = MutableLiveData()
    val jwt : LiveData<Token> = _jwt

    private var webService = RetrofitConfigurationService.getInstance(application).webService()
    private var loginMapper  = LoginMapper
    private  var tokenMapper = TokenMapper

    fun loginUser(login : Login){
        webService.loginUser(loginMapper.mapToLoginDto(login)).enqueue(object : Callback<TokenDto>{
            override fun onResponse(call: Call<TokenDto>, response: Response<TokenDto>) {
                if (response.isSuccessful) {
                    System.out.println("chouette " + response.code() + "  "+ response.body())
                    System.out.println(response.body()!!.token)

                    _error.value = Error.NO_ERROR
                    _jwt.value = tokenMapper.mapToToken(response.body()!!)

                } else {
                    System.out.println("pas chouette " + response.code())
                    _error.value = Error.BAD_CREDENTIALS
                }
            }

            override fun onFailure(call: Call<TokenDto>, t: Throwable) {
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