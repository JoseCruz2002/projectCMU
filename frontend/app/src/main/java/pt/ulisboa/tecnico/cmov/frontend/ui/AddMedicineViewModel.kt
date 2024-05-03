package pt.ulisboa.tecnico.cmov.frontend.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddMedicineViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddMedicineUIState())
    val uiState: StateFlow<AddMedicineUIState> = _uiState.asStateFlow()

    fun updateName(updatedName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                name = updatedName
            )
        }
    }

    fun updateDescription(updatedDescription: String) {
        _uiState.update { currentState ->
            currentState.copy(
                description = updatedDescription
            )
        }
    }

    fun updateQuantity(updatedQuantity: String) {
        _uiState.update { currentState ->
            currentState.copy(
                quantity = updatedQuantity
            )
        }
    }
}