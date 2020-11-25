package com.smartcity.petsservices.repositories.web.dto


data class UserDto(val email : String,
                   val password : String,
                   val firstname : String,
                   val lastname : String,
                   val phone : String,
                   val is_admin :  Boolean,
                   val locality : String,
                   val postal_code : Int,
                   val street_number : String,
                   val street_name :  String,
                   val country : String,
                   val picture : String?,
                   val customer : CustomerDto?,
                   val supplier: SupplierDto?) {
}