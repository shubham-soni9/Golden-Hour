package com.goldenhour.ui.base

import android.view.View

import androidx.recyclerview.widget.RecyclerView

/**
 * Base structure for defining Recycler Viewholder
 */
abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // Method to bind the view based on the position
    abstract fun onBind(position: Int)
}

