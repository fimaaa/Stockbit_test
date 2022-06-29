package com.stockbit.common.extension

import androidx.lifecycle.viewModelScope
import com.stockbit.common.base.BaseViewModel
import com.stockbit.model.common.UIText
import com.stockbit.repository.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.net.ssl.HttpsURLConnection

fun <T> BaseViewModel.safeApiCall(
    callSuccess: suspend () -> T
) {
    statusViewModel.value = Resource.loading()
    viewModelScope.launch(Dispatchers.IO) {
        try {
            statusViewModel.postValue(Resource.success(true))
            callSuccess()
        } catch (t: Throwable) {
//            if (!BuildConfig.DEBUG){
//                val responseString = Gson().toJson("msg: $message} , code: $code")
//                val url = t.ErrorResponseUrl()
//                Firebase.crashlytics.log(url)
//                Firebase.crashlytics.log(responseString)
//                Firebase.crashlytics.recordException(t)
//            }
            t.printStackTrace()
            statusViewModel.postValue(Resource.error(t))
        }
    }
}

fun <T> BaseViewModel.safeApiCallIndependent(
    callSuccess: suspend () -> T,
    callError: (Throwable) -> Unit = {}
) {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            callSuccess()
        } catch (t: Throwable) {
//            if (!BuildConfig.DEBUG){
//                val message = t.toThrowableMessage()
//                val code = t.toThrowableCode()
//                val responseString = Gson().toJson("msg: $message} , code: $code")
//                val url = t.ErrorResponseUrl()
//                Firebase.crashlytics.log(url)
//                Firebase.crashlytics.log(responseString)
//                Firebase.crashlytics.recordException(t)
//            }
            t.printStackTrace()
            callError(t)
        }
    }
}