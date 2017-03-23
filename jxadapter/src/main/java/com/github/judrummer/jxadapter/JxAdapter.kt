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

open class JxAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    open var items: List<JxItem> by Delegates.observable(listOf()) { prop, old, new ->
        jxDiffUtil?.let {
            DiffUtil.calculateDiff(it.callback(old, new)).dispatchUpdatesTo(this@JxAdapter)
        } ?: {
            notifyDataSetChanged()
        }()
    }
    private var jxHolderList: List<JxViewHolder<*>>
    private var mapType: Map<Class<out JxItem>, Int>
    var jxDiffUtil: JxDiffUtil? = null

    constructor(jxHolderList: List<JxViewHolder<*>>, jxDiffUtil: JxDiffUtil? = null) : super() {
        this.jxHolderList = jxHolderList
        this.jxDiffUtil = jxDiffUtil
        val tempMap = mutableMapOf<Class<out JxItem>, Int>()
        jxHolderList.forEachIndexed { i, jxViewHolder ->
            tempMap.put(jxViewHolder.itemType, i)
        }
        this.mapType = tempMap
    }

    constructor(vararg jxHolder: JxViewHolder<*>, jxDiffUtil: JxDiffUtil? = null) : this(jxHolder.toList(), jxDiffUtil)

    override fun getItemCount(): Int = items.size

    operator fun get(position: Int) = items[position]

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder
            = object : RecyclerView.ViewHolder(LayoutInflater.from(parent!!.context).inflate(jxHolderList[viewType].itemLayoutId, parent, false)) {}

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val itemType = getItemViewType(position)
        if (itemType == -1) {
            throw RuntimeException("Missing JxHolder<${items[position].javaClass.simpleName}>")
        }
        val bindHolder = (jxHolderList[itemType].bindHolder as RecyclerView.ViewHolder.(Int, JxItem) -> Unit)
        viewHolder.bindHolder(position, items[position])
    }

    override fun getItemViewType(position: Int): Int
            = mapType[items[position].javaClass] ?: -1
}
