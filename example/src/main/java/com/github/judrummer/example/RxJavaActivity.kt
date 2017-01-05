package com.github.judrummer.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.github.judrummer.jxadapter.JxAdapter
import com.github.judrummer.jxadapter.JxViewHolder
import com.github.judrummer.jxadapter_rxjava.rx_jxAdapter
import com.taskworld.kxandroid.format
import kotlinx.android.synthetic.main.activity_simple.*
import kotlinx.android.synthetic.main.item_invoice_footer.view.*
import kotlinx.android.synthetic.main.item_invoice_header.view.*
import kotlinx.android.synthetic.main.item_invoice_item.view.*
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription

class RxJavaActivity : AppCompatActivity() {

    val itemsObservable = PublishSubject.create<List<Any>>()
    val subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        setSupportActionBar(toolbar)
        rvExample.apply {
            layoutManager = LinearLayoutManager(this@RxJavaActivity)
            subscriptions.add(rx_jxAdapter(itemsObservable,
                    JxViewHolder<InvoiceHeaderViewData>(R.layout.item_invoice_header) { position, item ->
                        itemView.apply {
                            tvItemInvoiceHeaderNumber.text = item.invoiceNumber
                        }
                    },
                    JxViewHolder<InvoiceItemViewData>(R.layout.item_invoice_item) { position, item ->
                        itemView.apply {
                            tvItemInvoiceItemProductName.text = item.productName
                            tvItemInvoiceItemQuantity.text = "x ${item.quantity}"
                            tvItemInvoiceItemPrice.text = "$ ${item.price.format(2)}"
                            tvItemInvoiceItemTotal.text = "$ ${item.totalAmount.format(2)}"
                        }
                    },
                    JxViewHolder<InvoiceFooterViewData>(R.layout.item_invoice_footer) { position, item ->
                        itemView.apply {
                            tvItemInvoiceFooterTotal.text = "$ ${item.total.format(2)}"

                        }
                    },
                    JxViewHolder<InvoiceSpaceViewData>(R.layout.item_invoice_space) { position, item ->

                    })
            )
        }

        srlExample.setOnRefreshListener {
            fetchData()
            srlExample.isRefreshing = false
        }

        fetchData()
    }

    fun fetchData() {
        itemsObservable.onNext(fetchInvoiceData().toViewDataList())
    }

}


