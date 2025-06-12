package ru.netology.statsview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.statsview.R
import ru.netology.statsview.dto.MapMarker

class MarkersAdapter(
    private val onMarkerClick: (MapMarker) -> Unit
) : ListAdapter<MapMarker, MarkersAdapter.MarkerViewHolder>(MarkerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_marker, parent, false)
        return MarkerViewHolder(view, onMarkerClick)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MarkerViewHolder(
        view: View,
        private val onMarkerClick: (MapMarker) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        private val coordinatesTextView: TextView = view.findViewById(R.id.coordinatesTextView)

        fun bind(marker: MapMarker) {
            titleTextView.text = marker.title
            descriptionTextView.text = marker.description
            coordinatesTextView.text = "Широта: ${marker.latitude}, Долгота: ${marker.longitude}"

            itemView.setOnClickListener {
                onMarkerClick(marker)
            }
        }
    }

    private class MarkerDiffCallback : DiffUtil.ItemCallback<MapMarker>() {
        override fun areItemsTheSame(oldItem: MapMarker, newItem: MapMarker): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MapMarker, newItem: MapMarker): Boolean {
            return oldItem == newItem
        }
    }
}