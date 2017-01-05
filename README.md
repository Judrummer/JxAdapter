
[![](https://jitpack.io/v/judrummer/JxAdapter.svg)](https://jitpack.io/#judrummer/JxAdapter)
[ ![Kotlin](https://img.shields.io/badge/Kotlin-1.0.5-blue.svg)](http://kotlinlang.org)
[![Build Status](https://travis-ci.org/Judrummer/JxAdapter.svg?branch=master)](https://travis-ci.org/judrummer/JxAdapter)

# JxAdapter
The Easiest RecyclerAdapter for Android Kotlin

## Installation

### Gradle

``` Groovy
repositories {
   maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.judrummer.jxadapter:jxadapter:0.1.1' 
    compile 'com.github.judrummer.jxadapter:jxadapter-rxjava:0.1.1' 
}
```

## How to use (Multitype ViewHolder)

![alt tag](https://zippy.gfycat.com/ColorlessElderlyIndianskimmer.gif)

### Implement ViewData for each layout

``` Kotlin
//item_invoice_header.xml
data class InvoiceHeaderViewData(val invoiceNumber: String)

//item_invoice_item.xml
data class InvoiceItemViewData(val productName: String, val price: Double, val quantity: Int, val totalAmount: Double)

//item_invoice_footer.xml
data class InvoiceFooterViewData(val total: Double)

//item_invoice_space.xml
data class InvoiceSpaceViewData(val none: String = "")

```

### Implement Adapter

``` Kotlin
    val jxAdapter = JxAdapter(
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

```

### Assign Adapter to RecyclerView
``` Kotlin
  rvExample.apply {
            layoutManager = LinearLayoutManager(this@SimpleActivity)
            adapter = jxAdapter
        }
  
```

### Assign Items Value (Auto Trigger notifyDatasetChaged)
``` Kotlin
jxAdapter.items = listOf(....) // Your Data
```


### Extension For RxJava 
``` Kotlin
 val itemsObservable:Observable<List<Any>> = ....
 val subscriptions = CompositeSubscription()
 
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
```
