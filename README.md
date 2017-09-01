
[![](https://jitpack.io/v/judrummer/JxAdapter.svg)](https://jitpack.io/#judrummer/JxAdapter)
[ ![Kotlin](https://img.shields.io/badge/Kotlin-1.1.4-blue.svg)](http://kotlinlang.org)
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
    compile 'com.github.judrummer.jxadapter:jxadapter:1.0.0'
}
```

## How to use (Multitype ViewHolder)

![alt tag](https://zippy.gfycat.com/ColorlessElderlyIndianskimmer.gif)

### Implement ViewData for each layout by using JxItem interface

``` Kotlin
//item_invoice_header.xml
data class InvoiceHeaderViewData(val invoiceNumber: String) : JxItem

//item_invoice_item.xml
data class InvoiceItemViewData(val productName: String, val price: Double, val quantity: Int, val totalAmount: Double) : JxItem

//item_invoice_footer.xml
data class InvoiceFooterViewData(val total: Double) : JxItem

//item_invoice_space.xml
data class InvoiceSpaceViewData(val none: String = "") : JxItem

```

### Implement ViewHolder (use android extension experimental)

``` Kotlin
typealias ItemClick = (item: InvoiceItemViewData) -> Unit

class InvoiceHeaderViewHolder(parent: ViewGroup) : JxViewHolder<InvoiceHeaderViewData>(parent, R.layout.item_invoice_header) {

    override fun bind(item: InvoiceHeaderViewData) {
        tvItemInvoiceHeaderNumber.text = item.invoiceNumber
    }

}

class InvoiceItemViewHolder(parent: ViewGroup, initializer: (InvoiceItemViewHolder.() -> Unit)? = null) : JxViewHolder<InvoiceItemViewData>(parent, R.layout.item_invoice_item) {

    init {
        initializer?.invoke(this)
    }

    var onNameClick: (ItemClick)? = null
    var onTotalClick: (ItemClick)? = null

    override fun bind(item: InvoiceItemViewData) {
        tvItemInvoiceItemProductName.text = item.productName
        tvItemInvoiceItemProductName.setOnClickListener {
            onNameClick?.invoke(item)
        }
        tvItemInvoiceItemQuantity.text = "x ${item.quantity}"
        tvItemInvoiceItemPrice.text = "$ ${item.price.format(2)}"
        tvItemInvoiceItemTotal.text = "$ ${item.totalAmount.format(2)}"
        tvItemInvoiceItemTotal.setOnClickListener {
            onTotalClick?.invoke(item)
        }
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
```

### Implement Adapter

``` Kotlin
   val jxAdapter = JxAdapter {
          viewHolder(::InvoiceHeaderViewHolder)
          viewHolder {
              InvoiceItemViewHolder(it) {
                  onNameClick = this@SimpleActivity::onNameClick
                  onTotalClick = { item ->
                      toast("onTotalClick $item")
                  }
              }
          }
          viewHolder(::InvoiceFooterViewHolder)
          viewHolder(::InvoiceSpaceViewHolder)
      }

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
jxAdapter.items = listOf<JxItem>(....) // Your Data
```
### Use DiffUtil by set JxDiffUtil (Optional)
``` Kotlin
jxAdapter.jxDiffUtil = JxDiffUtil() 
```
JxDiffUtil is open class. you can extend to override function areItemsTheSame and areContentsTheSame.
``` Kotlin
open class JxDiffUtil {
    open val areItemsTheSame: (JxItem, JxItem) -> Boolean = { oldItem, newItem ->
        oldItem == newItem
    }
    open val areContentsTheSame: (JxItem, JxItem) -> Boolean = { oldItem, newItem ->
        oldItem == newItem
    }
}
```

### Licenses
JxAdapter is released under the [MIT](http://opensource.org/licenses/MIT) license.
