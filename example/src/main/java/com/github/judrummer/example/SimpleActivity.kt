package com.github.judrummer.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import com.github.judrummer.jxadapter.JxAdapter
import com.github.judrummer.jxadapter.JxViewHolder
import com.github.judrummer.jxadapter.JxViewHolderLayoutContainer
import com.taskworld.kxandroid.format
import com.taskworld.kxandroid.toast
import kotlinx.android.synthetic.main.activity_simple.*
import kotlinx.android.synthetic.main.item_invoice_footer.*
import kotlinx.android.synthetic.main.item_invoice_header.view.*
import kotlinx.android.synthetic.main.item_invoice_item.*

typealias ItemClick = (item: InvoiceItemViewData) -> Unit

class InvoiceHeaderViewHolder(parent: ViewGroup)
    : JxViewHolder<InvoiceHeaderViewData>(parent, R.layout.item_invoice_header) {

    val header = itemView.tvItemInvoiceHeaderNumber

    override fun bind(item: InvoiceHeaderViewData) {
        header.text = item.invoiceNumber
    }

}

class InvoiceItemViewHolder(parent: ViewGroup,
                            val onNameClick: ItemClick,
                            val onTotalClick: ItemClick)
    : JxViewHolderLayoutContainer<InvoiceItemViewData>(parent, R.layout.item_invoice_item) {

    override fun bind(item: InvoiceItemViewData) {
        tvItemInvoiceItemProductName.text = item.productName
        tvItemInvoiceItemProductName.setOnClickListener {
            onNameClick(item)
        }
        tvItemInvoiceItemQuantity.text = "x ${item.quantity}"
        tvItemInvoiceItemPrice.text = "$ ${item.price.format(2)}"
        tvItemInvoiceItemTotal.text = "$ ${item.totalAmount.format(2)}"
        tvItemInvoiceItemTotal.setOnClickListener {
            onTotalClick(item)
        }
    }

}

class InvoiceFooterViewHolder(parent: ViewGroup)
    : JxViewHolderLayoutContainer<InvoiceFooterViewData>(parent, R.layout.item_invoice_footer) {
    override fun bind(item: InvoiceFooterViewData) {
        tvItemInvoiceFooterTotal.text = "$ ${item.total.format(2)}"
    }
}

class InvoiceSpaceViewHolder(parent: ViewGroup)
    : JxViewHolderLayoutContainer<InvoiceSpaceViewData>(parent, R.layout.item_invoice_space) {
    override fun bind(item: InvoiceSpaceViewData) {}
}

class SimpleActivity : AppCompatActivity() {

    private val jxAdapter = JxAdapter {
        viewHolder(::InvoiceHeaderViewHolder)
        viewHolder {
            InvoiceItemViewHolder(it,
                    this@SimpleActivity::onNameClick,
                    this@SimpleActivity::onTotalClick)
        }
        viewHolder(::InvoiceFooterViewHolder)
        viewHolder(::InvoiceSpaceViewHolder)
    }

    private fun onNameClick(item: InvoiceItemViewData) {
        toast("onNameClick $item")
    }

    private fun onTotalClick(item: InvoiceItemViewData) {
        toast("onTotalClick $item")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        setSupportActionBar(toolbar)

        //jxAdapter.jxDiffUtil = JxDiffUtil() // uncomment this if want to use DiffUtil
        rvExample.apply {
            layoutManager = LinearLayoutManager(this@SimpleActivity)
            adapter = jxAdapter
        }

        srlExample.setOnRefreshListener {
            fetchData()
            srlExample.isRefreshing = false
        }

        fetchData()
    }

    fun fetchData() {
        jxAdapter.items = fetchInvoiceData().toViewDataList()
    }

}


