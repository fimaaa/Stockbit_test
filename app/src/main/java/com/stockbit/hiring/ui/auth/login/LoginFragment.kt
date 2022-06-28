package com.stockbit.hiring.ui.auth.login

import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.hiring.R
import com.stockbit.hiring.databinding.FragmentLoginBinding
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.stockbit.navigation.NavigationCommand


class LoginFragment: BaseFragment<FragmentLoginBinding>(
    R.layout.fragment_login
), LoginNavigator {
    private val navController: NavController by lazy { requireActivity().findNavController(R.id.nav_host_fragment) }

    private val viewModel: LoginViewModel by viewModels()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onReadyAction() {
        binding.btnLoginGoogle.setOnClickListener {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToListCryptoFragment())
        }
    }
}