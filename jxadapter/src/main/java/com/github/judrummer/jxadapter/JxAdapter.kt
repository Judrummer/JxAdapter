package com.github.judrummer.jxadapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.properties.Delegates

/**
 * Created by judrummer on 23/12/2559.
 */


open class JxDiffUtilCallback(val oldItems: List<Any>, val newItems: List<Any>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newItem = newItems[newItemPosition]
        val oldItem = oldItems[oldItemPosition]
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newItem = newItems[newItemPosition]
        val oldItem = oldItems[oldItemPosition]
        return oldItem == newItem
    }

}

class JxAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    var items: List<Any> by Delegates.observable(listOf()) { prop, old, new ->
        val callback = diffUtilCallback
        if (callback != null) {
            DiffUtil.calculateDiff(callback.invoke(old, new)).dispatchUpdatesTo(this@JxAdapter)
        } else {
            notifyDataSetChanged()
        }
    }
    private var jxHolderList: List<JxViewHolder<*>>
    var diffUtilCallback: ((List<Any>, List<Any>) -> JxDiffUtilCallback)? = null

    constructor(jxHolderList: List<JxViewHolder<*>>, diffUtilCallback: ((List<Any>, List<Any>) -> JxDiffUtilCallback)? = null) : super() {
        this.jxHolderList = jxHolderList
        this.diffUtilCallback = diffUtilCallback
    }

    constructor(vararg jxHolder: JxViewHolder<*>, diffUtilCallback: ((List<Any>, List<Any>) -> JxDiffUtilCallback)? = null) : this(jxHolder.toList(), diffUtilCallback)

    override fun getItemCount(): Int = items.size

    operator fun get(position: Int) = items[position]

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder
            = object : RecyclerView.ViewHolder(LayoutInflater.from(parent!!.context).inflate(jxHolderList[viewType].itemLayoutId, parent, false)) {}

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val bindHolder = (jxHolderList[getItemViewType(position)].bindHolder as RecyclerView.ViewHolder.(Int, Any) -> Unit)
        viewHolder.bindHolder(position, items[position])
    }

    override fun getItemViewType(position: Int): Int
            = jxHolderList.indexOfFirst { it.itemType == items[position].javaClass }
}
