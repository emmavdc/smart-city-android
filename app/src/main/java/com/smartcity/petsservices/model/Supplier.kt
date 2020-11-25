package com.smartcity.petsservices.model

data class Supplier(val isHost : Boolean,
                    val isAnimalWalker :Boolean,
                    val slogan : String?,
                    val commune : String?,
                    val weightMax :  Int?) {
}