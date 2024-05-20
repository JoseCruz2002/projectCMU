package pt.ulisboa.tecnico.cmov.frontend.ui.add_pharmacy_screen

import android.net.Uri

data class AddPharmacyUIState(
    val name: String = "",
    val address: String = "",
    val imageUri: Uri = Uri.EMPTY
)
