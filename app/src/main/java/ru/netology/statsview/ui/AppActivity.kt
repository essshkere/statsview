package ru.netology.statsview.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ru.netology.statsview.R
import ru.netology.statsview.adapter.TracksAdapter
import ru.netology.statsview.api.AlbumService
import ru.netology.statsview.data.Album
import ru.netology.statsview.data.Track
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.netology.statsview.databinding.ActivityMainBinding


class AppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TracksAdapter
    private val mediaObserver = MediaLifecycleObserver()
    private var album: Album? = null
    private var currentTrackIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(mediaObserver)

        setupRecyclerView()
        loadAlbum()
        setupControls()
    }

    private fun setupRecyclerView() {
        adapter = TracksAdapter { track ->
            playTrack(track)
        }
        binding.tracksList.adapter = adapter
    }

    private fun loadAlbum() {
        lifecycleScope.launchWhenCreated {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(AlbumService::class.java)
                album = service.getAlbum()
                updateUI()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateUI() {
        album?.let { album ->
            binding.albumTitle.text = album.title
            binding.albumArtist.text = album.artist
            adapter.tracks = album.tracks
        }
    }

    private fun setupControls() {
        binding.playPauseButton.setOnClickListener {
            album?.let {
                if (mediaObserver.player?.isPlaying == true) {
                    pause()
                } else {
                    playTrack(it.tracks[currentTrackIndex])
                }
            }
        }

        binding.nextButton.setOnClickListener {
            nextTrack()
        }

        binding.prevButton.setOnClickListener {
            prevTrack()
        }
    }

    private fun playTrack(track: Track) {
        album?.let { album ->
            currentTrackIndex = album.tracks.indexOf(track)
            updateTracksPlayingState()

            val url = "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/${track.file}"
            mediaObserver.apply {
                player?.reset()
                player?.setDataSource(url)
                play()

                player?.setOnCompletionListener {
                    nextTrack()
                }
            }

            binding.playPauseButton.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun pause() {
        mediaObserver.player?.pause()
        binding.playPauseButton.setImageResource(R.drawable.ic_play)
    }

    private fun nextTrack() {
        album?.let {
            currentTrackIndex = (currentTrackIndex + 1) % it.tracks.size
            playTrack(it.tracks[currentTrackIndex])
        }
    }

    private fun prevTrack() {
        album?.let {
            currentTrackIndex = (currentTrackIndex - 1 + it.tracks.size) % it.tracks.size
            playTrack(it.tracks[currentTrackIndex])
        }
    }

    private fun updateTracksPlayingState() {
        album?.tracks?.forEachIndexed { index, track ->
            track.isPlaying = index == currentTrackIndex
        }
        adapter.notifyDataSetChanged()
    }
}