package com.devdroid07.storeapp.store.presentation.payment

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.clearText
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdroid07.storeapp.R
import com.devdroid07.storeapp.core.domain.util.Result
import com.devdroid07.storeapp.core.presentation.ui.UiText
import com.devdroid07.storeapp.core.presentation.ui.asUiText
import com.devdroid07.storeapp.navigation.util.NavArgs
import com.devdroid07.storeapp.store.domain.usecases.card.CardUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val cardUseCases: CardUseCases,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(PaymentState())
    val state: StateFlow<PaymentState> get() = _state.asStateFlow()

    private val eventChannel = Channel<PaymentEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        _state.update { paymentState ->
            paymentState.copy(
                addressId = savedStateHandle[NavArgs.AddressID.key] ?: "1"
            )
        }
        getAllMyCard()
    }

    fun onAction(
        action: PaymentAction,
    ) {
        when (action) {
            PaymentAction.OnCreateCardClick -> createCard()
            PaymentAction.OnRetryClick -> getAllMyCard()
            else -> Unit
        }
    }

    private fun getAllMyCard() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            cardUseCases.getAllMyCardsUseCase().collectLatest { result ->
                _state.update { paymentState ->
                    when (result) {
                        is Result.Error -> {
                            paymentState.copy(
                                isLoading = false,
                                error = result.error.asUiText()
                            )
                        }
                        is Result.Success -> {
                            paymentState.copy(
                                error = null,
                                isLoading = false,
                                cards = result.data
                            )
                        }
                    }
                }
            }


        }
    }

    private fun createCard() {
        viewModelScope.launch {
            _state.update { paymentState ->
                paymentState.copy(
                    isCreatingCard = true
                )
            }
            val result = cardUseCases.createCardUseCase(
                cardNumber = state.value.numberCard.text.toString().replace(
                    " ",
                    ""
                ),
                cvv = state.value.cvv.text.toString(),
                expireDate = state.value.expireDate.text.toString(),
                nameHeadline = state.value.nameHeadlineCard.text.toString()
            )
            _state.update { paymentState ->
                when (result) {
                    is Result.Error -> {
                        eventChannel.send(
                            PaymentEvent.Error(UiText.StringResource(R.string.error_create_card))
                        )
                        paymentState.copy(
                            isCreatingCard = false,
                            cards = emptyList()
                        )

                    }
                    is Result.Success -> {
                        clearTextFieldState()
                        eventChannel.send(
                            PaymentEvent.Success(UiText.StringResource(R.string.success_create_card))
                        )
                        val newList = paymentState.cards.toMutableList()
                        newList.add(result.data)
                        paymentState.copy(
                            isCreatingCard = false,
                            cards = newList
                        )

                    }
                }
            }
        }
    }

    private fun clearTextFieldState() {
        state.value.apply {
            numberCard.clearText()
            cvv.clearText()
            expireDate.clearText()
            nameHeadlineCard.clearText()
        }
    }
}
