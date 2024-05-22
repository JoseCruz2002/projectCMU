package pt.ulisboa.tecnico.cmov.frontend.ui.add_medicine_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.ulisboa.tecnico.cmov.frontend.MEDICINE_ID_ARG
import pt.ulisboa.tecnico.cmov.frontend.PharmacISTApplication
import pt.ulisboa.tecnico.cmov.frontend.data.MedicineRepository
import pt.ulisboa.tecnico.cmov.frontend.model.Medicine

class AddMedicineViewModel(
    savedStateHandle: SavedStateHandle,
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    val medicineId: String = checkNotNull(savedStateHandle[MEDICINE_ID_ARG])

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
                quantity = updatedQuantity.toInt()
            )
        }
    }

    fun incrementQuantity() {
        val currentQuantity = _uiState.value.quantity
        updateQuantity((currentQuantity + 1).toString())
    }

    fun decrementQuantity() {
        val currentQuantity = _uiState.value.quantity
        val newQuantity = maxOf(0, currentQuantity - 1)
        updateQuantity(newQuantity.toString())
    }

    fun addMedicine() {
        viewModelScope.launch {
            medicineRepository.addMedicine(
                Medicine(
                    id = medicineId,
                    name = _uiState.value.name,
                    description = _uiState.value.description,
                    img = "",
                    pharmacies = emptyList()
                )
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PharmacISTApplication)
                AddMedicineViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    medicineRepository = application.container.medicineRepository
                )
            }
        }
    }
}