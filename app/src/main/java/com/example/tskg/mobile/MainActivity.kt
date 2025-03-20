package com.example.tskg.mobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.tskg.MyApplication
import com.example.tskg.R
import com.example.tskg.common.models.MenuItem
import com.example.tskg.mobile.fragments.HomeFragment
import com.example.tskg.mobile.fragments.MoviesGridFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity(R.layout.activity_mobile_main) {
    private val homeFragment = HomeFragment()
    private val categories = listOf(
        MenuItem(id = 1, title = "Главная"),
        MenuItem(id = 12, title = "Аниме"),
        MenuItem(id = 19, title = "Сериалы"),
        MenuItem(id = 25, title = "Полнометражные"),
        MenuItem(id = 5, title = "Cartoon Network"),
        MenuItem(id = 3, title = "Мультфильмы"),
        MenuItem(id = 18, title = "Турецкие фильмы"),
        MenuItem(id = 29, title = "Южная Корея"),
        MenuItem(id = 8, title = "Британские"),
        MenuItem(id = 31, title = "Китай"),
        MenuItem(id = 30, title = "Япония"),
        MenuItem(id = 4, title = "Российские"),
        MenuItem(id = 11, title = "Кыргызские"),
        MenuItem(id = 20, title = "Украинские"),
        MenuItem(id = 33, title = "Таиланд"),
        MenuItem(id = 32, title = "Индия"),
        MenuItem(id = 27, title = "Франшизы"),
        MenuItem(id = 17, title = "Спорт"),
        MenuItem(id = 6, title = "Зарубежные")
    )

    private lateinit var materialToolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        materialToolbar = findViewById(R.id.material_toolbar)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        val searchIntent = Intent(this, SearchActivity::class.java)


        val menu = navigationView.menu
        menu.clear()

        categories.forEachIndexed { index, item ->
            menu.add(Menu.NONE, item.id, Menu.NONE, item.title)
                .setIcon(if (index == 0) R.drawable.ic_home else R.drawable.ic_movie)
        }


        navigationView.setNavigationItemSelectedListener { menuItem ->
            val selectedCategory = categories.find { it.id == menuItem.itemId }

            selectedCategory?.let {
                if (it.id == 1) {
                    setHomeFragment()
                } else {
                    getCategoryProcess(it.title, it.id)
                }

                drawerLayout.closeDrawer(GravityCompat.START)
                return@setNavigationItemSelectedListener true
            } ?: run {
                return@setNavigationItemSelectedListener false
            }
        }

        materialToolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        materialToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.go_search_button -> {
                    startActivity(searchIntent)
                    true
                }
                else -> false
            }
        }

        setHomeFragment()
    }

    private fun getCategoryProcess(categoryTitle: String, categoryId: Int, pageId: Int = 1) {
        lifecycleScope.launch {
            try {
                val data = (application as MyApplication).getCategoryData(categoryId, pageId)
                val moviesGridInstance = MoviesGridFragment.newInstance(data, categoryId, pageId)
                val transaction = supportFragmentManager.beginTransaction()

                transaction.replace(R.id.mobile_container, moviesGridInstance)
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

                materialToolbar.title = categoryTitle
                transaction.commit()
            } catch (error: Exception) {
                Toast.makeText(this@MainActivity, "Не удалось загрузить категорию: ${categoryTitle}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setHomeFragment() {
        materialToolbar.title = "Главная"
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.mobile_container, homeFragment)
        transaction.commit()
    }
}
