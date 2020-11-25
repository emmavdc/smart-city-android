package com.smartcity.petsservices.repositories.web.webservice

import retrofit2.Call
import com.smartcity.petsservices.repositories.web.dto.UserDto
import retrofit2.http.POST

interface WebService {

    @POST("users")
    fun postUser() : Call<UserDto>
}