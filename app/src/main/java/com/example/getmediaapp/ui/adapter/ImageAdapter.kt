package com.example.getmediaapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.getmediaapp.R

class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ViewHold>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_image_item, parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        TODO("Not yet implemented")
    }

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}