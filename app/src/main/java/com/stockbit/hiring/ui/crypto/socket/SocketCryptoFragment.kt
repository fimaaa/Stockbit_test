package com.stockbit.hiring.ui.crypto.socket

import androidx.fragment.app.viewModels
import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.hiring.R
import com.stockbit.hiring.databinding.FragmentCryptoSocketBinding

class SocketCryptoFragment: BaseFragment<FragmentCryptoSocketBinding>(
    R.layout.fragment_crypto_socket
) {

    private val viewModel: SocketCryptoViewModel by viewModels()
    override fun getViewModel(): BaseViewModel = viewModel

    override fun onReadyAction() {
    }
}