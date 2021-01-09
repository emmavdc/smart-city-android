package com.smartcity.petsservices.repositories.web.webservice

import androidx.room.Update
import com.smartcity.petsservices.repositories.web.dto.LoginDto
import com.smartcity.petsservices.repositories.web.dto.TokenDto
import com.smartcity.petsservices.repositories.web.dto.UserDto
import retrofit2.Call
import retrofit2.http.*


interface WebService {

    @POST("v2/users")
    fun postUser(@Body userDto: UserDto) : Call<TokenDto>

    /*@GET("pizza/{pizzaId}")
    fun getPizza(@Path("pizzaId") pizzaId: Int?): Call<PizzaDto?>?*/

    @POST("v2/users/actions/login")
    fun loginUser(@Body loginDto: LoginDto) : Call<TokenDto>

    @GET("/v2/users/{id}")
    fun getUser(@Path("id") userId: Int, @Header("Authorization") authHeader : String) : Call<UserDto>

    @PUT("v2/users/{id}")
    fun putUser(@Body userDto: UserDto, @Path("id") userId: Int, @Header("Authorization") authHeader : String) : Call<String>

}