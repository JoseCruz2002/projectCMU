package pt.ulisboa.tecnico.cmov.frontend.data

import android.net.Uri
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import pt.ulisboa.tecnico.cmov.frontend.model.Medicine
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy
import pt.ulisboa.tecnico.cmov.frontend.model.PharmacyMedicine

const val PHARMACY_REFERENCE_PATH = "pharmacies"
const val PHARMACY_MEDICINES_REFERENCE_PATH = "medicines"
const val IMAGES_REFERENCE_PATH = "pharmacies"

interface PharmacyRepository {
    suspend fun getPharmacies(ids: List<String>? = null): List<Pharmacy>
    suspend fun getPharmacy(id: String): Pharmacy
    suspend fun getPharmacyMedicines(id: String): List<PharmacyMedicine>
    suspend fun addPharmacy(pharmacy: Pharmacy, uri: Uri)
    suspend fun addMedicineToPharmacy(medicineId: String, pharmacyId: String)
}

class FirebasePharmacyRepository(
    database: FirebaseFirestore,
    storage: FirebaseStorage
) : PharmacyRepository {
    private val databaseRef = database.collection(PHARMACY_REFERENCE_PATH)
    private val storageRef = storage.getReference(IMAGES_REFERENCE_PATH)

    override suspend fun getPharmacies(ids: List<String>?): List<Pharmacy> {
        return try {
            val query = if (ids != null) {
                databaseRef.whereIn(FieldPath.documentId(), ids)
            } else {
                databaseRef
            }

            query.get()
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

    override suspend fun getPharmacyMedicines(id: String): List<PharmacyMedicine> {
        return try {
            databaseRef.document(id).collection(PHARMACY_MEDICINES_REFERENCE_PATH)
                .get()
                .await()
                .documents
                .map {
                    it.toObject(PharmacyMedicine::class.java)?.apply {
                        this.medicine = Medicine(id = it.id)
                    }!!
                }
        } catch (e: Exception) {
            emptyList()
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

    override suspend fun addMedicineToPharmacy(medicineId: String, pharmacyId: String) {
        try {
            databaseRef
                .document(pharmacyId).collection(PHARMACY_MEDICINES_REFERENCE_PATH)
                .document(medicineId).set(hashMapOf("quantity" to 1))
        } catch (e: Exception) {
            // Handle exception
        }
    }
}