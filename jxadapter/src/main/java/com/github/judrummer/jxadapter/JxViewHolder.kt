package com.github.judrummer.jxadapter

import android.view.View

/**
 * Created by judrummer on 23/12/2559.
 */

class JxViewHolder<T : Any>(val itemLayoutId: Int, val itemType: Class<T>, val bindHolder: (view: View, position: Int, item: T) -> Unit)

inline fun <reified T : Any> JxViewHolder(itemLayoutId: Int, noinline bindHolder: (view: View, position: Int, item: T) -> Unit): JxViewHolder<T> {
    return JxViewHolder(itemLayoutId, T::class.java, bindHolder)
}

