package com.example.tskg.mobile.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tskg.R
import com.example.tskg.common.models.Movie
import com.example.tskg.common.models.MoviesList
import com.example.tskg.common.utils.Common
import com.example.tskg.mobile.adapters.MovieListAdapter

class MoviesListFragment: Fragment(R.layout.fragment_mobile_movies_list) {
    lateinit var mobileMoviesList: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mobileMoviesList = view.findViewById(R.id.mobile_movies_list)
    }

    fun setMovies(movies: MoviesList) {
       drawMoviesData(movies.movies)
    }

    private fun drawMoviesData(movies: List<Movie>) {
        val heightPerItem = Common.dpToPx(requireContext(), 260)
        val spacing = Common.dpToPx(requireContext(), 10)

        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        mobileMoviesList.addItemDecoration(Common.HorizontalSpacingItemDecoration(spacing))

        mobileMoviesList.layoutManager = linearLayoutManager

        val adapter = MovieListAdapter(movies, heightPerItem)
        mobileMoviesList.adapter = adapter
    }
}