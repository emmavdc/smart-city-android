package com.smartcity.petsservices.repositories.web.dto

data class CustomerDto (val locality : String?,
                        val searchWalker : Boolean,
                        val searchHost : Boolean) {
}