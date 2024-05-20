package pt.ulisboa.tecnico.cmov.frontend.data

import android.net.Uri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy

const val PHARMACY_REFERENCE_PATH = "pharmacies"
const val IMAGES_REFERENCE_PATH = "pharmacies"

interface PharmacyRepository {
    suspend fun getPharmacies(): List<Pharmacy>
    suspend fun getPharmacy(id: String): Pharmacy
    suspend fun addPharmacy(pharmacy: Pharmacy, uri: Uri)
}

class FirebasePharmacyRepository(
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage
) : PharmacyRepository {
    private val databaseRef: DatabaseReference by lazy {
        database.getReference(PHARMACY_REFERENCE_PATH)
    }
    private val storageRef = storage.getReference(IMAGES_REFERENCE_PATH)

    override suspend fun getPharmacies(): List<Pharmacy> {
        return try {
            val snapshot = databaseRef.get().await()
            snapshot.getValue<Map<String, Pharmacy>>()?.map {
                it.value.id = it.key
                it.value
            } ?: emptyList()
        } catch (e: Exception) {
            // Handle exception
            emptyList()
        }
    }

    override suspend fun getPharmacy(id: String): Pharmacy {
        return databaseRef
            .child(id)
            .get()
            .await()
            .getValue<Pharmacy>()
            ?.apply { this.id = id }
            ?: Pharmacy()
    }

    override suspend fun addPharmacy(pharmacy: Pharmacy, uri: Uri) {
        try {
            val photoRef = storageRef.child(uri.lastPathSegment!!)

            photoRef.putFile(uri).continueWithTask{
                photoRef.downloadUrl
            }.addOnCompleteListener{ task ->
                databaseRef.push().setValue(pharmacy.copy(img = task.result.toString()))
            }
        } catch (e: Exception) {
            // Handle exception
        }
    }
}