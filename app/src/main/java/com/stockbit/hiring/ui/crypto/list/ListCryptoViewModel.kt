package com.stockbit.hiring.ui.crypto.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stockbit.common.base.BaseViewModel
import com.stockbit.common.extension.safeApiCall
import com.stockbit.common.extension.safeApiCallIndependent
import com.stockbit.model.crypto.ResponseListCryptoInfo
import com.stockbit.remote.TopListDataSource
import com.stockbit.repository.TopListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ListCryptoViewModel(
    private val topListRepository: TopListRepository
): BaseViewModel() {

    val refreshList = MutableLiveData<Boolean>()
    val refresh = MutableLiveData<Boolean>()
    val listCrypto: LiveData<PagingData<ResponseListCryptoInfo>> = refreshList.switchMap {
        topListRepository.getPagingTopTier().cachedIn(viewModelScope)
    }

    suspend fun isDataExist(): Boolean = topListRepository.checkDaoExist()

    fun refresh() {
        safeApiCall {
            topListRepository.clearAllData()
            refreshList.postValue(!(refreshList.value?:false))
            refresh.postValue(!(refresh.value?:false))
        }

    }
}