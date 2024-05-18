package pt.ulisboa.tecnico.cmov.frontend.ui.search_medicine_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchMedicineViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchMedicineUIState())
    val uiState: StateFlow<SearchMedicineUIState> = _uiState.asStateFlow()

    fun updateQuery(updatedQuery: String) {
        _uiState.update { currentState ->
            currentState.copy(
                query = updatedQuery
            )
        }
    }
}