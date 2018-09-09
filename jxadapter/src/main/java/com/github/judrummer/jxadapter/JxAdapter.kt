package com.github.judrummer.jxadapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.properties.Delegates

class JxItemType<T : Any>(val itemType: Class<T>, val viewHolderProvider: (ViewGroup) -> JxViewHolder<T>)

class JxItemTypeBuilder {
    val itemTypeList = mutableListOf<JxItemType<out Any>>()

    inline fun <reified T : Any, reified H : JxViewHolder<T>> viewHolder(noinline viewHolderProvider: (ViewGroup) -> H) {
        itemTypeList.add(JxItemType(T::class.java, viewHolderProvider))
    }
}

open class JxAdapter(builder: JxItemTypeBuilder.() -> Unit) : RecyclerView.Adapter<JxViewHolder<Any>>() {

    private val itemTypeList = JxItemTypeBuilder().apply(builder).itemTypeList

    private val mapToType: Map<Class<out Any>, Int>

    var jxDiffUtil: JxDiffUtil? = null

    init {
        val tempMapToType = mutableMapOf<Class<out Any>, Int>()
        itemTypeList.forEachIndexed { i, itemType ->
            tempMapToType.put(itemType.itemType, i)
        }
        mapToType = tempMapToType
    }

    open var items: List<Any> by Delegates.observable(listOf()) { prop, old, new ->
        jxDiffUtil?.let {
            DiffUtil.calculateDiff(it.callback(old, new)).dispatchUpdatesTo(this@JxAdapter)
        } ?: {
            notifyDataSetChanged()
        }()
    }

    override fun onBindViewHolder(holder: JxViewHolder<Any>, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JxViewHolder<Any> = itemTypeList[viewType].viewHolderProvider.invoke(parent) as JxViewHolder<Any>

    override fun getItemViewType(position: Int): Int = mapToType.getOrElse(items[position].javaClass) { throw IllegalStateException("Missing JxHolder<${items[position].javaClass.simpleName}>") }

    override fun getItemCount(): Int = items.size
}

abstract class JxViewHolder<T> : RecyclerView.ViewHolder {

    constructor(parent: ViewGroup, layoutResId: Int) : super(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false))

    constructor(view: View) : super(view)

    abstract fun bind(item: T)
}

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

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = areItemsTheSame.invoke(oldItems[oldItemPosition], newItems[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = areContentsTheSame.invoke(oldItems[oldItemPosition], newItems[newItemPosition])

    }
}
