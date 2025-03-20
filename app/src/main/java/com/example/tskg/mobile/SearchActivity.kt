package com.example.tskg.mobile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.tskg.MyApplication
import com.example.tskg.R
import com.example.tskg.common.models.Movie
import com.example.tskg.common.models.MoviesList
import com.example.tskg.mobile.fragments.MoviesGridFragment
import kotlinx.coroutines.launch

class SearchActivity: FragmentActivity(R.layout.activity_mobile_search) {
    private lateinit var searchView: SearchView
    private lateinit var errorText: TextView
    private  lateinit var progressBar: ProgressBar
    private  lateinit var searchBg: ImageView

    private var runnable: Runnable? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchView = findViewById(R.id.search_view)
        progressBar = findViewById(R.id.progress_bar)
        errorText = findViewById(R.id.error_text)
        searchBg = findViewById(R.id.search_bg)

        searchView.requestFocus()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    performSearch(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    runnable?.let { handler.removeCallbacks(it) }

                    runnable = Runnable {
                        performSearch(newText)
                    }
                    handler.postDelayed(runnable!!, 1000)
                }
                return false
            }
        })
    }


    private fun performSearch(query: String) {
        if (query.length > 2) {
            lifecycleScope.launch {
                try {
                    progressBar.visibility = View.VISIBLE
                    errorText.text = ""
                    errorText.visibility = View.GONE
                    val jsonRequest = (application as MyApplication).jsonRequest
                    val baseUrl = (application as MyApplication).BASE_URL

                    val response = jsonRequest.search("/shows/search/$query")

                    if (response.isSuccessful()) {
                        val body = response.body()
                        val movies: MutableList<Movie> = mutableListOf()

                        if (body != null && body.isNotEmpty()) {
                            for (item in body) {
                                movies.add(
                                    Movie(
                                        movieId = item.url,
                                        title = item.name,
                                        year = "",
                                        posterUrl = "$baseUrl${item.url.replace("/show/", "/posters/")}.png",
                                        details = null,
                                        genre = "",
                                        country = ""
                                    )
                                )
                            }
                            val result = MoviesList(title = "", movies = movies)

                            val moviesGridInstance = MoviesGridFragment.newInstance(result)

                            val transaction = supportFragmentManager.beginTransaction()
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                            transaction.add(R.id.search_result, moviesGridInstance)
                            transaction.commit()
                        }else {
                            throw IllegalStateException("Не удалось ничего найти по запросу: $query")
                        }
                    }
                } catch (error: Exception) {
                    errorText.text = error.message
                    errorText.visibility = View.VISIBLE
                }finally {
                    searchBg.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}