package org.aim.wallpaper.ui.photo

import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import org.aim.wallpaper.R

class PermissionInfoDialog : DialogFragment(), DialogInterface.OnClickListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.permission_info_message)
        builder.setPositiveButton(R.string.ok, this)
        return builder.create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            val host = activity as PhotoHostActivity
            host.onRequestPermission()
        }
    }
}