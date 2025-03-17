package com.example.tskg.mobile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.tskg.MyApplication
import com.example.tskg.R
import com.example.tskg.common.models.Movie
import com.example.tskg.common.models.MoviesList
import com.example.tskg.common.parser.DataParser
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kotlin.math.abs

class HomeFragment: Fragment(R.layout.fragment_mobile_home){
    private val dataParser: DataParser = DataParser()
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mobile_home, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)

        getDataProcess(view)

        swipeRefreshLayout.setOnRefreshListener {
            getDataProcess(view, true)
        }

        swipeRefreshLayout.isEnabled = false

        return view
    }

    private fun initialTabSwitcher(movies: List<MoviesList>) {
        val fragmentList = listOf(MoviesGridFragment.newInstance(movies[0]), MoviesGridFragment.newInstance(movies[1]))
        val tabTitles = listOf("Новинки", "Популярное")

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = fragmentList.size
            override fun createFragment(position: Int) = fragmentList[position]
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        viewPager.setPageTransformer { page, position ->
            page.alpha = 0.25f + (1 - abs(position))
        }
    }

    private fun getDataProcess(view: View, isRefresh: Boolean = false) {
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        val errorText =  view.findViewById<TextView>(R.id.error_text)

        lifecycleScope.launch {
            try{
                if (!isRefresh) {
                    progressBar.visibility = ProgressBar.VISIBLE
                }
                val movies = getData()
                initialTabSwitcher(movies)
            } catch(error: Exception) {
                errorText.visibility = TextView.VISIBLE
                errorText.text = error.message
            } finally {
                progressBar.visibility = ProgressBar.GONE
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private suspend fun getData(): List<MoviesList> {
        try {
            val request = (requireActivity().application as MyApplication).htmlRequest
            val baseUrl = (requireActivity().application as MyApplication).BASE_URL

            val result = request.getHtmlPage("/")

            val body = result.body()

            if (!result.isSuccessful || body == null) {
                throw IllegalStateException("Failed to fetch data from the server")
            }

            val movies = dataParser.parseHomePage(body.toString(), baseUrl)

            return movies
        } catch (error: Exception) {
            throw error
        }
    }
}