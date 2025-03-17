package com.example.tskg.common.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tskg.R
import com.example.tskg.common.models.MovieEpisode
import com.example.tskg.tv.VideoPlayerActivity

class EpisodeMobileAdapter(private val episodes: List<MovieEpisode>) : RecyclerView.Adapter<EpisodeMobileAdapter.EpisodeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mobile_episode, parent, false)
        return EpisodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = episodes[position]
        holder.bind(episode, episode.episodeId, episodes)
    }

    override fun getItemCount(): Int = episodes.size

    class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.episode_title)
        private val qualityTextView: TextView = itemView.findViewById(R.id.episode_quality)
        private val durationTextView: TextView = itemView.findViewById(R.id.episode_duration)

        fun bind(episode: MovieEpisode, currentIndex: Int, episodes: List<MovieEpisode>) {
            itemView.setOnClickListener {
                val episodeUrls = episodes.map { it.episodeSourceId }.toCollection(ArrayList())
                Log.d("episode", currentIndex.toString())
                val intent = Intent(itemView.context, VideoPlayerActivity::class.java).apply {
                    putStringArrayListExtra("EPISODE_SOURCE_LIST", episodeUrls)
                    putExtra("CURRENT_EPISODE", currentIndex - 1)
                }

                itemView.context.startActivity(intent)
            }

            titleTextView.text = "Серия: ${episode.episodeTitle}"
            qualityTextView.text = "Качество: ${episode.quality}"
            durationTextView.text = "Длительность: ${episode.duration}"
        }
    }
}