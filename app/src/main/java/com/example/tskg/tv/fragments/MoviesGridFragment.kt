package com.example.tskg.tv.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tskg.MyApplication
import com.example.tskg.R
import com.example.tskg.common.models.Movie
import com.example.tskg.tv.adapters.MovieGridAdapter
import com.example.tskg.common.utils.Common
import kotlinx.coroutines.launch

class MoviesGridFragment : Fragment(R.layout.fragment_movies_grid) {
    private lateinit var moviesGrid: RecyclerView
    private var categoryId: Int = 12
    private var pageId: Int = 1
    private lateinit var movies: MutableList<Movie>
    private var isEndOfListReached = false
    private var stopLoadMore: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviesGrid = view.findViewById(R.id.movies_grid)
        recycleEvents()
    }

    fun bindMoviesData(data: MutableList<Movie>, categoryId: Int, pageId: Int) {
        this.categoryId = categoryId
        this.pageId = pageId
        this.movies = data

        val adapter = moviesGrid.adapter
        if (adapter is MovieGridAdapter) {
            adapter.updateMovies(data)
        } else {
            drawMoviesData()
        }
    }

    fun clearMoviesData() {
        val adapter = moviesGrid.adapter
        if (adapter is MovieGridAdapter) {
            adapter.clearMovies()
        }
    }

    private fun drawMoviesData() {
        val widthPerItem =  Common.dpToPx(requireContext(), 165)
        val heightPerItem = Common.dpToPx(requireContext(), 260)
        val screenWidth = resources.displayMetrics.widthPixels

        val itemsPerRow = screenWidth / widthPerItem

        val spacing = Common.dpToPx(requireContext(), 10)
        val gridLayoutManager = GridLayoutManager(requireContext(), itemsPerRow)

        moviesGrid.addItemDecoration(Common.GridSpacingItemDecoration(itemsPerRow, spacing, true))

        moviesGrid.layoutManager = gridLayoutManager

        val adapter = MovieGridAdapter(movies, heightPerItem)
        moviesGrid.adapter = adapter
    }

    private fun recycleEvents() {
        if (pageId != 0) {
            moviesGrid.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return

                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (!isEndOfListReached && lastVisibleItemPosition == totalItemCount - 1) {
                        isEndOfListReached = true
                        loadMoreMovies( )
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
                   moviesGrid.adapter?.notifyItemRangeInserted(startPosition, data.movies.size)
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
}

