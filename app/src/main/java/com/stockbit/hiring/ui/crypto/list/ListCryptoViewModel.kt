package com.stockbit.hiring.ui.crypto.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.Gson
import com.stockbit.common.base.BaseViewModel
import com.stockbit.common.extension.safeApiCall
import com.stockbit.common.extension.safeApiCallIndependent
import com.stockbit.hiring.ui.crypto.socket.SocketCryptoViewModel
import com.stockbit.model.crypto.ResponseListCryptoInfo
import com.stockbit.model.socket.BitcoinTicker
import com.stockbit.remote.TopListDataSource
import com.stockbit.repository.TopListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.net.ssl.SSLSocketFactory

class ListCryptoViewModel(
    webSocketURI: URI,
    private val topListRepository: TopListRepository
): BaseViewModel() {

    private lateinit var webSocketClient: WebSocketClient

    init {
        webSocketClient =  object : WebSocketClient(webSocketURI) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(SocketCryptoViewModel.TAG, "onOpen WEBSOCKET")
                reSubscribe()
            }

            override fun onMessage(message: String?) {
                Log.d(SocketCryptoViewModel.TAG, "onMessage MANUAL: $message")
                message?.let {
                    val bitcoin = Gson().fromJson(it, BitcoinTicker::class.java)
                    newMessage.postValue(bitcoin)
                }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(SocketCryptoViewModel.TAG, "onClose")
                unsubscribe()
            }

            override fun onError(ex: Exception?) {
                Log.e(SocketCryptoViewModel.TAG, "onError: ${ex?.message}")
            }
        }
        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient.setSocketFactory(socketFactory)
        connectWebSocket()
    }

    val newMessage = MutableLiveData<BitcoinTicker>()
    val refreshList = MutableLiveData<Boolean>()
    val refresh = MutableLiveData<Boolean>()
    val listCrypto: LiveData<PagingData<ResponseListCryptoInfo>> = refreshList.switchMap {
        topListRepository.getPagingTopTier(webSocketClient).cachedIn(viewModelScope)
    }



    suspend fun isDataExist(): Boolean = topListRepository.checkDaoExist()

    fun refresh() {
        viewModelScope.launch {
            topListRepository.clearAllData()
            refreshList.postValue(!(refreshList.value?:false))
            refresh.postValue(!(refresh.value?:false))
        }
    }

    private fun connectWebSocket() {
        if(!webSocketClient.isOpen) {
            webSocketClient.connect()
        }
    }

    fun reConnectingWebSocket() {
        if(webSocketClient.isClosed) {
            webSocketClient.connect()
        }
    }

    fun disconnectWebSocket() {
        if(webSocketClient.isOpen) {
            webSocketClient.close()
        }
    }

    private fun reSubscribe() {
        println("TAG reSubscribe")
        viewModelScope.launch {
            val list = arrayListOf<String>()
            topListRepository.getListTopTierLocal().forEach {
                list.add("\"21~"+it.coinInfo.name+"\"")
            }
            webSocketClient.send(
                "{\n" +
                        "    \"action\": \"SubAdd\",\n" +
                        "    \"subs\": $list" +
                        "}"
            )
        }
    }

    private fun unsubscribe() {
        safeApiCall {
            val list = arrayListOf<String>()
            topListRepository.getListTopTierLocal().forEach {
                list.add("\"21~"+it.coinInfo.name+"\"")
            }
            webSocketClient.send(
                "{\n" +
                        "    \"action\": \"SubRemove\",\n" +
                        "    \"subs\": $list" +
                        "}"
            )
        }
    }

    companion object {
        const val TAG = "Coinbase"
    }
}