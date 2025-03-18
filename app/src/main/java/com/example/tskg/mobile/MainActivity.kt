package com.example.tskg.mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.tskg.R
import com.example.tskg.mobile.fragments.HomeFragment
 import com.google.android.material.appbar.MaterialToolbar

class MainActivity: FragmentActivity(R.layout.activity_mobile_main) {
    private val homeFragment: HomeFragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val materialToolbar = findViewById<MaterialToolbar>(R.id.material_toolbar)

        val intent = Intent(this, SearchActivity::class.java)

        materialToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.go_search_button -> {
                    this.startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

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