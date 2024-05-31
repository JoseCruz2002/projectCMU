package pt.ulisboa.tecnico.cmov.frontend.ui.medicine_screen

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.ulisboa.tecnico.cmov.frontend.MEDICINE_ID_ARG
import pt.ulisboa.tecnico.cmov.frontend.PharmacISTApplication
import pt.ulisboa.tecnico.cmov.frontend.data.MedicineRepository
import pt.ulisboa.tecnico.cmov.frontend.data.PharmacyRepository
import pt.ulisboa.tecnico.cmov.frontend.model.Medicine
import pt.ulisboa.tecnico.cmov.frontend.model.MedicinePharmacy

class MedicineViewModel(
    savedStateHandle: SavedStateHandle,
    private val medicineRepository: MedicineRepository,
    private val pharmacyRepository: PharmacyRepository,
) : ViewModel() {

    private val itemId: String = checkNotNull(savedStateHandle[MEDICINE_ID_ARG])

    private val _uiState = MutableStateFlow(MedicineUIState(Medicine()))
    val uiState: StateFlow<MedicineUIState> = _uiState.asStateFlow()

    init {
        getMedicine(itemId)
    }

    private fun getMedicine(medicineId: String) {
        viewModelScope.launch {
            val medicine = medicineRepository.getMedicine(medicineId)
            medicine.pharmacies = medicineRepository.getMedicinePharmacies(medicineId)
            medicine.pharmacies =
                pharmacyRepository.getPharmacies(medicine.pharmacies.map { it.pharmacy.id })
                    .map { MedicinePharmacy(it, 1) }
            _uiState.update { currentState ->
                currentState.copy(
                    medicine = medicine
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PharmacISTApplication)
                val medicineRepository = application.container.medicineRepository
                val pharmacyRepository = application.container.pharmacyRepository
                MedicineViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    medicineRepository = medicineRepository,
                    pharmacyRepository = pharmacyRepository
                )
            }
        }
    }
}