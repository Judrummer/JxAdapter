package com.github.judrummer.jxadapter

import android.support.v7.util.DiffUtil

/**
 * Created by judrummer on 11/1/2560.
 */

open class JxDiffUtil {
    open val areItemsTheSame: (Any, Any) -> Boolean = { oldItem, newItem ->
        oldItem == newItem
    }
    open val areContentsTheSame: (Any, Any) -> Boolean = { oldItem, newItem ->
        oldItem == newItem
    }

    fun callback(oldItems: List<Any>, newItems: List<Any>) = object : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
                = areItemsTheSame.invoke(oldItems[oldItemPosition], newItems[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
                = areContentsTheSame.invoke(oldItems[oldItemPosition], newItems[newItemPosition])

    }
}

