package com.github.judrummer.jxadapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.properties.Delegates

/**
 * Created by judrummer on 23/12/2559.
 */


class JxAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    var items: List<Any> by Delegates.observable(listOf()) { prop, old, new ->
        if (useDiffUtils) {
            //TODO: implement diffutils
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
        }
    }
    private var jxHolderList: List<JxViewHolder<*>>
    var useDiffUtils: Boolean = false

    constructor(jxHolderList: List<JxViewHolder<*>>, useDiffUtils: Boolean = false) : super() {
        this.jxHolderList = jxHolderList
        this.useDiffUtils = useDiffUtils
    }

    constructor(vararg jxHolder: JxViewHolder<*>, useDiffUtils: Boolean = false) : this(jxHolder.toList(), useDiffUtils)

    override fun getItemCount(): Int = items.size

    operator fun get(position: Int) = items[position]

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder
            = object : RecyclerView.ViewHolder(LayoutInflater.from(parent!!.context).inflate(jxHolderList[viewType].itemLayoutId, parent, false)) {}

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (jxHolderList[getItemViewType(position)].bindHolder as (View, Int, Any) -> Unit).invoke(viewHolder.itemView, position, items[position])
    }

    override fun getItemViewType(position: Int): Int
            = jxHolderList.indexOfFirst { it.itemType.simpleName == items[position].javaClass.simpleName }
}
