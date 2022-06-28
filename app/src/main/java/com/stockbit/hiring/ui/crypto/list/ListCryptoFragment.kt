package com.stockbit.hiring.ui.crypto.list

import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.hiring.databinding.FragmentListcryptoBinding
import com.stockbit.hiring.R
import androidx.fragment.app.viewModels


class ListCryptoFragment: BaseFragment<FragmentListcryptoBinding>(
    R.layout.fragment_listcrypto
), ListCryptoNavigator {
    private val viewmodel: ListCryptoViewModel by viewModels()

    override fun getViewModel(): BaseViewModel = viewmodel

    override fun onReadyAction() {
    }
}