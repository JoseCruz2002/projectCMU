package pt.ulisboa.tecnico.cmov.frontend.model

data class Medicine(
    val name: String,
    val description: String,
    val img: String,
    val pharmacies: List<Pharmacy>
)
