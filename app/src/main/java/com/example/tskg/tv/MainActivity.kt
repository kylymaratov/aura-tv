package com.example.tskg.tv

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tskg.R
import com.example.tskg.common.adapters.MenuAdapter
import com.example.tskg.tv.fragments.CategoryFragment
import com.example.tskg.tv.fragments.HomeFragment
import com.example.tskg.tv.fragments.SearchFragment
import com.example.tskg.common.models.MenuItem
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity(R.layout.activity_main) {
    private lateinit var navCategories: RecyclerView
    private lateinit var container: FrameLayout
    private lateinit var navMock: FrameLayout
    private lateinit var hintText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setFragments()
        navCategories = findViewById(R.id.nav_categories)
        container = findViewById(R.id.container)
        navMock = findViewById(R.id.nav_mock)
        hintText = findViewById(R.id.hint_text)

        val categories = listOf(
            MenuItem(id = 1, title = "Главная"),
            MenuItem(id = 2, title = "Поиск"),
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
            MenuItem(id = 6, title = "Зарубежные"),
        )

        val adapter = MenuAdapter(categories) {item ->
            if (item.id == 1) {
                setFragments()
            }else if(item.id == 2) {
                val searchFragment = SearchFragment()
                changeFragment(searchFragment)
            }else {
                val categoryFragment = CategoryFragment()
                val bundle = Bundle().apply {
                    putInt("categoryId", item.id)
                    putString("categoryTitle", item.title)
                }
                categoryFragment.arguments = bundle
                changeFragment(categoryFragment)
            }
        }


        navCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        navCategories.adapter = adapter

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> {
                openOrCloseMenu()
                return true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (navMock.isFocused) {
                    openOrCloseMenu()
                }
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (!navCategories.isGone) {
                    openOrCloseMenu()
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun openOrCloseMenu () {
        if (navCategories.isGone) {
            navCategories.visibility = View.VISIBLE
            navMock.visibility = View.GONE
            navCategories.requestFocus()
         }else {
            navCategories.visibility = View.GONE
            navMock.visibility = View.VISIBLE
        }
        hintText.text = ""
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    private fun setFragments() {
        val homeFragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, homeFragment)
        transaction.commit()
    }
}