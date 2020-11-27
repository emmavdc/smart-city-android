package com.smartcity.petsservices.repositories.web.dto


data class UserDto(val email : String,
                   val password : String,
                   val firstname : String,
                   val lastname : String,
                   val phone : String,
                   val locality : String,
                   val postalCode : Int,
                   val streetNumber : String,
                   val streetName :  String,
                   val country : String,
                   val picture : String?,
                   val customer : CustomerDto?,
                   val supplier: SupplierDto?) {
}