package com.smartcity.petsservices.utils

import java.io.IOException

class NoConnectivityException :IOException() {
    override val message: String
        get() = "No Internet Connection Exception"
}