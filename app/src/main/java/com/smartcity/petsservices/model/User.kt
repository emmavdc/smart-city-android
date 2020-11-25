package com.smartcity.petsservices.model

data class User (val email : String,
                 val password : String,
                 val firstname : String,
                 val lastname : String,
                 val phone : String,
                 val isAdmin :  Boolean,
                 val locality : String,
                 val postalCode : Int,
                 val streetNumber : String,
                 val streetName :  String,
                 val country : String,
                 val picture : String?,
                 val customer: Customer?,
                 val supplier: Supplier?){

}