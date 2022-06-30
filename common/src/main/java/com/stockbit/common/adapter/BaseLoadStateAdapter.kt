package com.stockbit.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stockbit.common.databinding.ItemLoadingBinding


class BaseLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<BaseLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = LoadStateViewHolder(
        ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(
        holder: LoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetryLoadingtask.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                println("TAG LOADSTATE = $loadState")
                pbTask.isVisible = loadState is LoadState.Loading
                btnRetryLoadingtask.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}