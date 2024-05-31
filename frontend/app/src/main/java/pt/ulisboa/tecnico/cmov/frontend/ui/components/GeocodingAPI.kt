package pt.ulisboa.tecnico.cmov.frontend.ui.components

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class PlaceResponse(val candidates: List<PlaceResult>, val status: String)
data class PlaceResult(val location: Location)


suspend fun getLatLngFromPlace(
    query:String,
    apiKey: String
): LatLng? {
    val client = OkHttpClient()
    val url = "https://maps.googleapis.com/maps/api/geocode/json?address=${query.lowercase()}&key=${apiKey}"
    val request = Request.Builder().url(url).build()

    return withContext(Dispatchers.IO) {
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            response.body?.string()?.let { responseBody ->
                val json = JSONObject(responseBody)
                if (json.getJSONArray("results").length() > 0) {
                    val location = json.getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location")
                    val lat = location.getDouble("lat")
                    val lng = location.getDouble("lng")
                    LatLng(lat, lng)
                } else {
                    LatLng(38.736946,-9.142685)//If search fails it send to Lisbon
                }
            }
        } else {
            null
        }
    }
}

suspend fun getAddressFromCoordinates(latitude: Double, longitude: Double, apiKey: String): String? {
    val client = OkHttpClient()
    val url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=$latitude,$longitude&key=$apiKey"

    return withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        if (responseBody != null) {
            val json = JSONObject(responseBody)
            val results = json.getJSONArray("results")
            if (results.length() > 0) {
                val address = results.getJSONObject(0).getString("formatted_address")
                return@withContext address
            }
        }
        return@withContext null
    }
}