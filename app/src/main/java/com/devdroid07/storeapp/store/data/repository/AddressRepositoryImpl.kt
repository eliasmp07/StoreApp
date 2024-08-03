package com.devdroid07.storeapp.store.data.repository

import com.devdroid07.storeapp.core.data.network.safeCall2
import com.devdroid07.storeapp.core.domain.SessionStorage
import com.devdroid07.storeapp.core.domain.util.DataError
import com.devdroid07.storeapp.core.domain.util.EmptyResult
import com.devdroid07.storeapp.core.domain.util.Result
import com.devdroid07.storeapp.core.domain.util.asEmptyDataResult
import com.devdroid07.storeapp.store.data.mappers.toAddress
import com.devdroid07.storeapp.store.data.mappers.toPostalCode
import com.devdroid07.storeapp.store.data.remote.api.CopomexApi
import com.devdroid07.storeapp.store.data.remote.api.StoreApiService
import com.devdroid07.storeapp.store.data.remote.dto.AddressRequest
import com.devdroid07.storeapp.store.domain.model.Address
import com.devdroid07.storeapp.store.domain.model.PostalCode
import com.devdroid07.storeapp.store.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddressRepositoryImpl (
    private val storeApiService: StoreApiService,
    private val copomexApi: CopomexApi,
    private val sessionStorage: SessionStorage
): AddressRepository {

    override suspend fun getInfoByPostalCode(postalCode: String): Flow<Result<List<PostalCode>, DataError.Network>> = flow {
        val result = safeCall2 {
            copomexApi.getInfoByPostalCode(
                codigoPostal = postalCode,
                token = "pruebas"
            )
        }
        when (result) {
            is Result.Error -> {
                emit(
                    Result.Error(
                        result.error
                    )
                )
            }
            is Result.Success -> {
                emit(Result.Success(
                    result.data.map {
                        it.postalCodeDto?.toPostalCode() ?: PostalCode()
                    }
                ))
            }
        }
    }

    override suspend fun getAllMyAddress(): Result<List<Address>, DataError.Network> {
        val result = safeCall2 {
            storeApiService.getAllMyAddress(
                userId = sessionStorage.get()?.id.orEmpty()
            )
        }
        return when (result) {
            is Result.Error -> {
                Result.Error(result.error)
            }
            is Result.Success -> {
                Result.Success(result.data.data?.map {
                    it.toAddress()
                }.orEmpty())
            }
        }
    }

    override suspend fun createAddress(
        street: String,
        postalCode: String,
        state: String,
        mayoralty: String,
        settlement: String,
        phoneNumber: String,
        reference: String,
    ): Result<Address, DataError.Network> {
        val result = safeCall2 {
            storeApiService.createAddress(
                addressRequest = AddressRequest(
                    userId = sessionStorage.get()?.id.orEmpty(),
                    street = street,
                    postalCode = postalCode,
                    state = state,
                    mayoralty = mayoralty,
                    settlement = settlement,
                    phoneNumber = phoneNumber,
                    reference = reference
                )
            )
        }
        return when (result) {
            is Result.Error -> {
                Result.Error(result.error)
            }
            is Result.Success -> {
                Result.Success(
                    result.data.data?.toAddress() ?: Address()
                )
            }
        }
    }

    override suspend fun deleteAddress(idAddress: Int): EmptyResult<DataError.Network> {
        val result = safeCall2 {
            storeApiService.deleteAddress(idAddress.toString())
        }
        return result.asEmptyDataResult()
    }
}