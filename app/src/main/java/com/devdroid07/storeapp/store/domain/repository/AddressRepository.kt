package com.devdroid07.storeapp.store.domain.repository

import com.devdroid07.storeapp.core.domain.util.DataError
import com.devdroid07.storeapp.core.domain.util.EmptyResult
import com.devdroid07.storeapp.core.domain.util.Result
import com.devdroid07.storeapp.store.domain.model.Address
import com.devdroid07.storeapp.store.domain.model.PostalCode
import kotlinx.coroutines.flow.Flow

interface AddressRepository {

    suspend fun getInfoByPostalCode(postalCode: String): Flow<Result<List<PostalCode>, DataError.Network>>

    suspend fun getAllMyAddress(): Flow<Result<List<Address>, DataError.Network>>

    suspend fun createAddress(
        street: String,
        postalCode: String,
        state: String,
        mayoralty: String,
        settlement: String,
        phoneNumber: String,
        reference: String,
    ): Result<Address, DataError.Network>

    suspend fun deleteAddress(idAddress: Int): EmptyResult<DataError.Network>
}