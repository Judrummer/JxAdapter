package com.github.judrummer.jxadapter

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.item_test_jx_header.view.*
import kotlinx.android.synthetic.main.item_test_jx_item.view.*
import kotlinx.android.synthetic.main.item_test_jx_space.view.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

data class HeaderViewData(val id: String)

data class ItemViewData(val name: String)

data class SpaceViewData(val none: String = "")

data class ErrorData(val error: String = "")

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
        val jxAdapter = JxAdapter(
                JxViewHolder<HeaderViewData>(R.layout.item_test_jx_header) { position, item ->
                    assert(itemView.tvTestItemHeader != null)
                },
                JxViewHolder<ItemViewData>(R.layout.item_test_jx_item) { position, item ->
                    assert(itemView.tvTestItemItem != null)
                },
                JxViewHolder<SpaceViewData>(R.layout.item_test_jx_space) { position, item ->
                    assert(itemView.llTestItemSpace != null)
                })

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

        items.forEachIndexed { i, item ->
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
        val jxAdapter = JxAdapter(
                JxViewHolder<HeaderViewData>(R.layout.item_test_jx_header) { position, item ->
                    assert(itemView.tvTestItemHeader != null)
                },
                JxViewHolder<ItemViewData>(R.layout.item_test_jx_item) { position, item ->
                    assert(itemView.tvTestItemItem != null)
                },
                JxViewHolder<SpaceViewData>(R.layout.item_test_jx_space) { position, item ->
                    assert(itemView.llTestItemSpace != null)
                })

        val items = listOf(
                HeaderViewData("INV001"),
                ItemViewData("ITEM001"),
                ItemViewData("ITEM002"),
                ErrorData()
        )

        jxAdapter.items = items

        val parentView = LinearLayout(context)
        val viewHolders = listOf(
                jxAdapter.onCreateViewHolder(parentView, TYPE_HEADER),
                jxAdapter.onCreateViewHolder(parentView, TYPE_ITEM),
                jxAdapter.onCreateViewHolder(parentView, TYPE_SPACE))

        items.forEachIndexed { i, item ->
            val type = jxAdapter.getItemViewType(i)
            if (type == -1) {
                var catchException = false
                try {
                    jxAdapter.onBindViewHolder(viewHolders[type], i)
                } catch (ex: Exception) {
                    catchException = true
                } finally {
                    assert(catchException)
                }

            } else {
                jxAdapter.onBindViewHolder(viewHolders[type], i)
            }
        }
        assert(jxAdapter.getItemViewType(0) == TYPE_HEADER)
        assert(jxAdapter.getItemViewType(1) == TYPE_ITEM)
        assert(jxAdapter.getItemViewType(2) == TYPE_ITEM)
        assert(jxAdapter.getItemViewType(3) == -1)
    }

}
