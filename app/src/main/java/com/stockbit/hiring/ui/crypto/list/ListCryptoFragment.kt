package com.stockbit.hiring.ui.crypto.list

import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.hiring.databinding.FragmentListcryptoBinding
import com.stockbit.hiring.R
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.stockbit.common.adapter.BaseLoadStateAdapter
import com.stockbit.common.extension.showSnackbar
import com.stockbit.common.utils.Event
import com.stockbit.hiring.ui.crypto.adapter.AdapterCrypto
import com.stockbit.model.common.UIText


class ListCryptoFragment: BaseFragment<FragmentListcryptoBinding>(
    R.layout.fragment_listcrypto
), ListCryptoNavigator {
    private val viewmodel: ListCryptoViewModel by viewModels()

    override fun getViewModel(): BaseViewModel = viewmodel

    private val adapter = AdapterCrypto {
        showSnackbar(it.name, Snackbar.LENGTH_LONG)
    }.apply {
        withLoadStateHeaderAndFooter(
            header = BaseLoadStateAdapter { this.retry() },
            footer = BaseLoadStateAdapter { this.retry() }
        )
    }

    override fun onReadyAction() {
        setToolbar(
            leftImage = R.drawable.ic_menu,
            centerImage = R.drawable.ic_logo_stockbit,
            rightImage = R.drawable.ic_file_save
        )
    }
}