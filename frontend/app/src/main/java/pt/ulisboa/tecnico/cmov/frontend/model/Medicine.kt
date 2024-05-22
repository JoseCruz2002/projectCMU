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
    var pharmacies: List<Pharmacy> = emptyList()
)
