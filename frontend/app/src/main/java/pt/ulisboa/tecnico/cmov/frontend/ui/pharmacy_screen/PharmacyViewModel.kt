package pt.ulisboa.tecnico.cmov.frontend.ui.pharmacy_screen

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
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy

class PharmacyViewModel(
    private val pharmacyRepository: PharmacyRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PharmacyUIState(Pharmacy()))
    val uiState: StateFlow<PharmacyUIState> = _uiState.asStateFlow()

    init {
        //TODO("Call to getPharmacy should be done on init")
    }

    fun getPharmacy(pharmacyId: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    pharmacy = pharmacyRepository.getPharmacy(pharmacyId)
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PharmacISTApplication)
                val pharmacyRepository = application.container.pharmacyRepository
                PharmacyViewModel(pharmacyRepository = pharmacyRepository)
            }
        }
    }
}