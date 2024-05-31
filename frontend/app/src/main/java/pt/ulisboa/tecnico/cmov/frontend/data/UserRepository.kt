package pt.ulisboa.tecnico.cmov.frontend.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

const val USER_REFERENCE_PATH = "users"
const val USER_FAVORITES_REFERENCE_PATH = "favorites"

interface UserRepository {
    suspend fun getFavorites(): List<String>
    suspend fun isFavorite(pharmacyId: String): Boolean
    suspend fun addFavorite(pharmacyId: String)
    suspend fun removeFavorite(pharmacyId: String)
}

class FirebaseUserRepository(
    database: FirebaseFirestore
) : UserRepository {
    private val userRef = database.collection(USER_REFERENCE_PATH)
    private fun favoriteRef(): CollectionReference {
        return userRef.document("debug").collection(USER_FAVORITES_REFERENCE_PATH)
    }

    override suspend fun getFavorites(): List<String> {
        return try {
            favoriteRef().get()
                .await()
                .documents
                .map {
                    it.id
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun isFavorite(pharmacyId: String): Boolean {
        return try {
            favoriteRef().document(pharmacyId)
                .get()
                .await()
                .exists()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addFavorite(pharmacyId: String) {
        try {
            favoriteRef().document(pharmacyId)
                .set(mapOf("favorite" to true))
                .await()
        } catch (e: Exception) {

        }

    }

    override suspend fun removeFavorite(pharmacyId: String) {
        favoriteRef().document(pharmacyId)
            .delete()
    }
}