package com.stockbit.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.stockbit.common.utils.Event
import com.stockbit.navigation.NavigationCommand
import com.stockbit.repository.utils.Resource
import kotlinx.coroutines.CoroutineScope

abstract class BaseViewModel: ViewModel(), BaseNavigator {

    val statusViewModel = MutableLiveData<Resource<Boolean>>()
    lateinit var navigator: BaseNavigator

    // FOR ERROR HANDLER
    protected val _snackbarError = MutableLiveData<Event<Int>>()
    val snackBarError: LiveData<Event<Int>> get() = _snackbarError

    // FOR NAVIGATION
    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> = _navigation

    /**
     * Convenient method to handle navigation from a [ViewModel]
     */
     fun navigate(directions: NavDirections) {
        _navigation.value = Event(NavigationCommand.To(directions))
    }
}