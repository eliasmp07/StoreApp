package com.devdroid07.storeapp.store.domain.usecases

import com.devdroid07.storeapp.core.domain.util.DataError
import com.devdroid07.storeapp.core.domain.util.Result
import com.devdroid07.storeapp.store.domain.repository.ProductRepository

class AddMyCartUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        productId: String,
        quantity: Int
    ): Result<String, DataError.Network> {
        return repository.addMyCart(
            productId = productId,
            quantity = quantity
        )
    }
}