package com.example.tskg.mobile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tskg.MyApplication
import com.example.tskg.R
import com.example.tskg.tv.fragments.HeaderFragment
import com.example.tskg.tv.fragments.SeriesListFragment
import com.example.tskg.common.models.Movie
import com.example.tskg.common.utils.Common
import kotlinx.coroutines.launch

class DetailsActivity : FragmentActivity(R.layout.activity_mobile_details) {
    lateinit var movieTitle: TextView
    lateinit var movieGenre: TextView
    lateinit var movieDescription: TextView
    lateinit var movieAdditionalInfo: TextView
    lateinit var movieCover: ImageView
    lateinit var gradientBanner: View
    lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val errText = findViewById<TextView>(R.id.error_text)
        val headerLayout: ConstraintLayout = view.findViewById(R.id.layout_header)
        val infoLayout: ConstraintLayout = headerLayout.findViewById(R.id.layout_info)

        movieCover = headerLayout.findViewById(R.id.movie_cover)
        movieTitle = infoLayout.findViewById(R.id.movie_title)
        movieGenre = infoLayout.findViewById(R.id.movie_genre)
        movieDescription = infoLayout.findViewById(R.id.movie_description)
        movieAdditionalInfo = infoLayout.findViewById(R.id.movie_additional_info)
        gradientBanner = view.findViewById(R.id.gradient_horizontal)
        backButton = view.findViewById(R.id.button_back)

        val movie = intent.getParcelableExtra<Movie>("movie") as Movie

        lifecycleScope.launch {
            if (movie.details === null) {
                try {
                    movie.details = (application as MyApplication).getMovieDetails(movie)
                } catch (error: Exception) {
                    errText.text = error.message
                    errText.visibility
                    movie.details = null
                }
            }

            setMovieData(movie)
        }
    }



    fun updateBanner(movie: Movie, showBackButton: Boolean = false) {
        if (showBackButton) {
            backButton.visibility = View.VISIBLE
        }

        Common.fadeOutView(gradientBanner)
        gradientBanner.visibility = View.GONE

        movieTitle.text = movie.title
        movieGenre.text = movie.genre
        movieDescription.text = movie.details?.description
        movieAdditionalInfo.text = "Год: " + movie.year + " | " + "Сезонов: " + movie.details?.seasons?.size + " | " + "Страна: ${movie.country}"

        gradientBanner.visibility = View.VISIBLE

        Common.fadeInView(gradientBanner)

        Common.fadeOutImage(movieCover)
        Glide.with(this).load(movie.posterUrl).diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(false).into(movieCover)
        Common.fadeInImage(movieCover)
    }
}