package com.stockbit.hiring

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.stockbit.common.base.MainView
import com.stockbit.hiring.databinding.ActivityMainBinding
import com.stockbit.model.common.UIText

class MainActivity : AppCompatActivity(), MainView {

    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }
    private val appBarConfiguration: AppBarConfiguration by lazy { AppBarConfiguration(navController.graph) }

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        supportActionBar?.hide()
        setToolbarLeft(null)
        configureNavController()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // ---

    private fun configureNavController() {
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun setToolbar(
        rightImage: Int,
        leftImage: Int?,
        centerTitle: UIText?,
        centerImage: Int?
    ) {
        leftImage?.let { binding.ivToolbarLeft.setImageResource(it) }
        binding.ivToolbarLeft.isVisible = leftImage != null

        binding.ivToolbarRight.setImageResource(rightImage)

        binding.tvToolbarCenter.isVisible = centerTitle != null
        binding.tvToolbarCenter.text = centerTitle?.asString(this)


        binding.ivToolbarCenter.isVisible = centerImage != null
        centerImage?.let { binding.ivToolbarCenter.setImageResource(it) }

    }

    override fun setToolbarLeft(listener: (() -> Unit)?) {
        binding.ivToolbarLeft.setOnClickListener {
            (listener?:{
                navController.navigateUp()
            }).invoke()
        }
    }

    override fun setToolbarRight(listener: () -> Unit) {
        binding.ivToolbarRight.setOnClickListener {
            listener.invoke()
        }
    }
}
