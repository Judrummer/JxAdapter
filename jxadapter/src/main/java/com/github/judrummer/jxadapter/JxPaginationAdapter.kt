package com.github.judrummer.jxadapter


import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.ViewGroup
import kotlin.properties.Delegates

class JxPaginationAdapter(
        @LayoutRes loadMoreLayoutId: Int = R.layout.item_jx_pagination_load_more,
        builder: JxItemTypeBuilder.() -> Unit) : JxAdapter({
    itemTypeList.addAll(JxItemTypeBuilder().apply(builder).itemTypeList)
    viewHolder {
        JxPaginationLoadMoreViewHolder(it, loadMoreLayoutId)
    }
}) {
    override var items: List<Any> by Delegates.observable(listOf()) { prop, old, new ->
        jxDiffUtil?.let {
            DiffUtil.calculateDiff(it.callback(old, new)).dispatchUpdatesTo(this@JxPaginationAdapter)
        } ?: {
            notifyDataSetChanged()
        }()
        val loadingIndex = new.indexOfFirst { it is JxItemPaginationLoading }
        if (!paginationHasNext) {
            if (loadingIndex != -1) {
                items = new.toMutableList().apply { removeAt(loadingIndex) }
            }
        } else {
            if (loadingIndex == -1 && items.isNotEmpty()) {
                items = new.toMutableList().apply { add(JxItemPaginationLoading) }
            }
        }
    }
    var paginationHasNext = true
    var paginationLoading = false
}

object JxItemPaginationLoading

class JxPaginationLoadMoreViewHolder(parent: ViewGroup,
                                     @LayoutRes layoutId: Int) :
        JxViewHolder<JxItemPaginationLoading>(parent, layoutId) {
    override fun bind(item: JxItemPaginationLoading) {
        (itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.isFullSpan = true
    }
}

fun RecyclerView.onPaginationLoadMore(callback: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val loadingTriggerThreshold = 5
            val visibleItemCount = childCount
            val totalItemCount = layoutManager!!.itemCount
            val layoutManager = layoutManager
            val firstVisibleItemPosition: Int = when (layoutManager) {
                is LinearLayoutManager -> {
                    layoutManager.findFirstVisibleItemPosition()
                }
                is GridLayoutManager -> {
                    layoutManager.findFirstVisibleItemPosition()
                }
                is StaggeredGridLayoutManager -> {
                    if (layoutManager.childCount > 0) {
                        layoutManager.findFirstVisibleItemPositions(null)[0]
                    } else {
                        0
                    }
                }
                else -> {
                    throw IllegalStateException("LayoutManager needs to subclass LinearLayoutManager or GridLayoutManager or StaggeredGridLayoutManager")
                }
            }

            if (totalItemCount - visibleItemCount <= firstVisibleItemPosition + loadingTriggerThreshold || totalItemCount == 0) {
                (adapter as? JxPaginationAdapter)?.run {
                    if (paginationHasNext && !paginationLoading) {
                        callback()
                    }
                }
            }
        }
    })
}