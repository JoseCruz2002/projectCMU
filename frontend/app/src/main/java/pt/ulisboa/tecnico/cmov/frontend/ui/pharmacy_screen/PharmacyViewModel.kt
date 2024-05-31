package pt.ulisboa.tecnico.cmov.frontend.ui.pharmacy_screen

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
import pt.ulisboa.tecnico.cmov.frontend.PHARMACY_ID_ARG
import pt.ulisboa.tecnico.cmov.frontend.PharmacISTApplication
import pt.ulisboa.tecnico.cmov.frontend.data.MedicineRepository
import pt.ulisboa.tecnico.cmov.frontend.data.PharmacyRepository
import pt.ulisboa.tecnico.cmov.frontend.data.UserRepository
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy
import pt.ulisboa.tecnico.cmov.frontend.model.PharmacyMedicine

class PharmacyViewModel(
    savedStateHandle: SavedStateHandle,
    private val pharmacyRepository: PharmacyRepository,
    private val medicineRepository: MedicineRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val itemId: String = checkNotNull(savedStateHandle[PHARMACY_ID_ARG])

    private val _uiState = MutableStateFlow(PharmacyUIState(Pharmacy(), false))
    val uiState: StateFlow<PharmacyUIState> = _uiState.asStateFlow()

    init {
        getPharmacy(itemId)
    }

    private fun getPharmacy(pharmacyId: String) {
        viewModelScope.launch {
            val pharmacy = pharmacyRepository.getPharmacy(pharmacyId)
            pharmacy.medicines = pharmacyRepository.getPharmacyMedicines(pharmacyId)
            pharmacy.medicines =
                medicineRepository.getMedicines(pharmacy.medicines.map { it.medicine.id })
                    .map { PharmacyMedicine(it, 1) }
            _uiState.update { currentState ->
                currentState.copy(
                    pharmacy = pharmacy
                )
            }
        }
        viewModelScope.launch {
            val favorite = userRepository.isFavorite(pharmacyId)
            _uiState.update { currentState ->
                currentState.copy(
                    favorite = favorite
                )
            }
        }
    }

    fun toggleFavorite(newValue: Boolean) {
        if (newValue) {
            viewModelScope.launch {
                userRepository.addFavorite(_uiState.value.pharmacy.id)
            }
        } else {
            viewModelScope.launch {
                userRepository.removeFavorite(_uiState.value.pharmacy.id)
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                favorite = newValue
            )
        }
    }

    fun existsMedicine(medicineId: String) {
        viewModelScope.launch {
            val result = medicineRepository.existsMedicine(medicineId)
        
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PharmacISTApplication)
                val pharmacyRepository = application.container.pharmacyRepository
                val medicineRepository = application.container.medicineRepository
                val userRepository = application.container.userRepository
                PharmacyViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    pharmacyRepository = pharmacyRepository,
                    medicineRepository = medicineRepository,
                    userRepository = userRepository
                )
            }
        }
    }
}