package com.goldenhour.util.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.goldenhour.R

/**
 * Provide alert dialog with message and custom ui to user based on certain action
 */
class AlertDialog {

    private var alertDialog: Dialog? = null

    /**
     * The instance of the Activity on which the
     * AlertDialog will be displayed
     */
    private var activity: Activity? = null

    /**
     * The receiver to which the AlertDialog
     * will return the CallBacks
     *
     *
     * Note: listener should be an instance of
     * AlertDialog.Listener
     */
    private var listener: Listener? = null

    /**
     * The code to differentiate the various CallBacks
     * from different Dialogs
     */
    private val purpose: Int = 0

    /**
     * The title to be set on the Dialog
     */
    private var title: String? = null

    /**
     * The message to be set on the Dialog
     */
    private var message: String? = null

    /**
     * The text to be set on the Action Button
     */
    private var actionButton: String? = null

    /**
     * The data to sent via the Dialog from the
     * remote parts of the Activity to other
     * parts
     */
    private val backpack: Bundle? = null

    /**
     * Method to create and display the alert alertDialog
     *
     * @return
     */
    private fun init(): AlertDialog {

        try {

            alertDialog = Dialog(activity!!, android.R.style.Theme_Translucent_NoTitleBar)
            alertDialog!!.setContentView(R.layout.dialog_alert)

            val dialogWindow = alertDialog!!.window
            val layoutParams = dialogWindow!!.attributes
            layoutParams.dimAmount = 0.6f

            dialogWindow.attributes.windowAnimations = R.style.CustomDialogStyle

            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            alertDialog!!.setCancelable(false)
            alertDialog!!.setCanceledOnTouchOutside(false)

            val tvTitle = alertDialog!!.findViewById<TextView>(R.id.tvTitle)
            val tvMessage = alertDialog!!.findViewById<TextView>(R.id.tvMessage)
            val btnAction = alertDialog!!.findViewById<Button>(R.id.btnAction)

            if (title != null) {
                tvTitle.text = title
                tvTitle.visibility = View.VISIBLE
            }
            tvMessage.text = message
            btnAction.text = actionButton
            btnAction.setOnClickListener {

                alertDialog!!.dismiss()

                if (listener != null)
                    listener!!.performPostAlertAction(purpose, backpack)
            }

        } catch (e: Exception) {

            e.printStackTrace()
        }

        return this
    }

    /**
     * Method to init the initialized alertDialog
     */
    fun show() {

        // Check if activity lives
        if (activity != null)
        // Check if alertDialog contains data
            if (alertDialog != null) {
                try {
                    // Show the Dialog
                    alertDialog!!.show()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
    }


    /**
     * Interfaces the events from the AlertDialog
     * to the Calling Context
     */
    interface Listener {

        /**
         * Override this method to listen to
         * the events fired from AlertDialog
         *
         * @param purpose
         * @param backpack
         */
        fun performPostAlertAction(purpose: Int, backpack: Bundle?)
    }

    /**
     * Class to help building the Alert Dialog
     */
    class Builder
    /**
     * Constructor to build a alertDialog for Activity
     *
     * @param activity
     */
        (activity: Activity) {

        private val alertDialog = AlertDialog()

        init {

            alertDialog.activity = activity

            if (activity is Listener)
                alertDialog.listener = activity
        }

        /**
         * Sets the a custom listener to receive the CallBacks
         *
         * @param listener
         * @return
         */
        fun listener(listener: Listener): Builder {
            alertDialog.listener = listener
            return this
        }

        /**
         * Set the message for the AlertDialog
         *
         * @param messageId
         * @return
         */
        fun message(messageId: Int): Builder {
            alertDialog.message = alertDialog.activity!!.getString(messageId)
            return this
        }

        /**
         * Set the message for the AlertDialog
         *
         * @param message
         * @return
         */
        fun message(message: String): Builder {
            alertDialog.message = message
            return this
        }

        /**
         * Set the actionButton for the AlertDialog
         *
         * @param button
         * @return
         */
        fun button(button: String): Builder {
            alertDialog.actionButton = button
            return this
        }

        /**
         * Set the actionButton for the AlertDialog
         *
         * @param buttonRes
         * @return
         */
        fun button(buttonRes: Int): Builder {
            alertDialog.actionButton = alertDialog.activity!!.getString(buttonRes)
            return this
        }

        /**
         * Method to build an AlertDialog and display
         * it on the screen. The instance returned can
         * be used to manipulate the alertDialog in future.
         *
         * @return
         */
        fun build(): AlertDialog {
            return alertDialog.init()
        }

    }
}


