package com.smartcity.petsservices.repositories.web.dto

data class SupplierDto (val is_host : Boolean,
                        val is_animal_walker :Boolean,
                        val slogan : String?,
                        val commune : String?,
                        val weight_max :  Int?) {
}