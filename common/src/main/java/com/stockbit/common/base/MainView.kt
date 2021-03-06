package com.stockbit.common.base

import com.stockbit.model.common.UIText

interface MainView {
    fun setToolbar(
        rightImage: Int,
        leftImage: Int? = null,
        centerTitle: UIText? = null,
        centerImage: Int? = null,
    )

    fun setToolbarLeft(listener: (()-> Unit)?)
    fun setToolbarRight(listener: ()-> Unit)

}