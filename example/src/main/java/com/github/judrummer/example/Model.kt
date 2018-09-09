package com.github.judrummer.example

import java.util.*


//View Data
data class InvoiceHeaderViewData(val invoiceNumber: String)

data class InvoiceItemViewData(val productName: String, val price: Double, val quantity: Int, val totalAmount: Double)

data class InvoiceFooterViewData(val total: Double)

data class InvoiceSpaceViewData(val none: String = "")

//Data Model
data class InvoiceItem(val productName: String, val price: Double, val quantity: Int)

data class Invoice(val invoiceNumber: String, val items: List<InvoiceItem>)


//Converter
fun List<Invoice>.toViewDataList(): List<Any> {
    val viewDataList = mutableListOf<Any>()
    forEach { (invoiceNumber, items) ->
        viewDataList.add(InvoiceHeaderViewData(invoiceNumber))
        var invoiceTotal = 0.0
        items.forEach { (productName, price, quantity) ->
            val totalItemAmount = price * quantity
            invoiceTotal += totalItemAmount
            viewDataList.add(InvoiceItemViewData(productName, price, quantity, totalItemAmount))
        }
        viewDataList.add(InvoiceFooterViewData(invoiceTotal))
        viewDataList.add(InvoiceSpaceViewData())
    }
    return viewDataList
}


//Random fetch Data
fun fetchInvoiceData(): List<Invoice> {
    val random = Random()
    val invoice1 = Invoice("INV001", listOf(
            InvoiceItem("GTX1060", random.nextDouble() * 10000, 1),
            InvoiceItem("Intel Core i7 6700", random.nextDouble() * 10000, 1),
            InvoiceItem("Corsair ram 8 gb", random.nextDouble() * 1000, 2)
    ))

    val invoice2 = Invoice("INV002", listOf(
            InvoiceItem("GTX970", random.nextDouble() * 10000, 1),
            InvoiceItem("Intel Core i5 6400", random.nextDouble() * 10000, 1),
            InvoiceItem("Kingstons ram 16 gb", random.nextDouble() * 1000, 2)
    ))

    val invoice3 = Invoice("INV003", listOf(
            InvoiceItem("GTX750", random.nextDouble() * 10000, 1),
            InvoiceItem("Intel Core i5 6600", random.nextDouble() * 10000, 1),
            InvoiceItem("Kingtons  ram 4 gb", random.nextDouble() * 1000, 3)
    ))

    val invoice4 = Invoice("INV004", listOf(
            InvoiceItem("Huawei Mate 9 ", random.nextDouble() * 1000, 1),
            InvoiceItem("Sony Xperia XZ", random.nextDouble() * 1000, 1),
            InvoiceItem("Google Pixel XL", random.nextDouble() * 1000, 1)
    ))


    val invoice5 = Invoice("INV005", listOf(
            InvoiceItem("Sony XB70AP", random.nextDouble() * 1000, 1),
            InvoiceItem("Sony XB950BT", random.nextDouble() * 1000, 1),
            InvoiceItem("64 Audio A3", random.nextDouble() * 1000, 1)
    ))


    return listOf(invoice1, invoice2, invoice3, invoice4, invoice5)
}


