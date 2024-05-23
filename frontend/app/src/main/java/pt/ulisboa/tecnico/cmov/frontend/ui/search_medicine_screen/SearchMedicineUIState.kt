package pt.ulisboa.tecnico.cmov.frontend.ui.search_medicine_screen

import pt.ulisboa.tecnico.cmov.frontend.model.Medicine

data class SearchMedicineUIState(
    val query: String = "",
    val results: List<Medicine>? = null
)
