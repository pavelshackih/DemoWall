package org.aim.wallpaper.ui.donate

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.support.v7.app.AlertDialog
import org.aim.wallpaper.R

class ThankYouDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.thank_you)
        builder.setMessage(getString(R.string.thank_you_message))
        builder.setPositiveButton(R.string.ok, null)
        return builder.create()
    }
}
