package ru.netology.statsview.repository
import com.google.gson.Gson
import android.content.Context
import ru.netology.statsview.dto.MapMarker

class MarkerRepository(private val context: Context) {
    private val sharedPrefs = context.getSharedPreferences("map_markers", Context.MODE_PRIVATE)

    fun saveMarker(marker: MapMarker) {
        val markers = getMarkers().toMutableList()
        markers.add(marker)
        val json = Gson().toJson(markers)
        sharedPrefs.edit().putString("markers", json).apply()
    }

    fun getMarkers(): List<MapMarker> {
        val json = sharedPrefs.getString("markers", "[]") ?: "[]"
        return Gson().fromJson(json, Array<MapMarker>::class.java).toList()
    }

    fun deleteMarker(marker: MapMarker) {
        val markers = getMarkers().toMutableList()
        markers.removeAll { it.id == marker.id }
        val json = Gson().toJson(markers)
        sharedPrefs.edit().putString("markers", json).apply()
    }
}