package ru.netology.statsview.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.netology.statsview.R
import ru.netology.statsview.adapter.MarkersAdapter
import ru.netology.statsview.dto.MapMarker
import ru.netology.statsview.repository.MarkerRepository

class MarkersListFragment : Fragment() {
    private lateinit var repository: MarkerRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_markers_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = MarkerRepository(requireContext())
        val recyclerView = view.findViewById<RecyclerView>(R.id.markersRecyclerView)
        val adapter = MarkersAdapter { marker ->
            (parentFragment as? OnMarkerSelectedListener)?.onMarkerSelected(marker)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        adapter.submitList(repository.getMarkers())

        view.findViewById<FloatingActionButton>(R.id.fabMap).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    interface OnMarkerSelectedListener {
        fun onMarkerSelected(marker: MapMarker)
    }
}