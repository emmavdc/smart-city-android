package com.smartcity.petsservices.repositories.web.dto

data class CustomerDto (val commune : String?,
                        val searchWalker : Boolean,
                        val searchHost : Boolean) {
}