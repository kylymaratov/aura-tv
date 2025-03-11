package com.example.tskg.tv.presenters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tskg.R
import com.example.tskg.common.models.Movie
import com.example.tskg.common.utils.Common

class MovieListPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_movie_list, parent, false)

        val params = view.layoutParams
        params.width = Common.getWidthInPercent(parent!!.context, 14)
        params.height = Common.getHeightInPercent(parent!!.context, 34)
        view.layoutParams = params

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        val content = item as Movie
        val movieCover: ImageView = viewHolder?.view?.findViewById(R.id.poster_image) ?: return

        val params = viewHolder.view.layoutParams

        viewHolder.view.layoutParams = params

        Glide.with(viewHolder.view.context)
            .load(content.posterUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(movieCover)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
        viewHolder?.view?.findViewById<TextView>(R.id.movie_title)?.text = ""
        viewHolder?.view?.findViewById<ImageView>(R.id.poster_image)?.setImageDrawable(null)
    }

}