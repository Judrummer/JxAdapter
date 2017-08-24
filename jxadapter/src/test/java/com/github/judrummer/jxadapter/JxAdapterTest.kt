package com.github.judrummer.jxadapter

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

data class HeaderViewData(val id: String) : JxItem

data class ItemViewData(val name: String) : JxItem

data class SpaceViewData(val none: String = "") : JxItem

data class ErrorData(val error: String = "") : JxItem

class HeaderViewHolder(parent: ViewGroup) : JxViewHolder<HeaderViewData>(parent, R.layout.item_test_jx_header) {
    override fun bind(item: HeaderViewData) {

    }
}

class ItemViewHolder(parent: ViewGroup) : JxViewHolder<ItemViewData>(parent, R.layout.item_test_jx_item) {
    override fun bind(item: ItemViewData) {

    }
}

class SpaceViewHolder(parent: ViewGroup) : JxViewHolder<SpaceViewData>(parent, R.layout.item_test_jx_item) {
    override fun bind(item: SpaceViewData) {

    }
}

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class JxAdapterTest {

    lateinit var context: Context

    val TYPE_HEADER = 0
    val TYPE_ITEM = 1
    val TYPE_SPACE = 2

    @Before
    @Throws(Exception::class)
    fun setUp() {
        context = RuntimeEnvironment.application
    }

    @Test
    fun adapterTest() {
        val jxAdapter = JxAdapter {
            viewHolder(::HeaderViewHolder)
            viewHolder(::ItemViewHolder)
            viewHolder(::SpaceViewHolder)
        }

        val items = listOf(
                HeaderViewData("INV001"),
                ItemViewData("ITEM001"),
                ItemViewData("ITEM002"),
                SpaceViewData(""),
                HeaderViewData("INV002"),
                ItemViewData("ITEM003"),
                ItemViewData("ITEM004"),
                SpaceViewData("")
        )

        jxAdapter.items = items

        val parentView = LinearLayout(context)
        val viewHolders = listOf(
                jxAdapter.onCreateViewHolder(parentView, TYPE_HEADER),
                jxAdapter.onCreateViewHolder(parentView, TYPE_ITEM),
                jxAdapter.onCreateViewHolder(parentView, TYPE_SPACE))

        items.forEachIndexed { i, _ ->
            val type = jxAdapter.getItemViewType(i)
            jxAdapter.onBindViewHolder(viewHolders[type], i)
        }
        assert(jxAdapter.getItemViewType(0) == TYPE_HEADER)
        assert(jxAdapter.getItemViewType(1) == TYPE_ITEM)
        assert(jxAdapter.getItemViewType(2) == TYPE_ITEM)
        assert(jxAdapter.getItemViewType(3) == TYPE_SPACE)
        assert(jxAdapter.getItemViewType(4) == TYPE_HEADER)
        assert(jxAdapter.getItemViewType(5) == TYPE_ITEM)
        assert(jxAdapter.getItemViewType(6) == TYPE_ITEM)
        assert(jxAdapter.getItemViewType(7) == TYPE_SPACE)
    }

    @Test
    fun adapterErrorTest() {
        val jxAdapter = JxAdapter {
            viewHolder(::HeaderViewHolder)
            viewHolder(::ItemViewHolder)
            viewHolder(::SpaceViewHolder)
        }

        val items = listOf(
                HeaderViewData("INV001"),
                ItemViewData("ITEM001"),
                ItemViewData("ITEM002"),
                ErrorData()
        )

        jxAdapter.items = items

        var isCatch = false
        try {
            jxAdapter.getItemViewType(3)
        } catch (ex: IllegalStateException) {
            isCatch = true
        } finally {
            assert(isCatch)
        }

    }

}
