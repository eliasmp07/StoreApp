package com.devdroid07.storeapp.store.data.network.dto.store

import com.google.gson.annotations.SerializedName

data class AddressUpdateRequest(
    @SerializedName("id") val id: String,
    @SerializedName("street") val street: String,
    @SerializedName("postal_code") val postalCode: String,
    @SerializedName("state") val state: String,
    @SerializedName("mayoralty") val mayoralty: String,
    @SerializedName("settlement") val settlement: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("reference") val reference: String,
)
