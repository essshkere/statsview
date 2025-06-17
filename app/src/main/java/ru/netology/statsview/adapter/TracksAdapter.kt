package ru.netology.statsview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.statsview.R
import ru.netology.statsview.data.Track
import ru.netology.statsview.databinding.TrackItemBinding

class TracksAdapter(
    private val onTrackClick: (Track) -> Unit
) : RecyclerView.Adapter<TracksAdapter.TrackViewHolder>() {

    var tracks = emptyList<Track>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int = tracks.size

    inner class TrackViewHolder(
        private val binding: TrackItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            binding.apply {
                trackNumber.text = track.id.toString()
                trackTitle.text = "Трек ${track.id} (${track.file})"

                playPauseButton.setImageResource(
                    if (track.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                )

                playPauseButton.setOnClickListener {
                    onTrackClick(track)
                }

                root.setOnClickListener {
                    onTrackClick(track)
                }
            }
        }
    }
}