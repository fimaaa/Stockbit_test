package com.stockbit.hiring.ui.crypto.socket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.stockbit.common.base.BaseViewModel
import com.stockbit.model.socket.BitcoinTicker
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.net.ssl.SSLSocketFactory

class SocketCryptoViewModel(
    webSocketURI: URI
): BaseViewModel() {


    private val webSocketClient = object : WebSocketClient(webSocketURI) {
        override fun onOpen(handshakedata: ServerHandshake?) {
            Log.d(TAG, "onOpen")
            subscribe()
        }

        override fun onMessage(message: String?) {
            Log.d(TAG, "onMessage MANUAL: $message")
            message?.let {
                val bitcoin = Gson().fromJson(it, BitcoinTicker::class.java)
                _observablePriceText.postValue(bitcoin?.topTierVolume.toString())
            }
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            Log.d(TAG, "onClose")
            unsubscribe()
        }

        override fun onError(ex: Exception?) {
            Log.e(TAG, "onError: ${ex?.message}")
        }
    }

    init {
        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient.setSocketFactory(socketFactory)
    }

    private val _observablePriceText = MutableLiveData<String>()
    val observablePriceText: LiveData<String>
        get() = _observablePriceText

    fun connectWebSocket() {
        if(!webSocketClient.isOpen) {
            webSocketClient.connect()
        }

    }

    fun disconnectWebSocket() {
        if(webSocketClient.isOpen) {
            webSocketClient.close()
        }
    }

    private fun subscribe() {
//        webSocketClient.send(
//            "{\n" +
//                    "    \"type\": \"subscribe\",\n" +
//                    "    \"channels\": [{ \"name\": \"ticker\", \"product_ids\": [\"BTC-EUR\"] }]\n" +
//                    "}"
//        )
        webSocketClient.send(
            "{\n" +
                    "    \"action\": \"SubAdd\",\n" +
                    "    \"subs\": [\"21~BTC\", \"21~ETH\"]" +
                    "}"
        )
    }

    private fun unsubscribe() {
//        webSocketClient.send(
//            "{\n" +
//                    "    \"type\": \"unsubscribe\",\n" +
//                    "    \"channels\": [\"ticker\"]\n" +
//                    "}"
//        )
        webSocketClient.send(
            "{\n" +
                    "    \"action\": \"SubRemove\",\n" +
                    "    \"subs\": [\"21~BTC\", \"21~ETH\"]" +
                    "}"
        )
    }

    companion object {
        const val TAG = "Coinbase"
    }

}