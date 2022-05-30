package com.june0122.wakplus.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EmptyDataObserver(
    private val recyclerView: RecyclerView,
    private val view: View
) : RecyclerView.AdapterDataObserver() {

    init {
        checkItemEmpty()
    }

    private fun checkItemEmpty() {
        val isEmpty = recyclerView.adapter?.itemCount == 0
        view.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    // DiffUtil에서 동작하지 않음
    override fun onChanged() {
        super.onChanged()
        checkItemEmpty()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
        checkItemEmpty()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        super.onItemRangeChanged(positionStart, itemCount, payload)
        checkItemEmpty()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        checkItemEmpty()
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount)
        checkItemEmpty()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        checkItemEmpty()
    }
}