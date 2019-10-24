package com.goldenhour.util.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.goldenhour.util.Utils

/**
 * Custom Recycleview of setting the maximum height of recycleview
 */
class MaxRecycleView : RecyclerView {

    private var mContext: Context

    constructor(context: Context) : super(context) {
        this.mContext = context
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        this.mContext = context
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        this.mContext = context
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(
            widthSpec,
            MeasureSpec.makeMeasureSpec(Utils.dpToPx(mContext, 480F), View.MeasureSpec.AT_MOST)
        )
    }
}