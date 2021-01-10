package com.smartcity.petsservices.services.mappers

import com.auth0.android.jwt.Claim
import com.auth0.android.jwt.JWT
import com.smartcity.petsservices.model.Token
import com.smartcity.petsservices.model.Value
import com.smartcity.petsservices.repositories.web.dto.TokenDto
import java.util.*


object TokenMapper {

    fun mapToToken(tokenDto: TokenDto) : Token{
        val parsedJWT = JWT(tokenDto.token);
        val allClaims: Map<String, Claim> = parsedJWT.getClaims()
        val value: Value? = allClaims.getValue("value").asObject<Value>(Value::class.java)
        val userId : Int = value!!.userId
        val email : String = value!!.email
        val exp : Date ? = parsedJWT.expiresAt
        val token = tokenDto.token

        return Token(email, userId, exp, token)

    }
}

