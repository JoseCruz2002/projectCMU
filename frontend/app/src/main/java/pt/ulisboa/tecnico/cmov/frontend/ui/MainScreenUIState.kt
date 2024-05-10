package pt.ulisboa.tecnico.cmov.frontend.ui

import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy

data class MainScreenUIState(
    val pharmacies: List<Pharmacy>,
    val query: String = "",
    val active: Boolean = false
)
