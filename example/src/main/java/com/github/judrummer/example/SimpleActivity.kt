package com.github.judrummer.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import com.github.judrummer.jxadapter.JxAdapter
import com.github.judrummer.jxadapter.JxViewHolder
import com.taskworld.kxandroid.format
import kotlinx.android.synthetic.main.activity_simple.*
import kotlinx.android.synthetic.main.item_invoice_footer.*
import kotlinx.android.synthetic.main.item_invoice_header.*
import kotlinx.android.synthetic.main.item_invoice_item.*


class InvoiceHeaderViewHolder(parent: ViewGroup) : JxViewHolder<InvoiceHeaderViewData>(parent, R.layout.item_invoice_header) {

    override fun bind(item: InvoiceHeaderViewData) {
        tvItemInvoiceHeaderNumber.text = item.invoiceNumber
    }

}

class InvoiceItemViewHolder(parent: ViewGroup) : JxViewHolder<InvoiceItemViewData>(parent, R.layout.item_invoice_item) {

    override fun bind(item: InvoiceItemViewData) {
        tvItemInvoiceItemProductName.text = item.productName
        tvItemInvoiceItemQuantity.text = "x ${item.quantity}"
        tvItemInvoiceItemPrice.text = "$ ${item.price.format(2)}"
        tvItemInvoiceItemTotal.text = "$ ${item.totalAmount.format(2)}"
    }

}

class InvoiceFooterViewHolder(parent: ViewGroup) : JxViewHolder<InvoiceFooterViewData>(parent, R.layout.item_invoice_footer) {
    override fun bind(item: InvoiceFooterViewData) {
        tvItemInvoiceFooterTotal.text = "$ ${item.total.format(2)}"
    }
}

class InvoiceSpaceViewHolder(parent: ViewGroup) : JxViewHolder<InvoiceSpaceViewData>(parent, R.layout.item_invoice_space) {
    override fun bind(item: InvoiceSpaceViewData) {}
}

class SimpleActivity : AppCompatActivity() {

    private val jxAdapter = JxAdapter {
        itemType(::InvoiceHeaderViewHolder)
        itemType(::InvoiceItemViewHolder)
        itemType(::InvoiceFooterViewHolder)
        itemType(::InvoiceSpaceViewHolder)
    }

//    val jxAdapter = JxAdapter(
//            JxViewHolder<InvoiceHeaderViewData>(R.layout.item_invoice_header) { position, item ->
//                //                itemView.apply {
////                    tvItemInvoiceHeaderNumber.text = item.invoiceNumber
////                }
//            },
//            JxViewHolder<InvoiceItemViewData>(R.layout.item_invoice_item) { position, item ->
//                //                itemView.apply {
////                    tvItemInvoiceItemProductName.text = item.productName
////                    tvItemInvoiceItemQuantity.text = "x ${item.quantity}"
////                    tvItemInvoiceItemPrice.text = "$ ${item.price.format(2)}"
////                    tvItemInvoiceItemTotal.text = "$ ${item.totalAmount.format(2)}"
////                }
//            },
//            JxViewHolder<InvoiceFooterViewData>(R.layout.item_invoice_footer) { position, item ->
//                //                itemView.apply {
////                    tvItemInvoiceFooterTotal.text = "$ ${item.total.format(2)}"
////                }
//            },
//            JxViewHolder<InvoiceSpaceViewData>(R.layout.item_invoice_space) { position, item ->
//
//            })

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


