package com.github.judrummer.jxadapter_rxjava

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.judrummer.jxadapter.JxAdapter
import com.github.judrummer.jxadapter.JxViewHolder
import rx.Observable
import rx.Subscription

/**
 * Created by judrummer on 23/12/2559.
 */

fun RecyclerView.rx_jxAdapter(observable: Observable<List<Any>>, vararg jxHolderList: JxViewHolder<*>): Subscription {
    return rx_jxAdapter(observable, jxHolderList.toList())
}

fun RecyclerView.rx_jxAdapter(observable: Observable<List<Any>>,
                                jxHolderList: List<JxViewHolder<*>>): Subscription {
    val jxAdapter = JxAdapter(jxHolderList)
    adapter = jxAdapter
    return observable.subscribe {
        jxAdapter.items = it
    }
}