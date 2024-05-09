package pt.ulisboa.tecnico.cmov.frontend.data

import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

interface AppContainer {
    val pharmacyRepository: PharmacyRepository
}

class DefaultAppContainer : AppContainer {
    private var database: FirebaseDatabase = Firebase.database.apply {
        setPersistenceEnabled(true)
    }

    override val pharmacyRepository: PharmacyRepository = FirebasePharmacyRepository(database)
}