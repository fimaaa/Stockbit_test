package com.stockbit.hiring.ui.crypto.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stockbit.hiring.databinding.ItemCryptoBinding
import com.stockbit.model.CryptoData

class AdapterCrypto(
    private val listener: (CryptoData) -> Unit
) : PagingDataAdapter<CryptoData, AdapterCrypto.ViewHolder>(TASK_COMPARATOR) {

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


    inner class ViewHolder(private val binding: ItemCryptoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { listener.invoke(it) }
                }
            }
        }

        fun bind(exampleData: CryptoData) {
            binding.data = exampleData
        }
    }

    companion object {
        private val TASK_COMPARATOR = object : DiffUtil.ItemCallback<CryptoData>() {
            override fun areItemsTheSame(
                oldItem: CryptoData,
                newItem: CryptoData
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CryptoData,
                newItem: CryptoData
            ): Boolean = oldItem == newItem
        }
    }
}