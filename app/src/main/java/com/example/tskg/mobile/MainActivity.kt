package com.example.tskg.mobile

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.tskg.R
import com.example.tskg.mobile.fragments.HomeFragment

class MainActivity: FragmentActivity(R.layout.activity_mobile_main) {
    private val homeFragment: HomeFragment = HomeFragment()

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        setFragments()
        return super.onCreateView(name, context, attrs)
    }

    private fun setFragments() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mobile_container, homeFragment)
        transaction.commit()
    }
}