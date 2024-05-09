package pt.ulisboa.tecnico.cmov.frontend.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.await
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy

const val PHARMACY_REFERENCE_PATH = "pharmacies"

interface PharmacyRepository {
    suspend fun getPharmacies(): List<Pharmacy>
    suspend fun addPharmacy(pharmacy: Pharmacy)
}

class FirebasePharmacyRepository(
    private val database: FirebaseDatabase
) : PharmacyRepository {
    private val databaseRef: DatabaseReference by lazy {
        database.getReference(PHARMACY_REFERENCE_PATH)
    }

    override suspend fun getPharmacies(): List<Pharmacy> {
        return try {
            val snapshot = databaseRef.get().await()
            snapshot.getValue<Map<String, Pharmacy>>()?.values?.toList() ?: emptyList()
        } catch (e: Exception) {
            // Handle exception
            emptyList()
        }
    }

    override suspend fun addPharmacy(pharmacy: Pharmacy) {
        try {
            val newPharmacyReference = databaseRef.push()
            newPharmacyReference.setValue(pharmacy).await()
        } catch (e: Exception) {
            // Handle exception
        }
    }
}