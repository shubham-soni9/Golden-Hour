package com.goldenhour.ui.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Base structure for defining Dialog
 *
 * @param <V> of type BaseViewModel
 */
abstract class BaseDialog<V : BaseViewModel> : DialogFragment(), HasAndroidInjector {
    var mActivity: Activity? = null

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    /**
     * Performs members-injection for a concrete subtype of a fragment
     */
    override fun androidInjector(): AndroidInjector<Any>? {
        return androidInjector
    }

    /**
     * Attaching fragment to its parent
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mActivity = context as Activity
        AndroidSupportInjection.inject(this)
    }

    /**
     * Initializing view for the dialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = LinearLayout(activity)

        // creating the fullscreen dialog
        val dialog = Dialog(context!!)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    /**
     * On Detaching the view from its parent
     */
    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    /**
     * Method to show the dialog
     */
    override fun show(fragmentManager: FragmentManager, tag: String?) {
        val transaction = fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }

    /**
     * Method to dismiss the dialog
     */
    fun dismissDialog(tag: String) {
        dismiss()
    }


}
