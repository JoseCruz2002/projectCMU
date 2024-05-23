package pt.ulisboa.tecnico.cmov.frontend.ui.medicine_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
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

class MedicineViewModel(
    savedStateHandle: SavedStateHandle,
    private val medicineRepository: MedicineRepository,
) : ViewModel() {

    private val itemId: String = checkNotNull(savedStateHandle[MEDICINE_ID_ARG])

    private val _uiState = MutableStateFlow(MedicineUIState(Medicine()))
    val uiState: StateFlow<MedicineUIState> = _uiState.asStateFlow()

    init {
        getMedicine(itemId)
    }

    private fun getMedicine(medicineId: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    medicine = medicineRepository.getMedicine(medicineId)
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PharmacISTApplication)
                val medicineRepository = application.container.medicineRepository
                MedicineViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    medicineRepository = medicineRepository
                )
            }
        }
    }
}