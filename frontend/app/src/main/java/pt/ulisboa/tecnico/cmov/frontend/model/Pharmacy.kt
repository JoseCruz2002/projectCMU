package pt.ulisboa.tecnico.cmov.frontend.model

data class Pharmacy(
    val name: String,
    val location: String,
    val img: String,
    val medicines: List<Pair<Medicine, Int>>
)
