package com.stockbit.common.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.stockbit.common.R
import com.stockbit.common.databinding.BasedialogAlertBinding
import com.stockbit.common.databinding.CustomTwobuttonDialogBinding

object DialogUtils {

    data class DefaultDialogData(
        val title: String,
        val desc: String,
        val txtButton1: String,
        val txtButton2: String,
        val txtLink: String,
        val iconVisibility: Boolean
    )

    fun showDialogAlert(
        context: Context,
        title: String,
        desc: String,
        listener: () -> Unit = {}
    ) {
        val view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater).inflate(R.layout.basedialog_alert, null as ViewGroup?)
        val binding = BasedialogAlertBinding.bind(view)
        with(binding) {
            titleDialog.text = title
            descDialog.text = desc

            val builder = AlertDialog.Builder(context)
            builder.setView(root)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.drawable.bg_rounded)

            ivCloseBasedialogalert.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setOnDismissListener {
                listener.invoke()
            }
        }
    }

    fun showDefaultDialog(
        context: Context,
        defaultData: DefaultDialogData,
        listenerBtn1: () -> Unit,
        listenerBtn2: (() -> Unit)? = null,
        listenerLink: (() -> Unit)? = null
    ) {
        val view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater).inflate(R.layout.custom_twobutton_dialog, null as ViewGroup?)
        val binding = CustomTwobuttonDialogBinding.bind(view)
        with(binding) {
            titleDialog.text = defaultData.title
            descDialog.text = defaultData.desc
            btn1Dialog.text = defaultData.txtButton1
            imgDialog.visibility = if(defaultData.iconVisibility) View.VISIBLE else View.GONE

            btn2Dialog.text = defaultData.txtButton2
            if (defaultData.txtLink.isEmpty()) {
                linkDialog.visibility = View.GONE
            } else {
                linkDialog.visibility = View.VISIBLE
                linkDialog.text = defaultData.txtLink
            }

            linkDialog.setOnClickListener {
                listenerLink?.invoke()
            }
            val builder = AlertDialog.Builder(context)
            builder.setView(root)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.drawable.bg_rounded)
            btn2Dialog.setOnClickListener {
                listenerBtn2?.invoke()
                dialog.dismiss()
            }
            btn1Dialog.setOnClickListener {
                listenerBtn1.invoke()
                dialog.dismiss()
            }
        }
    }
}