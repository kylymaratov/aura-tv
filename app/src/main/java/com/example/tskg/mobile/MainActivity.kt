package com.example.tskg.mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
 import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import com.example.tskg.R
import com.example.tskg.mobile.fragments.CategoryFragment
import com.example.tskg.mobile.fragments.HomeFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.internal.NavigationMenu
import com.google.android.material.navigation.NavigationView

class MainActivity: FragmentActivity(R.layout.activity_mobile_main) {
    private val homeFragment: HomeFragment = HomeFragment()
    private val categoryFragment: CategoryFragment = CategoryFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val materialToolbar = findViewById<MaterialToolbar>(R.id.material_toolbar)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        val SearchIntent = Intent(this, SearchActivity::class.java)

        navigationView.setNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.go_category -> {

                 true
            }
            else -> false
        }
        }

        materialToolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        materialToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.go_search_button -> {
                    this.startActivity(SearchIntent)
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

    private fun openCategoriesFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mobile_container, categoryFragment)
        transaction.commit()
    }

    private fun setFragments() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mobile_container, homeFragment)
        transaction.commit()
    }
}