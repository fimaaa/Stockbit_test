package com.stockbit.hiring.ui.crypto.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stockbit.hiring.databinding.ItemCryptoBinding
import com.stockbit.model.crypto.ResponseListCryptoInfo

class AdapterCrypto(
    private val listener: (ResponseListCryptoInfo) -> Unit
) : PagingDataAdapter<ResponseListCryptoInfo, AdapterCrypto.ViewHolder>(TASK_COMPARATOR) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemCryptoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    inner class ViewHolder(private val binding: ItemCryptoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exampleData: ResponseListCryptoInfo) {
            binding.data = exampleData
            binding.root.setOnClickListener {
                listener.invoke(exampleData)
            }
        }
    }

    companion object {
        private val TASK_COMPARATOR = object : DiffUtil.ItemCallback<ResponseListCryptoInfo>() {
            override fun areItemsTheSame(
                oldItem: ResponseListCryptoInfo,
                newItem: ResponseListCryptoInfo
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ResponseListCryptoInfo,
                newItem: ResponseListCryptoInfo
            ): Boolean = oldItem == newItem
        }
    }
}