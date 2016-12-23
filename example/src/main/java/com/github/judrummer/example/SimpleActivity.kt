package com.github.judrummer.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.github.judrummer.jxadapter.JxAdapter
import com.github.judrummer.jxadapter.JxViewHolder
import com.taskworld.kxandroid.format
import kotlinx.android.synthetic.main.activity_simple.*
import kotlinx.android.synthetic.main.item_invoice_footer.view.*
import kotlinx.android.synthetic.main.item_invoice_header.view.*
import kotlinx.android.synthetic.main.item_invoice_item.view.*

class SimpleActivity : AppCompatActivity() {

    val jxAdapter = JxAdapter(
            JxViewHolder<InvoiceHeaderViewData>(R.layout.item_invoice_header) { view, position, item ->
                view.apply {
                    tvItemInvoiceHeaderNumber.text = item.invoiceNumber
                }
            },
            JxViewHolder<InvoiceItemViewData>(R.layout.item_invoice_item) { view, position, item ->
                view.apply {
                    tvItemInvoiceItemProductName.text = item.productName
                    tvItemInvoiceItemQuantity.text = "x ${item.quantity}"
                    tvItemInvoiceItemPrice.text = "$ ${item.price.format(2)}"
                    tvItemInvoiceItemTotal.text = "$ ${item.totalAmount.format(2)}"
                }
            },
            JxViewHolder<InvoiceFooterViewData>(R.layout.item_invoice_footer) { view, position, item ->
                view.apply {
                    tvItemInvoiceFooterTotal.text = "$ ${item.total.format(2)}"

                }
            },
            JxViewHolder<InvoiceSpaceViewData>(R.layout.item_invoice_space) { view, position, item ->

            })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        setSupportActionBar(toolbar)
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


