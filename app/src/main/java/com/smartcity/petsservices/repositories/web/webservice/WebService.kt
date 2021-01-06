package com.smartcity.petsservices.repositories.web.webservice

import com.smartcity.petsservices.repositories.web.dto.LoginDto
import com.smartcity.petsservices.repositories.web.dto.TokenDto
import com.smartcity.petsservices.repositories.web.dto.UserDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface WebService {

    @POST("v2/users")
    fun postUser(@Body userDto: UserDto) : Call<TokenDto>

    /*@GET("pizza/{pizzaId}")
    fun getPizza(@Path("pizzaId") pizzaId: Int?): Call<PizzaDto?>?*/

    @POST("v2/users/actions/login")
    fun loginUser(@Body loginDto: LoginDto) : Call<TokenDto>
}