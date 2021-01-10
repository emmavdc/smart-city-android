package com.smartcity.petsservices.repositories.web.dto

data class SupplierDto (val isHost : Boolean,
                        val isAnimalWalker :Boolean,
                        val slogan : String?,
                        val locality : String?,
                        val weightMax :  Int?) {
}