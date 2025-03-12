package com.example.tskg.mobile.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tskg.MyApplication
import com.example.tskg.R
import com.example.tskg.common.models.MoviesList
import com.example.tskg.common.parser.DataParser
import kotlinx.coroutines.launch

class HomeFragment: Fragment(R.layout.fragment_mobile_home){
    private val moviesListFragment: MoviesListFragment = MoviesListFragment()
    private val dataParser: DataParser = DataParser()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataProcess(view)
    }

    private fun setFragments() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.mobile_movies_list, moviesListFragment)

        transaction.commit()
    }

    private fun getDataProcess(view: View) {
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val errorText =  view.findViewById<TextView>(R.id.errorText)

        lifecycleScope.launch {
            try{
                progressBar.visibility = ProgressBar.VISIBLE
                val movies = getData()
                moviesListFragment.setMovies(movies[0])
            } catch(error: Exception) {
                errorText.visibility = TextView.VISIBLE
                errorText.text = error.message
            } finally {
                progressBar.visibility = ProgressBar.GONE
            }
        }
    }

    private suspend fun getData(): List<MoviesList> {
        try {
            val request = (requireActivity().application as MyApplication).htmlRequest
            val BASE_URL = (requireActivity().application as MyApplication).BASE_URL

            val result = request.getHtmlPage("/")

            val body = result.body()

            if (!result.isSuccessful || body == null) {
                throw IllegalStateException("Failed to fetch data from the server")
            }

            val movies = dataParser.parseHomePage(body.toString(), BASE_URL)

            return movies
        } catch (error: Exception) {
            throw error
        }
    }
}