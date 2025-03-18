package com.example.tskg.mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tskg.R
import com.example.tskg.common.adapters.MovieMobileGridAdapter
import com.example.tskg.common.models.MoviesList
import com.example.tskg.common.utils.Common

class MoviesGridFragment: Fragment(R.layout.fragment_mobile_movies_list) {
    private lateinit var mobileMoviesGrid: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mobile_movies_list, container, false)
        mobileMoviesGrid = view.findViewById(R.id.mobile_movies_list)
        val movies = arguments?.getParcelable<MoviesList>("movies")

        movies?.let {
            bindMovies(movies)
        }
        return view
    }

    fun setMovies(movies: MoviesList) {
        bindMovies(movies)
    }

    fun clearMovies() {
        val adapter = mobileMoviesGrid.adapter

        if (adapter is MovieMobileGridAdapter) {
            adapter.clearMovies()
        }
    }

    private fun bindMovies(movies: MoviesList) {
        val widthPerItem =  Common.dpToPx(requireContext(), 130)
        val heightPerItem = Common.dpToPx(requireContext(), 220)
        val screenWidth = resources.displayMetrics.widthPixels

        val itemsPerRow = screenWidth / widthPerItem

        val spacing = Common.dpToPx(requireContext(), 0)
        val gridLayoutManager = GridLayoutManager(requireContext(), itemsPerRow)

        mobileMoviesGrid.addItemDecoration(Common.GridSpacingItemDecoration(itemsPerRow, spacing, true))

        mobileMoviesGrid.layoutManager = gridLayoutManager

        val adapter = MovieMobileGridAdapter(movies.movies, heightPerItem)
        mobileMoviesGrid.adapter = adapter
    }


    companion object {
        fun newInstance(movies: MoviesList): MoviesGridFragment {
            val fragment = MoviesGridFragment()
            fragment.arguments = Bundle().apply {
                putParcelable("movies", movies)
            }
            return fragment
        }
    }
}