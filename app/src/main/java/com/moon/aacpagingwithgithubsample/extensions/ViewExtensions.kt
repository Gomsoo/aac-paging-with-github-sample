package com.moon.aacpagingwithgithubsample.extensions

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.ColorInt

fun ViewGroup.inflate(layoutId: Int): View =
    LayoutInflater.from(this.context).inflate(layoutId, this, false)

fun ProgressBar.setIndeterminateColor(@ColorInt color: Int) {
    indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
}
