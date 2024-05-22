package pt.ulisboa.tecnico.cmov.frontend.data

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
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
    database: FirebaseFirestore,
    storage: FirebaseStorage
) : PharmacyRepository {
    private val databaseRef = database.collection(PHARMACY_REFERENCE_PATH)
    private val storageRef = storage.getReference(IMAGES_REFERENCE_PATH)

    override suspend fun getPharmacies(): List<Pharmacy> {
        return try {
            databaseRef
                .get()
                .await()
                .documents
                .map {
                    it.toObject(Pharmacy::class.java)?.apply {
                        this.id = it.id
                    }!!
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getPharmacy(id: String): Pharmacy {
        return try {
            databaseRef.document(id).get().await().toObject(Pharmacy::class.java)?.apply {
                this.id = id
            }!!
        } catch (e: Exception) {
            Pharmacy()
        }
    }

    override suspend fun addPharmacy(pharmacy: Pharmacy, uri: Uri) {
        try {
            val photoRef = storageRef.child(uri.lastPathSegment!!)
            photoRef.putFile(uri).continueWithTask {
                photoRef.downloadUrl
            }.addOnCompleteListener { task ->
                databaseRef.add(pharmacy.copy(img = task.result.toString()))
            }
        } catch (e: Exception) {
            // Handle exception
        }
    }
}