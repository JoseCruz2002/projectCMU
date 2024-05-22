package pt.ulisboa.tecnico.cmov.frontend.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pt.ulisboa.tecnico.cmov.frontend.model.Medicine

const val MEDICINES_REFERENCE_PATH = "medicines"

interface MedicineRepository {
    suspend fun getMedicines(): List<Medicine>
    suspend fun addMedicine(medicine: Medicine)
}

class FirebaseMedicineRepository(
    database: FirebaseFirestore,
) : MedicineRepository {

    private val databaseRef = database.collection(MEDICINES_REFERENCE_PATH)
    override suspend fun getMedicines(): List<Medicine> {
        return try {
            databaseRef
                .get()
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

    override suspend fun addMedicine(medicine: Medicine) {
        try {
            databaseRef.document(medicine.id).set(medicine)
        } catch (e: Exception) {
            // Handle exception
        }
    }
}