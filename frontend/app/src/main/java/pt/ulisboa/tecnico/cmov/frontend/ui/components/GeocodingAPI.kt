package pt.ulisboa.tecnico.cmov.frontend.ui.components

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import pt.ulisboa.tecnico.cmov.frontend.ui.main_screen.MainScreenViewModel

data class PlaceResponse(val candidates: List<PlaceResult>, val status: String)
data class PlaceResult(val location: Location)


suspend fun getLatLngFromPlace(
    viewModel: MainScreenViewModel,
    query:String,
    apiKey: String
) {
    val client = OkHttpClient()
    val url = "https://maps.googleapis.com/maps/api/geocode/json?address=${query}&key=${apiKey}"
    val request = Request.Builder().url(url).build()

    return withContext(Dispatchers.IO) {
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            response.body?.string()?.let { responseBody ->
                val json = JSONObject(responseBody)
                val location = json.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location")
                val lat = location.getDouble("lat")
                val lng = location.getDouble("lng")
                viewModel.updateJustSearchedLocation(true)
                viewModel.updateLocation(getLocationFromLatLng(LatLng(lat, lng)))
            }
        }
    }
}