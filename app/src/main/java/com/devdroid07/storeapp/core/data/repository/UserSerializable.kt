package com.devdroid07.storeapp.core.data.repository

import kotlinx.serialization.Serializable

@Serializable
data class UserSerializable(
    val id: String,
    val email: String,
    val name: String,
    val lastname: String,
    val image: String,
    val token: String
)
