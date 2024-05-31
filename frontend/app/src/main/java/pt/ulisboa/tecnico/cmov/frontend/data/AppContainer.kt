package pt.ulisboa.tecnico.cmov.frontend.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage

interface AppContainer {
    val pharmacyRepository: PharmacyRepository
    val medicineRepository: MedicineRepository
    val userRepository: UserRepository
}

class DefaultAppContainer : AppContainer {
    private val database: FirebaseFirestore = Firebase.firestore

    private val storage: FirebaseStorage = Firebase.storage

    override val pharmacyRepository: PharmacyRepository =
        FirebasePharmacyRepository(database, storage)

    override val medicineRepository: MedicineRepository = FirebaseMedicineRepository(database,storage)

    override val userRepository: UserRepository = FirebaseUserRepository(database)
}