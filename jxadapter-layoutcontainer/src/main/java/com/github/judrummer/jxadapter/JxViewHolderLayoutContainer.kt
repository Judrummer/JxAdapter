package com.github.judrummer.jxadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer

abstract class JxViewHolderLayoutContainer<in T : JxItem>(
        parent: ViewGroup,
        layoutResId: Int,
        override val containerView: View = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false))
    : JxViewHolder<T>(parent, layoutResId, containerView), LayoutContainer
