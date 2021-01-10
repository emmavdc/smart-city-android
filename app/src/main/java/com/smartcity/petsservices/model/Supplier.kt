package com.smartcity.petsservices.model

data class Supplier(val isHost : Boolean,
                    val isAnimalWalker :Boolean,
                    val slogan : String?,
                    val locality : String?,
                    val weightMax :  Int?) {
}