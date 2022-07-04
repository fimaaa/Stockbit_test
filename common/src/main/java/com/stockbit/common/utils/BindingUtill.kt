package com.stockbit.common.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("textPrice")
fun TextView.setPriceText(value: Double?) {
    text =
        CurrencyUtils.simpleCovertCurrency(context, value.toString())
}