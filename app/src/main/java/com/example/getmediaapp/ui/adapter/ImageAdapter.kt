package com.example.getmediaapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.getmediaapp.R
import kotlinx.android.synthetic.main.recycle_image_item.view.*

class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ViewHold>() {

    private val imageList = mutableListOf<String>()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_image_item, parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        val view = holder.itemView
        val imageUrl = imageList[position]
        Glide.with(context)
            .load(imageUrl)
            .into(view.ivImage)

    }

    public fun addToFirst(string: String) {
        imageList.add(0, string)
        notifyDataSetChanged()
    }

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView)
}