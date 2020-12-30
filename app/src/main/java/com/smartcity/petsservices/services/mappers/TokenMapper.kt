package com.smartcity.petsservices.services.mappers

import com.auth0.android.jwt.Claim
import com.auth0.android.jwt.JWT
import com.smartcity.petsservices.model.Token
import com.smartcity.petsservices.repositories.web.dto.TokenDto
import java.util.*


object TokenMapper {

    fun mapToToken(tokenDto: TokenDto) : Token{
        val parsedJWT = JWT(tokenDto.token);
        val userId : Int? = parsedJWT.getClaim("userId").asInt()
        val email : String? = parsedJWT.getClaim("email").asString()
        val exp : Date ? = parsedJWT.expiresAt

        return Token(email,userId, exp)

    }
}