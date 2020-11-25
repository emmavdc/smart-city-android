package com.smartcity.petsservices.services.mappers

import com.smartcity.petsservices.model.Customer
import com.smartcity.petsservices.model.Supplier
import com.smartcity.petsservices.model.User
import com.smartcity.petsservices.repositories.web.dto.CustomerDto
import com.smartcity.petsservices.repositories.web.dto.SupplierDto
import com.smartcity.petsservices.repositories.web.dto.UserDto

object UserMapper {

    fun mapToUserDto(user : User?) : UserDto?{
        if(user == null) {
            return null
        }
        else{
            return UserDto(user.email,
                    user.password,
                    user.firstname,
                    user.lastname,
                    user.phone,
                    false,
                    user.locality,
                    user.postalCode, user.streetNumber, user.streetName, user.country, user.picture, mapToCustomerDto(user.customer), mapToSupplierDto(user.supplier))
        }
    }

    private fun mapToCustomerDto(customer: Customer?) : CustomerDto?{
        if(customer == null)  return null
        else return CustomerDto(customer.commune, customer.searchWalker, customer.searchHost)
    }

    private fun mapToSupplierDto(supplier: Supplier?) : SupplierDto?{
        if(supplier == null)  return null
        else return SupplierDto(supplier.isHost,
                supplier.isAnimalWalker,
                supplier.slogan,
                supplier.commune,
                supplier.weightMax)
    }
}