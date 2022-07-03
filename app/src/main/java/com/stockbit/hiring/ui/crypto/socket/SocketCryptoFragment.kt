package com.stockbit.hiring.ui.crypto.socket

import android.util.Log
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.hiring.R
import com.stockbit.hiring.databinding.FragmentCryptoSocketBinding
import com.stockbit.model.crypto.ResponseListCryptoInfo
import com.stockbit.model.socket.BitcoinTicker
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import javax.net.ssl.SSLSocketFactory

class SocketCryptoFragment: BaseFragment<FragmentCryptoSocketBinding>(
    R.layout.fragment_crypto_socket
) {
    private val viewModel: SocketCryptoViewModel by viewModels()
    override fun getViewModel(): BaseViewModel = viewModel

    private lateinit var webSocketClient: WebSocketClient

    override fun onReadyAction() {
        binding.btnOpen.setOnClickListener {
            binding.tvPriceSocket.text = "Opened Socket"
            initWebSocket()
        }
        binding.btnClose.setOnClickListener {
            binding.tvPriceSocket.text = "Closed Socket"
            webSocketClient.close()
        }
    }

    private fun initWebSocket() {
        val coinbaseUri = URI(WEB_SOCKET_URL)

        createWebSocketClient(coinbaseUri)
        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient.setSocketFactory(socketFactory)
        webSocketClient.connect()
    }

    private fun createWebSocketClient(coinbaseUri: URI?) {
        webSocketClient = object : WebSocketClient(coinbaseUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(TAG, "onOpen")
                subscribe()
            }

            override fun onMessage(message: String?) {
                Log.d(TAG, "onMessage: $message")
                setUpBtcPriceText(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose")
                unsubscribe()
            }

            override fun onError(ex: Exception?) {
                Log.e(TAG, "onError: ${ex?.message}")
            }

        }
    }

    private fun setUpBtcPriceText(message: String?) {
        message?.let {
            val bitcoin = Gson().fromJson(it, BitcoinTicker::class.java)
            requireActivity().runOnUiThread {
                binding.tvPriceSocket.text = "1 BTC: ${bitcoin?.price} €"
            }
        }
    }

    private fun subscribe() {
        webSocketClient.send(
            "{\n" +
                    "    \"type\": \"subscribe\",\n" +
                    "    \"channels\": [{ \"name\": \"ticker\", \"product_ids\": [\"BTC-EUR\"] }]\n" +
                    "}"
        )
    }

    private fun unsubscribe() {
        webSocketClient.send(
            "{\n" +
                    "    \"type\": \"unsubscribe\",\n" +
                    "    \"channels\": [\"ticker\"]\n" +
                    "}"
        )
    }

    override fun onResume() {
        super.onResume()
        initWebSocket()
    }

    override fun onPause() {
        super.onPause()
        webSocketClient.close()
    }

    companion object {
        const val WEB_SOCKET_URL = "wss://ws-feed.pro.coinbase.com"
        const val TAG = "Coinbase"
    }
}