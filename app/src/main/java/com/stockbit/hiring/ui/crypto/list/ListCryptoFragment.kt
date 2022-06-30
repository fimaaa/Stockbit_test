package com.stockbit.hiring.ui.crypto.list

import androidx.core.view.isVisible
import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.hiring.databinding.FragmentListcryptoBinding
import com.stockbit.hiring.R
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.net.HttpURLConnection


class ListCryptoFragment: BaseFragment<FragmentListcryptoBinding>(
    R.layout.fragment_listcrypto
), ListCryptoNavigator {
    private val viewModel: ListCryptoViewModel by inject()

    private var skeleton: Skeleton? = null

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
        adapter.addLoadStateListener {
            lifecycleScope.launch {
                adapter.loadStateFlow.collectLatest { loadState ->
                    binding.apply {
                        blanklayout.isVisible = false
                        rcvCrypto.isVisible = true
                        when  {
                            loadState.refresh is LoadState.Loading -> if(adapter.itemCount < 1) skeleton?.showSkeleton()
                            loadState.refresh is LoadState.NotLoading -> {
                                skeleton?.showOriginal()
                                if (loadState.refresh is LoadState.NotLoading &&
                                    loadState.append.endOfPaginationReached &&
                                    adapter.itemCount < 1
                                ) {
                                    rcvCrypto.isVisible = false
                                    blanklayout.isVisible = true
                                    blanklayout.setType(HttpURLConnection.HTTP_NO_CONTENT)
                                }
                            }
                            loadState.mediator?.refresh is LoadState.Error -> {
                                skeleton?.showOriginal()
                                val throwable = (loadState.refresh as LoadState.Error).error
                                rcvCrypto.isVisible = adapter.itemCount > 1
                                blanklayout.isVisible = adapter.itemCount < 1
                                blanklayout.setType(
                                    throwable.toThrowableCode(),
                                    throwable.toThrowableMessage().asString(requireContext())
                                )
                                blanklayout.setOnClick(getString(R.string.retry)) {
                                    adapter.retry()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onReadyAction() {
        binding.rcvCrypto.adapter = adapter
        skeleton = binding.rcvCrypto.applySkeleton(R.layout.item_crypto)
        skeleton?.showShimmer = true
        setToolbar(
            leftImage = R.drawable.ic_menu,
            centerImage = R.drawable.ic_logo_stockbit,
            rightImage = R.drawable.ic_file_save
        )
        setToolbarRight {
            viewModel.getLocal()
        }
    }

    override fun onFragmentDestroyed() {
        skeleton = null
    }
}