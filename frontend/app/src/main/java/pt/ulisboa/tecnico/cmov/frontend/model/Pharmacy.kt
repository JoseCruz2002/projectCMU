package pt.ulisboa.tecnico.cmov.frontend.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Pharmacy(
    @get:Exclude
    var id: String = "",
    val name: String = "",
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val img: String = "",
    @get:Exclude
    var medicines: List<PharmacyMedicine> = emptyList()
)

@IgnoreExtraProperties
data class PharmacyMedicine(
    @get:Exclude
    var medicine: Medicine = Medicine(),
    val quantity: Int = 0
)