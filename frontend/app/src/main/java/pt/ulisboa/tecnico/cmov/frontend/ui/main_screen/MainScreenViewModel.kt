package pt.ulisboa.tecnico.cmov.frontend.ui.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.ulisboa.tecnico.cmov.frontend.PharmacISTApplication
import pt.ulisboa.tecnico.cmov.frontend.data.PharmacyRepository

class MainScreenViewModel(private val pharmacyRepository: PharmacyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUIState(listOf()))
    val uiState: StateFlow<MainScreenUIState> = _uiState.asStateFlow()

    init {
        getPharmacies()
    }

    private fun getPharmacies() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    pharmacies = pharmacyRepository.getPharmacies()
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PharmacISTApplication)
                val pharmacyRepository = application.container.pharmacyRepository
                MainScreenViewModel(pharmacyRepository = pharmacyRepository)
            }
        }
    }

    fun updateQuery(newQuery: String) {
        _uiState.update { currentState ->
            currentState.copy(
                query = newQuery
            )
        }
    }

    fun updateActive(newActive: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                active = newActive
            )
        }
    }
}