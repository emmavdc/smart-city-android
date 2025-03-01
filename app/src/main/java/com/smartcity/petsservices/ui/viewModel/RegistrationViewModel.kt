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
import com.smartcity.petsservices.services.mappers.TokenMapper
import com.smartcity.petsservices.services.mappers.UserMapper
import com.smartcity.petsservices.utils.NoConnectivityException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegistrationViewModel(application: Application) : AndroidViewModel(application){

    val email = MutableLiveData<String>()
    private val emailMediator = MediatorLiveData<Boolean>()
    val password = MutableLiveData<String>()
    private val passwordMediator = MediatorLiveData<Boolean>()
    val passwordValidation = MutableLiveData<String>()
    private val passwordValidationMediator = MediatorLiveData<Boolean>()
    val firstname = MutableLiveData<String>()
    private val firstNameMediator = MediatorLiveData<Boolean>()
    val lastname = MutableLiveData<String>()
    private val lastnameMediator = MediatorLiveData<Boolean>()
    val phone = MutableLiveData<String>()
    private val phoneMediator = MediatorLiveData<Boolean>()
    val streetName = MutableLiveData<String>()
    private val streetNameMediator = MediatorLiveData<Boolean>()
    val streetNumber = MutableLiveData<String>()
    private val streetNumberMediator = MediatorLiveData<Boolean>()
    val locality = MutableLiveData<String>()
    private val localityMediator = MediatorLiveData<Boolean>()
    val postalCode = MutableLiveData<String>()
    private val postalCodeMediator = MediatorLiveData<Boolean>()

    init {
        emailMediator.addSource(email) { validateEmail() }
        passwordMediator.addSource(password) { validatePassword() }
        passwordValidationMediator.addSource(passwordValidation) { validateValidationPassword() }
        firstNameMediator.addSource(firstname) { validateFirstname() }
        lastnameMediator.addSource(lastname) { validateLastname() }
        phoneMediator.addSource(phone) { validatePhone() }
        streetNameMediator.addSource(streetName) { validateStreetName() }
        streetNumberMediator.addSource(streetNumber) { validateStreetNumber() }
        localityMediator.addSource(locality) { validateLocality() }
        postalCodeMediator.addSource(postalCode) { validatePostalCode() }
    }

    private val _error: MutableLiveData<Error> = MutableLiveData()
    val error: LiveData<Error> = _error
    private val _jwt : MutableLiveData<Token> = MutableLiveData()
    val jwt : LiveData<Token> = _jwt


    private var webService = RetrofitConfigurationService.getInstance(application).webService()
    private var userMapper  = UserMapper
    private  var tokenMapper = TokenMapper

    fun addUser(user: User){

        webService.postUser(userMapper.mapToUserDto(user)!!).enqueue(object : Callback<TokenDto> {
            override fun onResponse(call: Call<TokenDto>, response: Response<TokenDto>) {
                if (response.isSuccessful) {
                    System.out.println(response.body()!!.token)
                    _error.value = Error.NO_ERROR
                    _jwt.value = tokenMapper.mapToToken(response.body()!!)
                } else {
                    if (response.code() == 409)
                        _error.value = Error.USER_ALREADY_EXIST
                    else
                        _error.value = Error.REQUEST_ERROR
                }
            }

            override fun onFailure(call: Call<TokenDto>, t: Throwable) {
                if (t is NoConnectivityException) {
                    _error.value = Error.NO_CONNECTION
                } else {
                    System.out.println(t)
                    _error.value = Error.TECHNICAL_ERROR
                }
            }

        })
    }


    private fun validateEmail() {
        emailMediator.value = email.value!!.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
    }

    private fun validatePassword(){
        passwordMediator.value = password.value!!.isNotEmpty()
    }

    private fun validateValidationPassword(){
        passwordValidationMediator.value = passwordValidation.value!!.isNotEmpty() && passwordValidation.value!!.equals(password.value)
    }

    private fun validateFirstname(){
            firstNameMediator.value = firstname.value!!.isNotEmpty() && Pattern.compile("^[a-zéèçàïôëA-Z]{1,50}(-| )?([a-zéèçàïôëA-Z]{1,50})?$").matcher(firstname.value).matches()
    }

    private fun validateLastname(){
        lastnameMediator.value = lastname.value!!.isNotEmpty() && Pattern.compile("^[a-zéèçàïôëA-Z]{1,50}(-| )?([a-zéèçàïôëA-Z]{1,50})?$").matcher(lastname.value).matches()
    }

    private fun validatePhone(){
        phoneMediator.value = phone.value!!.isNotEmpty() && Patterns.PHONE.matcher(phone.value).matches()
    }

    private fun validateStreetName(){
        streetNameMediator.value = streetName.value!!.isNotEmpty() && Pattern.compile("^\\s*[a-zA-Z]{1}[a-zA-Z][a-zA-Z '-]*\$").matcher(streetName.value).matches()
    }

    private fun validateStreetNumber(){
        streetNumberMediator.value = streetNumber.value!!.isNotEmpty() && Pattern.compile("^(\\d{1,3})\\w{0,3}\$").matcher(streetNumber.value).matches()
    }

    private fun validateLocality(){
        localityMediator.value = locality.value!!.isNotEmpty() && Pattern.compile("^\\s*[a-zA-Z]{1}[a-zA-Z][a-zA-Z '-]*\$").matcher(locality.value).matches()
    }

    private fun validatePostalCode(){
        postalCodeMediator.value = postalCode.value!!.isNotEmpty() && Pattern.compile("^(\\d{4,10})\$").matcher(postalCode.value).matches()
    }




    override fun onCleared() {
        // DO NOT forget to remove sources from mediator
        emailMediator.removeSource(email)
        passwordMediator.removeSource(password)
        passwordValidationMediator.removeSource(passwordValidation)
        firstNameMediator.removeSource(firstname)
        lastnameMediator.removeSource(lastname)
        phoneMediator.removeSource(phone)
        streetNameMediator.removeSource(streetName)
        streetNumberMediator.removeSource(streetNumber)
        localityMediator.removeSource(locality)
        postalCodeMediator.removeSource(postalCode)
    }

    fun getEmailMediator(): LiveData<Boolean> {
        return emailMediator
    }

    fun getPasswordMediator(): LiveData<Boolean> {
        return passwordMediator
    }

    fun getValidationPasswordMediator(): LiveData<Boolean> {
        return passwordValidationMediator
    }

    fun getFirstnameMediator(): LiveData<Boolean> {
        return firstNameMediator
    }
    fun getLastnameMediator(): LiveData<Boolean> {
        return lastnameMediator
    }
    fun getPhoneMediator(): LiveData<Boolean> {
        return phoneMediator
    }
    fun getStreetNameMediator(): LiveData<Boolean> {
        return streetNameMediator
    }
    fun getStreetNumberMediator(): LiveData<Boolean> {
        return streetNumberMediator
    }
    fun getLocalityMediator(): LiveData<Boolean> {
        return localityMediator
    }
    fun getPostalCodeMediator(): LiveData<Boolean> {
        return postalCodeMediator
    }












}