
[![](https://jitpack.io/v/judrummer/JxAdapter.svg)](https://jitpack.io/#judrummer/JxAdapter)
[ ![Kotlin](https://img.shields.io/badge/Kotlin-1.0.6-blue.svg)](http://kotlinlang.org)
[![Build Status](https://travis-ci.org/Judrummer/JxAdapter.svg?branch=master)](https://travis-ci.org/judrummer/JxAdapter)

# JxAdapter
The easiest RecyclerAdapter for Android Kotlin

## Installation

### Gradle

``` Groovy
repositories {
   maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.judrummer.jxadapter:jxadapter:0.3.2'
    compile 'com.github.judrummer.jxadapter:jxadapter-rxjava:0.3.2'
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
### Use DiffUtil by set JxDiffUtil (Optional)
``` Kotlin
jxAdapter.jxDiffUtil = JxDiffUtil() 
```
JxDiffUtil is open class. you can extend to override function areItemsTheSame and areContentsTheSame.
``` Kotlin
open class JxDiffUtil {
    open val areItemsTheSame: (Any, Any) -> Boolean = { oldItem, newItem ->
        oldItem == newItem
    }
    open val areContentsTheSame: (Any, Any) -> Boolean = { oldItem, newItem ->
        oldItem == newItem
    }
}
```

### Extension For RxJava (Inspired by rx_itemWith() from ReactiveAndroid)
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
