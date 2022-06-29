package com.stockbit.common.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.stockbit.common.R
import com.stockbit.common.extension.observe
import com.stockbit.common.extension.setupSnackbar
import com.stockbit.common.utils.DialogUtils
import com.stockbit.common.utils.Event
import com.stockbit.model.common.UIText
import com.stockbit.navigation.NavigationCommand

abstract class BaseFragment<binding: ViewDataBinding>(
    @LayoutRes
    private val layoutID: Int
): Fragment() {
    private lateinit var mainView: MainView
    private var _binding: binding? = null
    val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainView = activity as MainView
    }

    abstract fun getViewModel(): BaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutID, container, false)
//        binding.setVariable(BR.data, getViewModel())
        binding.executePendingBindings()
        return binding.root
    }

    open fun onInitialization() = Unit

    abstract fun onReadyAction()

    open fun onObserveAction() = Unit

    open fun onFragmentDestroyed() = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(this is BaseNavigator) getViewModel().navigator = this as BaseNavigator
        onInitialization()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigation(getViewModel())
        setupSnackbar(this, getViewModel().snackBarError, Snackbar.LENGTH_LONG)
        onObserveAction()
    }

    override fun onStart() {
        super.onStart()
        onReadyAction()
    }

    override fun onDestroy() {
        super.onDestroy()
//        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        onFragmentDestroyed()
    }

    fun setToolbar(
        rightImage: Int,
        leftImage: Int? = null,
        centerTitle: UIText? = null,
        centerImage: Int? = null,
    ) = mainView.setToolbar(rightImage, leftImage, centerTitle, centerImage)


    fun setToolbarLeft(listener: (()-> Unit)?) = mainView.setToolbarLeft(listener)

    fun setToolbarRight(listener: ()-> Unit) = mainView.setToolbarRight(listener)

    // UTILS METHODS ---
    open fun defaultErrorAction(message: String?, code: Int?) {
        DialogUtils.showDialogAlert(requireContext(), getString(R.string.default_dialog_title), message?:getString(R.string.error_default))
    }

    /**
     * Observe a [NavigationCommand] [Event] [LiveData].
     * When this [LiveData] is updated, [Fragment] will navigate to its destination
     */
    private fun observeNavigation(viewModel: BaseViewModel) {
        observe(viewModel.navigation) {
            it.getContentIfNotHandled()?.let { command ->
                when (command) {
                    is NavigationCommand.To -> findNavController().navigate(command.directions, getExtras())
                    is NavigationCommand.Back -> findNavController().navigateUp()
                }
            }
        }
    }

    /**
     * [FragmentNavigatorExtras] mainly used to enable Shared Element transition
     */
    open fun getExtras(): FragmentNavigator.Extras = FragmentNavigatorExtras()
}