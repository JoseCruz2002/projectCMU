package pt.ulisboa.tecnico.cmov.frontend.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddPharmacyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddPharmacyUIState())
    val uiState: StateFlow<AddPharmacyUIState> = _uiState.asStateFlow()

    fun updateName(updatedName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                name = updatedName
            )
        }
    }

    fun updateAddress(updatedAddress: String) {
        _uiState.update { currentState ->
            currentState.copy(
                address = updatedAddress
            )
        }
    }
}