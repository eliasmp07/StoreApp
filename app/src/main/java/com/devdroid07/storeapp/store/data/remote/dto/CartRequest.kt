package com.devdroid07.storeapp.store.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CartRequest(
    @SerializedName("userId") val idUser: String,
    @SerializedName("productId") val productId: String,
    @SerializedName("quantity") val quantity: Int
)
