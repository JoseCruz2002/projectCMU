package pt.ulisboa.tecnico.cmov.frontend.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Medicine(
    @get:Exclude
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var img: String = "",
    @get:Exclude
    var pharmacies: List<MedicinePharmacy> = emptyList()
)

@IgnoreExtraProperties
data class MedicinePharmacy(
    var pharmacy: Pharmacy = Pharmacy(),
    val quantity: Int = 0
)