package com.example.tskg.tv.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.tskg.MyApplication
import com.example.tskg.R
import com.example.tskg.common.models.Movie
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    lateinit var progressBar: ProgressBar
    lateinit var errorText: TextView
    lateinit var searchEditText: EditText

    lateinit var moviesGridFragment: MoviesGridFragment
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var savedSearchQuery: String? = null

    private lateinit var speechRecognitionLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        speechRecognitionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val spokenText = results?.get(0) ?: ""
                performSearch(spokenText)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moviesGridFragment = MoviesGridFragment()
        setFragments()
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorText = view.findViewById(R.id.error_message)
        progressBar  = view.findViewById(R.id.progress_bar)

        searchEditText = view.findViewById(R.id.search_edit_text)


        searchEditText.setText(savedSearchQuery)

        searchEditText.requestFocus()


        searchEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                performSearch(searchEditText.text.toString())
                true
            } else {
                false
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                runnable?.let { handler.removeCallbacks(it) }

                runnable = Runnable {
                    performSearch(s.toString())
                }

                handler.postDelayed(runnable!!, 1000)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun performSearch(query: String) {
        if (query.length > 2) {
            lifecycleScope.launch {
                try {
                    progressBar.visibility = View.VISIBLE
                    errorText.text = ""
                    errorText.visibility = View.GONE
                    val jsonRequest = (requireActivity().application as MyApplication).jsonRequest
                    val baseUrl = (requireActivity().application as MyApplication).BASE_URL

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
                            setSearchResult(movies)
                        }else {
                            throw IllegalStateException("Не удалось ничего найти по запросу: $query")
                        }

                    }
                } catch (error: Exception) {
                    moviesGridFragment.clearMoviesData()
                    errorText.text = error.message
                    errorText.visibility = View.VISIBLE
                }finally {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun setFragments() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.movies_list_fragment, moviesGridFragment)
        transaction.commit()
    }

    private fun setSearchResult(movies: MutableList<Movie>) {
        moviesGridFragment.bindMoviesData(movies, 0, 0)
    }

    override fun onPause() {
        super.onPause()

        savedSearchQuery = searchEditText.text.toString()
    }
}