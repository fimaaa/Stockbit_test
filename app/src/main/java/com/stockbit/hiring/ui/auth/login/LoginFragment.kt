package com.stockbit.hiring.ui.auth.login

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.stockbit.common.base.BaseFragment
import com.stockbit.common.base.BaseViewModel
import com.stockbit.common.extension.observe
import com.stockbit.hiring.R
import com.stockbit.hiring.databinding.FragmentLoginBinding
import com.stockbit.model.common.UIText



class LoginFragment: BaseFragment<FragmentLoginBinding>(
    R.layout.fragment_login
), LoginNavigator {
    private val navController: NavController by lazy { requireActivity().findNavController(R.id.nav_host_fragment) }

    private val viewModel: LoginViewModel by viewModels()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onInitialization() {
        viewModel.navigator = this
    }

    override fun onReadyAction() {
        setToolbar(
            centerTitle = UIText.StringResource(R.string.page_title_login),
            rightImage = R.drawable.ic_headset_mic
        )
        binding.apply {
            btnLoginActionlogin.setOnClickListener {
                viewModel.validateLogin(
                    tiLoginUsermail.editText?.text.toString(),
                    tiLoginPassword.editText?.text.toString()
                )
            }
            btnLoginGoogle.setOnClickListener {
                viewModel.validateLogin(
                    "tiLoginUsermail.editText?.text.toString()",
                    "iLoginPassword.editText?.text.toString()"
                )
            }
            tiLoginUsermail.editText?.addTextChangedListener {
                if(tiLoginUsermail.error != null) tiLoginUsermail.error = null
                setActionLoginButton()
            }
            tiLoginPassword.editText?.addTextChangedListener {
                if(tiLoginPassword.error != null) tiLoginPassword.error = null
                setActionLoginButton()
            }
        }
    }

    private fun setActionLoginButton() {
        binding.btnLoginActionlogin.isEnabled = binding.tiLoginUsermail.error == null && binding.tiLoginPassword.error == null
    }

    override fun setEmailError(message: UIText) {
        binding.tiLoginUsermail.error = message.asString(requireContext())
        setActionLoginButton()
    }

    override fun setPasswordError(message: UIText) {
        binding.tiLoginPassword.error = message.asString(requireContext())
        setActionLoginButton()
    }
}