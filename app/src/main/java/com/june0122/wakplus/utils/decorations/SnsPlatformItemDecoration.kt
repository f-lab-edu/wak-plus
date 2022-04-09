package com.june0122.wakplus.utils.decorations

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class SnsPlatformItemDecoration(@Px private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        when (position) {
            0 -> {
                outRect.left = spacing
                outRect.right = spacing / 3
            }
            itemCount - 1 -> {
                outRect.left = spacing / 3
                outRect.right = spacing
            }
            else -> {
                outRect.left = spacing / 3
                outRect.right = spacing / 3
            }
        }
    }
}