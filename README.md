
[![](https://jitpack.io/v/judrummer/JxAdapter.svg)](https://jitpack.io/#judrummer/JxAdapter)
[ ![Kotlin](https://img.shields.io/badge/Kotlin-1.2.61-blue.svg)](http://kotlinlang.org)
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
    implementation 'com.github.judrummer.jxadapter:jxadapter:1.0.0'
    
    //If you want to use kotlin android extention LayoutContainer experimental
    implementation 'com.github.judrummer.jxadapter:jxadapter-layoutcontainer:1.0.0'
}

//If you want to use kotlin android extention LayoutContainer experimental
androidExtensions {
    experimental = true
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

### Implement ViewHolder (JxViewHolder for normal, JxViewHolderLayoutContainer for android experimental extension)

``` Kotlin
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

```

### Implement Adapter

``` Kotlin
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
jxAdapter.items = listOf<Any>(....) // Your Data
```
### Use DiffUtil by set JxDiffUtil
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

### Licenses
JxAdapter is released under the [MIT](http://opensource.org/licenses/MIT) license.
