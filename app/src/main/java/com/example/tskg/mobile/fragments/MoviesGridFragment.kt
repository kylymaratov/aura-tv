package com.example.tskg.mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tskg.MyApplication
import com.example.tskg.R
import com.example.tskg.common.adapters.MovieMobileGridAdapter
import com.example.tskg.common.models.Movie
import com.example.tskg.common.models.MoviesList
import com.example.tskg.common.utils.Common
import kotlinx.coroutines.launch

class MoviesGridFragment: Fragment(R.layout.fragment_mobile_movies_list) {
    private lateinit var mobileMoviesGrid: RecyclerView
    private var categoryId: Int = 12
    private var pageId: Int = 1
    private lateinit var movies: MutableList<Movie>
    private var isEndOfListReached = false
    private var stopLoadMore: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mobile_movies_list, container, false)
        mobileMoviesGrid = view.findViewById(R.id.mobile_movies_list)
        val incomingMovies = arguments?.getParcelable<MoviesList>("movies")
        categoryId = arguments?.getInt("categoryId") ?: 0
        pageId = arguments?.getInt("pageId") ?: 0

        incomingMovies?.let {
            movies = incomingMovies.movies
            bindMovies()
            recycleEvents()
        }
        return view
    }

     private fun bindMovies() {
        val widthPerItem =  Common.dpToPx(requireContext(), 130)
        val heightPerItem = Common.dpToPx(requireContext(), 220)
        val screenWidth = resources.displayMetrics.widthPixels

        val itemsPerRow = screenWidth / widthPerItem

        val spacing = Common.dpToPx(requireContext(), 0)
        val gridLayoutManager = GridLayoutManager(requireContext(), itemsPerRow)

        mobileMoviesGrid.addItemDecoration(Common.GridSpacingItemDecoration(itemsPerRow, spacing, true))

        mobileMoviesGrid.layoutManager = gridLayoutManager

        val adapter = MovieMobileGridAdapter(movies, heightPerItem)
        mobileMoviesGrid.adapter = adapter
    }

    private fun recycleEvents() {
        if (pageId != 0) {
            mobileMoviesGrid.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return

                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (!isEndOfListReached && lastVisibleItemPosition == totalItemCount - 1) {
                        isEndOfListReached = true
                        loadMoreMovies()
                    }
                }
            })
        }
    }

    private fun loadMoreMovies() {
        lifecycleScope.launch {
            try {
                if (!stopLoadMore) {
                    pageId = pageId + 1
                    val data = (requireActivity().application as MyApplication).getCategoryData(categoryId, pageId);
                    val startPosition = movies.size
                    data.movies.forEach { movie ->
                        if (movies.none { it.movieId == movie.movieId }) {
                            movies.add(movie)
                        }
                    }
                    mobileMoviesGrid.adapter?.notifyItemRangeInserted(startPosition, data.movies.size)
                }

            } catch (error: Exception) {
                if (pageId >= 5) {
                    stopLoadMore = true
                }
            } finally {
                isEndOfListReached = false
            }
        }
    }

    companion object {
        fun newInstance(movies: MoviesList, categoryId: Int = 0, pageId: Int = 0): MoviesGridFragment {
            val fragment = MoviesGridFragment()
            fragment.arguments = Bundle().apply {
                putParcelable("movies", movies)
                putInt("categoryId", categoryId)
                putInt("pageId", pageId)
            }
            return fragment
        }
    }
}