package pt.ulisboa.tecnico.cmov.frontend.ui.medicine_screen

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
import kotlinx.coroutines.launch
import pt.ulisboa.tecnico.cmov.frontend.PHARMACY_ID_ARG
import pt.ulisboa.tecnico.cmov.frontend.model.Medicine

class MedicineViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: String = checkNotNull(savedStateHandle[PHARMACY_ID_ARG])

    private val _uiState = MutableStateFlow(MedicineUIState(Medicine()))
    val uiState: StateFlow<MedicineUIState> = _uiState.asStateFlow()

    init {
        getMedicine(itemId)
    }

    private fun getMedicine(medicineId: String) {
        viewModelScope.launch {

        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MedicineViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                )
            }
        }
    }
}