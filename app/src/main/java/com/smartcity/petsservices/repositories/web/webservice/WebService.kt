package com.smartcity.petsservices.repositories.web.webservice

import com.smartcity.petsservices.repositories.web.dto.UserDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface WebService {

    @POST("users")
    fun postUser(@Body userDto: UserDto) : Call<UserDto>
}