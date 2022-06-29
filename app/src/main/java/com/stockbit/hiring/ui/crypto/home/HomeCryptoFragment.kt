package com.stockbit.hiring.ui.crypto.home

import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.hiring.R
import com.stockbit.hiring.databinding.FragmentHomeCryptoBinding


class HomeCryptoFragment: BaseFragment<FragmentHomeCryptoBinding>(
    R.layout.fragment_home_crypto
) {
    private val viewModel: HomeCryptoViewModel by viewModels()

    override fun getViewModel(): BaseViewModel = viewModel

    private val navController: NavController by lazy { requireActivity().findNavController(R.id.nav_host_crypto) }

    override fun onReadyAction() {
        NavigationUI.setupWithNavController(binding.bottomnavCrypto, navController)
    }
}