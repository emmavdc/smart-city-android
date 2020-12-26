package com.smartcity.petsservices.model

import com.smartcity.petsservices.R

enum class NetworkError(var errorMessage : Int) {
    NO_CONNECTION(R.string.connectivity_error),
    REQUEST_ERROR(R.string.request_error),
    USER_ALREADY_EXIST(R.string.user_already_exist),
    TECHNICAL_ERROR(R.string.technical_error),
    NO_ERROR(R.string.no_error)
}