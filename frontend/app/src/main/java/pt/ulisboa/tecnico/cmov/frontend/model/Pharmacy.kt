package pt.ulisboa.tecnico.cmov.frontend.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Pharmacy(
    val name: String = "",
    val location: String = "",
    val img: String = "",
    val medicines: Map<String, Long> = HashMap()
)
