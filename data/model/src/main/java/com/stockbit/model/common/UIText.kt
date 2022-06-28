package com.stockbit.model.common

import android.content.Context
import androidx.annotation.StringRes

sealed class UIText {
    data class DynamicString(val value: String): UIText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ): UIText()

//    @Composable
//    fun asString(): String {
//        return when(this) {
//            is DynamicString -> value
//            is StringResource -> stringResource(resId, args)
//        }
//    }

    fun asString(mContext: Context): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> mContext.getString(resId, args)
        }
    }
}
