package com.smartcity.petsservices.services.mappers

import com.smartcity.petsservices.model.Login
import com.smartcity.petsservices.repositories.web.dto.LoginDto

object LoginMapper {

    fun mapToLoginDto(login : Login) : LoginDto {
        return LoginDto(login.email, login.password)
    }
}