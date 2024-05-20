package pt.ulisboa.tecnico.cmov.frontend.data

import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage

interface AppContainer {
    val pharmacyRepository: PharmacyRepository
}

class DefaultAppContainer : AppContainer {
    private var database: FirebaseDatabase = Firebase.database.apply {
        setPersistenceEnabled(true)
    }

    private var storage: FirebaseStorage = Firebase.storage

    override val pharmacyRepository: PharmacyRepository = FirebasePharmacyRepository(database, storage)
}