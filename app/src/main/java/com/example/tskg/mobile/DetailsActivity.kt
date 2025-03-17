package com.example.tskg.mobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tskg.MyApplication
import com.example.tskg.R
import com.example.tskg.common.models.Movie
import com.example.tskg.common.models.MovieSeason
import com.example.tskg.common.utils.Common
import com.example.tskg.mobile.fragments.SeriesListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kotlin.math.abs

class DetailsActivity : FragmentActivity(R.layout.activity_mobile_details) {
    private  lateinit var movieTitle: TextView
    private  lateinit var movieGenre: TextView
    private  lateinit var movieDescription: TextView
    private  lateinit var movieAdditionalInfo: TextView
    private  lateinit var movieCover: ImageView
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val errText = findViewById<TextView>(R.id.error_text)
        val infoLayout: ConstraintLayout = findViewById(R.id.layout_info)

        movieCover = findViewById(R.id.movie_cover)
        movieTitle = infoLayout.findViewById(R.id.movie_title)
        movieGenre = infoLayout.findViewById(R.id.movie_genre)
        movieDescription = infoLayout.findViewById(R.id.movie_description)
        movieAdditionalInfo = infoLayout.findViewById(R.id.movie_additional_info)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)

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
            setMovieContent(movie)
            movie.details?.let { initialTabSwitcher(it.seasons) }
        }
    }

    private fun initialTabSwitcher(seasons: List<MovieSeason>) {
        val fragmentList = seasons.map { It ->
            SeriesListFragment.newInstance(It)
        }

        val tabTitles = seasons.map { season ->
            season.seasonId
        }

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = fragmentList.size
            override fun createFragment(position: Int): Fragment = fragmentList[position]
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Сезон " + tabTitles[position].toString()
        }.attach()

        viewPager.setPageTransformer { page, position ->
            page.alpha = 0.25f + (1 - abs(position))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setMovieContent(movie: Movie) {
        movieTitle.text = movie.title
        movieGenre.text = movie.genre
        movieDescription.text = movie.details?.description
        movieAdditionalInfo.text = "Год: " + movie.year + " | " + "Сезонов: " + movie.details?.seasons?.size + " | " + "Страна: ${movie.country}"

        Common.fadeOutImage(movieCover)
        Glide.with(this).load(movie.posterUrl).diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(false).into(movieCover)
        Common.fadeInImage(movieCover)
    }
}