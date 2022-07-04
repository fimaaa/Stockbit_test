package com.stockbit.hiring.ui.crypto.socket

import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.common.extension.observe
import com.stockbit.hiring.R
import com.stockbit.hiring.databinding.FragmentCryptoSocketBinding
import org.koin.android.ext.android.inject

class SocketCryptoFragment: BaseFragment<FragmentCryptoSocketBinding>(
    R.layout.fragment_crypto_socket
) {
    private val viewModel: SocketCryptoViewModel by inject()
    override fun getViewModel(): BaseViewModel = viewModel

    override fun onObserveAction() {
        observe(viewModel.observablePriceText) { text ->
            binding.tvPriceSocket.text = "1 BTC: $text €"
        }
    }

    override fun onReadyAction() {
        binding.btnOpen.setOnClickListener {
            binding.tvPriceSocket.text = "Opened Socket"
            viewModel.connectWebSocket()
        }
        binding.btnClose.setOnClickListener {
            binding.tvPriceSocket.text = "Closed Socket"
            viewModel.disconnectWebSocket()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.connectWebSocket()
    }

    override fun onPause() {
        super.onPause()
        viewModel.disconnectWebSocket()
    }
}