package com.stockbit.model.common

import com.google.gson.annotations.SerializedName

/**
 * Generic data class for parsing response
 * M for message type string, D for data type any
 */

data class BaseResponse<M,D>(
    @SerializedName(value = "code_message")
    val codeMessage: M,
    @SerializedName(value = "code")
    val code: Int = 0,
    @SerializedName(value = "data")
    val data: D,
    @SerializedName(value = "code_type")
    val codeType: String?,
    @SerializedName(value = "pagination")
    val pagination: BasePagination?
)