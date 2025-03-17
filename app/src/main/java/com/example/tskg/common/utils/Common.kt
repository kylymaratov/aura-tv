package com.example.tskg.common.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tskg.R

class Common {
    companion object {
        fun getWidthInPercent(context: Context, percent: Int): Int {
            val width = context.resources.displayMetrics.widthPixels ?: 0
            return (width * percent) / 100
        }

        fun getHeightInPercent(context: Context, percent: Int): Int {
            val height = context.resources.displayMetrics.heightPixels ?: 0
            return (height * percent) / 100
        }

        fun dpToPx(context: Context, dp: Int): Int {
            val density = context.resources.displayMetrics.density
            return (dp * density).toInt()
        }

        fun fadeInView(view: View) {
            val fadeIn = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
            view.startAnimation(fadeIn)
        }

        fun fadeOutView(view: View) {
            val fadeOut = AnimationUtils.loadAnimation(view.context, R.anim.fade_out)
            view.startAnimation(fadeOut)
        }

        fun fadeInImage(imageView: ImageView) {
            val fadeIn = AnimationUtils.loadAnimation(imageView.context, R.anim.fade_in)
            imageView.startAnimation(fadeIn)
        }

        fun fadeOutImage(imageView: ImageView) {
            val fadeOut = AnimationUtils.loadAnimation(imageView.context, R.anim.fade_out)
            imageView.startAnimation(fadeOut)
        }
    }


    class HorizontalSpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: android.graphics.Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.right = spacing
        }
    }


    class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.top = spacing

                outRect.right = (column + 1) * spacing / spanCount
                outRect.bottom = spacing
            } else {

                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                outRect.top = spacing
                outRect.bottom = spacing
            }
        }
    }



}