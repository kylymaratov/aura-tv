package com.example.tskg.mobile.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tskg.MyApplication
import com.example.tskg.R
import kotlinx.coroutines.launch

class CategoryFragment : Fragment(R.layout.fragment_category) {
    private var categoryId: Int = 0
    private  var categoryTitle: String = ""
    val moviesGridFragment: MoviesGridFragment = MoviesGridFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            categoryId = it.getInt("categoryId", 0)
            categoryTitle = it.getString("categoryTitle", "")
        }
        getCategoryProcess(view)
        setFragments()
    }

    private fun setFragments() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.movies_grid_fragment, moviesGridFragment)
        transaction.commit()
    }

    private fun getCategoryProcess(view: View) {
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        val errorText =  view.findViewById<TextView>(R.id.error_text)
        val categoryText = view.findViewById<TextView>(R.id.category_text)

        lifecycleScope.launch {
            try{
                progressBar.visibility = View.VISIBLE
                 val data = (requireActivity().application as MyApplication).getCategoryData(categoryId, 1)
                 categoryText.text = categoryTitle
            } catch(error: Exception) {
                errorText.visibility = TextView.VISIBLE
                errorText.text = error.message
            } finally {
                progressBar.visibility = ProgressBar.GONE
            }
        }
    }
}