package com.smartcity.petsservices.repositories.web.dto

data class CustomerDto (val commune : String?,
                        val search_walker : Boolean,
                        val search_host : Boolean) {
}