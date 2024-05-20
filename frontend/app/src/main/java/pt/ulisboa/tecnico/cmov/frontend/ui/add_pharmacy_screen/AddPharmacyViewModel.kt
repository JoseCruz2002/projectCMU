package pt.ulisboa.tecnico.cmov.frontend.ui.add_pharmacy_screen

import android.net.Uri
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

class AddPharmacyViewModel(private val pharmacyRepository: PharmacyRepository) : ViewModel() {

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

    fun updateUri(updatedUri: Uri) {
        _uiState.update { currentState ->
            currentState.copy(
                imageUri = updatedUri
            )
        }
    }

    fun addPharmacy() {
        viewModelScope.launch {
            pharmacyRepository.addPharmacy(
                Pharmacy(
                    name = _uiState.value.name,
                    location = _uiState.value.address,
                    img = "",
                    medicines = mapOf()
                ),
                uri = _uiState.value.imageUri
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PharmacISTApplication)
                val pharmacyRepository = application.container.pharmacyRepository
                AddPharmacyViewModel(pharmacyRepository = pharmacyRepository)
            }
        }
    }
}