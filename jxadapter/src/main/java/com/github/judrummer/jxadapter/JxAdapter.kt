package com.github.judrummer.jxadapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.properties.Delegates

interface JxItem

class JxItemType<T : JxItem>(val itemType: Class<T>, val viewHolderProvider: (ViewGroup) -> JxViewHolder<T>)

class JxItemTypeBuilder {
    val itemTypeList = mutableListOf<JxItemType<out JxItem>>()

    inline fun <reified T : JxItem, reified H : JxViewHolder<T>> viewHolder(noinline viewHolderProvider: (ViewGroup) -> H) {
        itemTypeList.add(JxItemType(T::class.java, viewHolderProvider))
    }
}

open class JxAdapter(builder: JxItemTypeBuilder.() -> Unit) : RecyclerView.Adapter<JxViewHolder<JxItem>>() {

    private val itemTypeList = JxItemTypeBuilder().apply(builder).itemTypeList

    private val mapToType: Map<Class<out JxItem>, Int>

    var jxDiffUtil: JxDiffUtil? = null

    init {
        val tempMapToType = mutableMapOf<Class<out JxItem>, Int>()
        itemTypeList.forEachIndexed { i, itemType ->
            tempMapToType.put(itemType.itemType, i)
        }
        mapToType = tempMapToType
    }

    open var items: List<JxItem> by Delegates.observable(listOf()) { prop, old, new ->
        jxDiffUtil?.let {
            DiffUtil.calculateDiff(it.callback(old, new)).dispatchUpdatesTo(this@JxAdapter)
        } ?: {
            notifyDataSetChanged()
        }()
    }

    override fun onBindViewHolder(holder: JxViewHolder<JxItem>, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JxViewHolder<JxItem>
            = itemTypeList[viewType].viewHolderProvider.invoke(parent) as JxViewHolder<JxItem>

    override fun getItemViewType(position: Int): Int
            = mapToType.getOrElse(items[position].javaClass) { throw IllegalStateException("Missing JxHolder<${items[position].javaClass.simpleName}>") }

    override fun getItemCount(): Int = items.size
}

abstract class JxViewHolder<in T : JxItem>(parent: ViewGroup,
                                           layoutResId: Int,
                                           view: View = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false))
    : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}

open class JxDiffUtil {
    open val areItemsTheSame: (JxItem, JxItem) -> Boolean = { oldItem, newItem ->
        oldItem == newItem
    }
    open val areContentsTheSame: (JxItem, JxItem) -> Boolean = { oldItem, newItem ->
        oldItem == newItem
    }

    fun callback(oldItems: List<JxItem>, newItems: List<JxItem>) = object : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
                = areItemsTheSame.invoke(oldItems[oldItemPosition], newItems[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
                = areContentsTheSame.invoke(oldItems[oldItemPosition], newItems[newItemPosition])

    }
}
