package pt.ulisboa.tecnico.cmov.frontend.ui.search_medicine_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.ulisboa.tecnico.cmov.frontend.PharmacISTApplication
import pt.ulisboa.tecnico.cmov.frontend.data.MedicineRepository

class SearchMedicineViewModel(
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchMedicineUIState())
    val uiState: StateFlow<SearchMedicineUIState> = _uiState.asStateFlow()

    fun updateQuery(updatedQuery: String) {
        _uiState.update { currentState ->
            currentState.copy(
                query = updatedQuery
            )
        }
    }

    fun searchMedicine() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    results = medicineRepository.searchMedicines(_uiState.value.query)
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PharmacISTApplication)
                SearchMedicineViewModel(
                    medicineRepository = application.container.medicineRepository
                )
            }
        }
    }
}