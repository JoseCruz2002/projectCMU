package pt.ulisboa.tecnico.cmov.frontend.model

data class User(
    val username: String,
    val favouritePharmacies: List<Pharmacy>,
    val subscribedMedicines: List<Medicine>
)
