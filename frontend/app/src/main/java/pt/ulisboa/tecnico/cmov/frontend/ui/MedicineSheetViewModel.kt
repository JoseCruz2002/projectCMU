package pt.ulisboa.tecnico.cmov.frontend.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MedicineSheetViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MedicineSheetUIState())
    val uiState: StateFlow<MedicineSheetUIState> = _uiState.asStateFlow()

    fun updateShowBottomSheet(updatedShowBottomSheet: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                showBottomSheet = updatedShowBottomSheet
            )
        }
    }

}