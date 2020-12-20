package com.smartcity.petsservices.repositories.web.webservice

import com.smartcity.petsservices.repositories.web.dto.UserDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface WebService {

    @POST("v1/users")
    fun postUser(@Body userDto: UserDto) : Call<UserDto>

    /*@GET("pizza/{pizzaId}")
    fun getPizza(@Path("pizzaId") pizzaId: Int?): Call<PizzaDto?>?*/
}