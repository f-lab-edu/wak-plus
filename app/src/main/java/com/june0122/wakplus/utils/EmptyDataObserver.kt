package com.june0122.wakplus.utils

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmptyDataObserver(
    private val recyclerView: RecyclerView,
    private val textView: TextView
) : RecyclerView.AdapterDataObserver() {

    init {
        checkItemEmpty()
    }

    private fun checkItemEmpty() {
        val isEmpty = recyclerView.adapter?.itemCount == 0
        // val isEmpty = recyclerView.isEmpty() // 아이템이 존재하는데도 true를 반환하는 케이스가 있음
        textView.visibility = if (isEmpty) View.VISIBLE else View.GONE
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
        checkItemEmpty()
        super.onItemRangeInserted(positionStart, itemCount)
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