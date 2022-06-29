package com.stockbit.model.common

import com.google.gson.annotations.SerializedName

/**
 * Generic data class for parsing response
 * M for message type string, D for data type any
 */

data class BaseResponse<M,D>(
    @SerializedName(value = "Message")
    val codeMessage: M,
    @SerializedName(value = "Type")
    val code: Int = 0,
    @SerializedName(value = "Data")
    val data: D,
    @SerializedName(value = "MetaData")
    val pagination: BasePagination? = null
)