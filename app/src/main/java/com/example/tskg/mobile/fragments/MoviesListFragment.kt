package com.example.tskg.mobile.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tskg.R
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
        val widthPerItem =  Common.dpToPx(requireContext(), 170)
        val heightPerItem = Common.dpToPx(requireContext(), 270)
        val spacing = Common.dpToPx(requireContext(), 5)

        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        mobileMoviesList.addItemDecoration(Common.HorizontalSpacingItemDecoration(spacing))

        mobileMoviesList.layoutManager = linearLayoutManager

        val adapter = MovieListAdapter(movies.movies, heightPerItem, widthPerItem)
        mobileMoviesList.adapter = adapter
    }
}