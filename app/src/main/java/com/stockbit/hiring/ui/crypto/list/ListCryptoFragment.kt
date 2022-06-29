package com.stockbit.hiring.ui.crypto.list

import androidx.core.view.isVisible
import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.hiring.databinding.FragmentListcryptoBinding
import com.stockbit.hiring.R
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.snackbar.Snackbar
import com.stockbit.common.adapter.BaseLoadStateAdapter
import com.stockbit.common.extension.observe
import com.stockbit.common.extension.showSnackbar
import com.stockbit.common.utils.Event
import com.stockbit.hiring.ui.crypto.adapter.AdapterCrypto
import com.stockbit.model.common.UIText
import com.stockbit.remote.extension.toThrowableCode
import com.stockbit.remote.extension.toThrowableMessage
import org.koin.android.ext.android.inject
import java.net.HttpURLConnection


class ListCryptoFragment: BaseFragment<FragmentListcryptoBinding>(
    R.layout.fragment_listcrypto
), ListCryptoNavigator {
    private val viewModel: ListCryptoViewModel by inject()

    private val skeleton: Skeleton by lazy { binding.rcvCrypto.applySkeleton(R.layout.item_crypto) }

    override fun getViewModel(): BaseViewModel = viewModel

    private val adapter = AdapterCrypto {
        showSnackbar(it.coinInfo.fullName, Snackbar.LENGTH_LONG)
    }.apply {
        withLoadStateHeaderAndFooter(
            header = BaseLoadStateAdapter { this.retry() },
            footer = BaseLoadStateAdapter { this.retry() }
        )
    }

    override fun onObserveAction() {
        observe(viewModel.listCrypto) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                blanklayout.isVisible = false
                rcvCrypto.isVisible = true
                when (loadState.source.refresh) {
                    is LoadState.Loading -> {
                        skeleton?.showSkeleton()
                    }
                    is LoadState.Error -> {
                        skeleton?.showOriginal()
                        val throwable = (loadState.source.refresh as LoadState.Error).error
                        rcvCrypto.isVisible = false
                        blanklayout.isVisible = true
                        blanklayout.setType(
                            throwable.toThrowableCode(),
                            throwable.toThrowableMessage().asString(requireContext())
                        )
                        blanklayout.setOnClick(getString(R.string.retry)) {
                            adapter.retry()
                        }
                    }
                    is LoadState.NotLoading -> {
                        skeleton?.showOriginal()
                        if (loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached &&
                            adapter.itemCount < 1
                        ) {
                            rcvCrypto.isVisible = false
                            blanklayout.isVisible = true
                            blanklayout.setType(HttpURLConnection.HTTP_NO_CONTENT)
                        }
                    }
                }
            }
        }
    }

    override fun onReadyAction() {
        binding.rcvCrypto.adapter = adapter
        skeleton.showShimmer = true
        setToolbar(
            leftImage = R.drawable.ic_menu,
            centerImage = R.drawable.ic_logo_stockbit,
            rightImage = R.drawable.ic_file_save
        )
    }
}