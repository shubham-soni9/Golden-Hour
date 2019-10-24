package com.goldenhour.ui.base;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Base structure for defining Recycleview Adapters
 *
 * @param <T> of type Recycler viewholder
 * @param <D> of type Recycler adapter
 */
public abstract class BaseAdapter<T extends RecyclerView.ViewHolder, D> extends RecyclerView.Adapter<T> {


}
