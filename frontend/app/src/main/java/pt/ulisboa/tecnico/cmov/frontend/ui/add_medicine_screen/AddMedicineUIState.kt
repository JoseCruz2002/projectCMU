package pt.ulisboa.tecnico.cmov.frontend.ui.add_medicine_screen

import android.net.Uri

data class AddMedicineUIState(
    val name: String = "",
    val description: String = "",
    val quantity: Int = 0,
    val imageUri: Uri = Uri.EMPTY
)
