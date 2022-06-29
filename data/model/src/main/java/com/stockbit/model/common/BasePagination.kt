package com.stockbit.model.common

import com.google.gson.annotations.SerializedName

data class BasePagination (
    @SerializedName(value = "current_page")
    val currentPage: Int = 0,
    @SerializedName(value = "per_page")
    val perPage: Int = 0,
    @SerializedName(value = "Count")
    val totalPage: Int = 0,
    @SerializedName(value = "total_records")
    val totalRecords: Int = 0,
    @SerializedName(value = "link_parameter")
    val linkParameter: Link = Link(),
    @SerializedName(value = "links")
    val links: Link = Link()

) {
    data class Link(
        @SerializedName(value = "first")
        val first: String = "",
        @SerializedName(value = "last")
        val last: String = "",
        @SerializedName(value = "next")
        val next: String = "",
        @SerializedName(value = "previous")
        val previous: String = ""
    )
}