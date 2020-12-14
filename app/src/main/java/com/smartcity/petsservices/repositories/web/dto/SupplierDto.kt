package com.smartcity.petsservices.repositories.web.dto

data class SupplierDto (val isHost : Boolean,
                        val isAnimalWalker :Boolean,
                        val slogan : String?,
                        val commune : String?,
                        val weightMax :  Int?) {
}