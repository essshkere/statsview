package ru.netology.statsview.ui

import android.view.View
import ru.netology.statsview.R
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.model.cameraPosition
import com.google.maps.android.ktx.utils.collection.addMarker
import ru.netology.statsview.dto.MapMarker
import ru.netology.statsview.repository.MarkerRepository
import ru.netology.statsview.ui.extensions.icon

class MapsFragment : Fragment() {
    private lateinit var googleMap: GoogleMap
    private lateinit var repository: MarkerRepository
    private val markersCollection = mutableListOf<Marker>()


    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                googleMap.apply {
                    isMyLocationEnabled = true
                    uiSettings.isMyLocationButtonEnabled = true
                }
            } else {
                // TODO: show sorry dialog
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = MarkerRepository(requireContext())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        view.findViewById<FloatingActionButton>(R.id.fabList).setOnClickListener {
            (requireActivity() as AppActivity).showMarkersList()
        }

        lifecycle.coroutineScope.launchWhenCreated {
            googleMap = mapFragment.awaitMap().apply {
                isTrafficEnabled = true
                isBuildingsEnabled = true

                uiSettings.apply {
                    isZoomControlsEnabled = true
                    setAllGesturesEnabled(true)
                }
                setOnMapClickListener { latLng ->
                    showAddMarkerDialog(latLng)
                }
            }


            loadMarkers()
            when {
                             ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    googleMap.apply {
                        isMyLocationEnabled = true
                        uiSettings.isMyLocationButtonEnabled = true
                    }

                    val fusedLocationProviderClient = LocationServices
                        .getFusedLocationProviderClient(requireActivity())

                    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        println(it)
                    }
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    // TODO: show rationale dialog
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }

            val target = LatLng(55.751999, 37.617734)
            val markerManager = MarkerManager(googleMap)
            val collection: MarkerManager.Collection = markerManager.newCollection().apply {
                addMarker {
                    position(target)
                    icon(getDrawable(requireContext(), R.drawable.ic_netology_48dp)!!)
                    title("The Moscow Kremlin")
                }.apply {
                    tag = "Any additional data" // Any
                }
            }
            collection.setOnMarkerClickListener { marker ->
                // TODO: work with marker
                Toast.makeText(
                    requireContext(),
                    (marker.tag as String),
                    Toast.LENGTH_LONG
                ).show()
                true
            }

            googleMap.awaitAnimateCamera(
                CameraUpdateFactory.newCameraPosition(
                    cameraPosition {
                        target(target)
                        zoom(15F)
                    }
                ))
        }
    }
    private fun showAddMarkerDialog(latLng: LatLng) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_marker, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.titleEditText)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.descriptionEditText)

        AlertDialog.Builder(requireContext())
            .setTitle("Добавить маркер")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val marker = MapMarker(
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    title = titleEditText.text.toString(),
                    description = descriptionEditText.text.toString()
                )

                repository.saveMarker(marker)
                addMarkerToMap(marker)
            }
            .setNegativeButton("Отмена", null)
            .create()
            .show()
    }

    private fun loadMarkers() {
        repository.getMarkers().forEach { marker ->
            addMarkerToMap(marker)
        }
    }

    private fun addMarkerToMap(mapMarker: MapMarker) {
        val marker = googleMap.addMarker {
            position(LatLng(mapMarker.latitude, mapMarker.longitude))
            title(mapMarker.title)
            snippet(mapMarker.description)
            icon(getDrawable(requireContext(), R.drawable.ic_netology_48dp)!!)
        }
        marker?.tag = mapMarker.id
        marker?.let { markersCollection.add(it) }
    }
}