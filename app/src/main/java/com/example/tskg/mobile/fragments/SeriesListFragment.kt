package com.example.tskg.mobile.fragments

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tskg.R
import com.example.tskg.common.adapters.EpisodeMobileAdapter
import com.example.tskg.common.models.MovieSeason

class SeriesListFragment : Fragment(R.layout.fragment_mobile_series) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var episodeMobileAdapter: EpisodeMobileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mobile_series , container, false)
        recyclerView = view.findViewById(R.id.episodes_list)

        val season = arguments?.getParcelable<MovieSeason>("season")

        season?.let {
            bindMovies(season)
        }

        return view
    }
    private fun bindMovies(season: MovieSeason) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        episodeMobileAdapter = EpisodeMobileAdapter(season.episodes)
        recyclerView.adapter = episodeMobileAdapter
    }

    companion object {
        fun newInstance(season: MovieSeason): SeriesListFragment  {
            val fragment = SeriesListFragment()
            fragment.arguments = Bundle().apply {
                putParcelable("season", season)
            }
            return fragment
        }
    }
}