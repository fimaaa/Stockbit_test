package com.stockbit.hiring.ui.crypto.list

import androidx.core.view.isVisible
import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.hiring.databinding.FragmentListcryptoBinding
import com.stockbit.hiring.R
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
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
import kotlinx.coroutines.cancel
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

    private var adapter: AdapterCrypto? = AdapterCrypto {
        showSnackbar(it.coinInfo.fullName, Snackbar.LENGTH_LONG)
    }.apply {
        addLoadStateListener { loadState ->
            binding.apply {
                blanklayout.isVisible = false
//                            rcvCrypto.isVisible = true
                when  {
                    loadState.refresh is LoadState.Loading -> {
                        lifecycleScope.launch{
                            if(itemCount < 1 && !viewModel.isDataExist()) skeleton?.showSkeleton()
                        }
                    }
                    loadState.refresh is LoadState.NotLoading -> {
                        if(skeleton?.isSkeleton() == true) skeleton?.showOriginal()
                        if (loadState.append.endOfPaginationReached &&
                            itemCount < 1
                        ) {
                            blanklayout.isVisible = true
                            blanklayout.setType(java.net.HttpURLConnection.HTTP_NO_CONTENT)
                        }
                    }
                    loadState.mediator?.refresh is LoadState.Error -> {
                        if(skeleton?.isSkeleton() == true)skeleton?.showOriginal()
                        val throwable = (loadState.refresh as LoadState.Error).error
                        blanklayout.isVisible = itemCount < 1
                        blanklayout.setType(
                            throwable.toThrowableCode(),
                            throwable.toThrowableMessage().asString(requireContext())
                        )
                        blanklayout.setOnClick(getString(com.stockbit.hiring.R.string.retry)) {
                            retry()
                        }
                    }
                }
            }
        }
    }

    override fun onInitialization() {
    }

    override fun onObserveAction() {
        observe(viewModel.listCrypto) {
            adapter?.submitData(lifecycle, it)
        }
        observe(viewModel.refresh) {
            adapter?.refresh()
            binding.swiperefreshCrypto.isRefreshing = false
        }
        observe(viewModel.newMessage) {
            adapter?.snapshot()?.items?.forEachIndexed { position, item ->
                if(item.coinInfo.name == it.symbol){
                    item.moneyData.coinValue.change = it.topTierVolume?:0.0
                    adapter?.notifyItemChanged(position)
                }
            }
        }
    }

    override fun onReadyAction() {
        setToolbar(
            leftImage = R.drawable.ic_menu,
            centerImage = R.drawable.ic_logo_stockbit,
            rightImage = R.drawable.ic_file_save
        )
        binding.rcvCrypto.adapter = adapter?.withLoadStateHeaderAndFooter(
            header = BaseLoadStateAdapter { adapter?.retry() },
            footer = BaseLoadStateAdapter { adapter?.retry() }
        )
        skeleton = binding.rcvCrypto.applySkeleton(R.layout.item_crypto)
        skeleton?.showShimmer = true
        binding.swiperefreshCrypto.setOnRefreshListener {
            viewModel.refresh()
        }
        viewModel.refreshList.apply {
            value = !(value?:false)
        }
        binding.tvTitleCrypto.setOnClickListener {
//            val data = adapter?.snapshot()?.firstOrNull { info ->
//                info?.coinInfo?.name == "BTC"
//            }
//            data?.coinInfo?.name = "BERUBAH"
            adapter?.snapshot()?.items?.forEachIndexed { position, item ->
                if(item.coinInfo.name == "BTC"){
                    item.coinInfo.name = "CHANGED"
                    adapter?.notifyItemChanged(position)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.reConnectingWebSocket()
        binding.swiperefreshCrypto.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        viewModel.disconnectWebSocket()
        binding.swiperefreshCrypto.isEnabled = false
    }

    override fun onFragmentDestroyed() {
        viewModel.refresh.removeObservers(viewLifecycleOwner)
        lifecycleScope.cancel()
        skeleton = null
        adapter = null
    }

}