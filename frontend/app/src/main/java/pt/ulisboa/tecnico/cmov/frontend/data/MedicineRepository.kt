package pt.ulisboa.tecnico.cmov.frontend.data

import android.net.Uri
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import pt.ulisboa.tecnico.cmov.frontend.model.Medicine
import pt.ulisboa.tecnico.cmov.frontend.model.MedicinePharmacy
import pt.ulisboa.tecnico.cmov.frontend.model.Pharmacy

const val MEDICINES_REFERENCE_PATH = "medicines"
const val MEDICINE_PHARMACIES_REFERENCE_PATH = "medicines"
const val MEDICINE_IMAGES_REFERENCE_PATH = "medicines"

interface MedicineRepository {
    suspend fun getMedicines(ids: List<String>? = null): List<Medicine>
    suspend fun searchMedicines(query: String): List<Medicine>
    suspend fun getMedicine(id: String): Medicine
    suspend fun getMedicinePharmacies(id: String): List<MedicinePharmacy>
    suspend fun addMedicine(medicine: Medicine, uri: Uri)
    suspend fun addMedicineToPharmacy(medicineId: String, pharmacyId: String)
}

class FirebaseMedicineRepository(
    database: FirebaseFirestore,
    storage: FirebaseStorage
) : MedicineRepository {

    private val medicinesRef = database.collection(MEDICINES_REFERENCE_PATH)
    private val storageRef = storage.getReference(MEDICINE_IMAGES_REFERENCE_PATH)

    private fun medicinePharmaciesRef(id: String): CollectionReference {
        return medicinesRef.document(id).collection(MEDICINE_PHARMACIES_REFERENCE_PATH)
    }

    override suspend fun getMedicines(ids: List<String>?): List<Medicine> {
        return try {
            val query = if (ids != null) {
                medicinesRef.whereIn(FieldPath.documentId(), ids)
            } else {
                medicinesRef
            }

            query.get()
                .await()
                .documents
                .map {
                    it.toObject(Medicine::class.java)?.apply {
                        this.id = it.id
                    }!!
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun searchMedicines(query: String): List<Medicine> {
        return medicinesRef
            .whereGreaterThanOrEqualTo("name", query)
            .whereLessThan("name", query + "\uf8ff")
            .get()
            .await()
            .documents
            .map {
                it.toObject(Medicine::class.java)?.apply {
                    this.id = it.id
                }!!
            }
    }

    override suspend fun getMedicine(id: String): Medicine {
        return try {
            medicinesRef.document(id).get().await().toObject(Medicine::class.java)?.apply {
                this.id = id
            }!!
        } catch (e: Exception) {
            Medicine()
        }
    }

    override suspend fun getMedicinePharmacies(id: String): List<MedicinePharmacy> {
        return try {
            medicinePharmaciesRef(id).get()
                .await()
                .documents
                .map {
                    it.toObject(MedicinePharmacy::class.java)?.apply {
                        this.pharmacy = Pharmacy(id = it.id)
                    }!!
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addMedicine(medicine: Medicine, uri: Uri) {
        try {
            val photoRef = storageRef.child(uri.lastPathSegment!!)
            photoRef.putFile(uri).continueWithTask {
                photoRef.downloadUrl
            }.addOnCompleteListener { task ->
                medicinesRef.document(medicine.id).set(medicine.copy(img = task.result.toString()))
            }
        } catch (e: Exception) {
            // Handle exception
        }
    }

    override suspend fun addMedicineToPharmacy(medicineId: String, pharmacyId: String) {
        try {
            medicinePharmaciesRef(medicineId)
                .document(pharmacyId).set(hashMapOf("quantity" to 1))
        } catch (e: Exception) {
            // Handle exception
        }
    }
}
