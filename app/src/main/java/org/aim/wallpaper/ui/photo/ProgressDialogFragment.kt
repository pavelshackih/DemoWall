package org.aim.wallpaper.ui.photo

import android.app.Dialog
import android.app.DialogFragment
import android.app.FragmentManager
import android.app.ProgressDialog
import android.os.Bundle
import org.aim.wallpaper.R

class ProgressDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = ProgressDialog(activity)
        dialog.isIndeterminate = true
        dialog.setMessage(getString(R.string.please_waiting))
        isCancelable = false
        return dialog
    }

    companion object {

        const val TAG = "ProgressDialogFragment"

        fun newInstance() = ProgressDialogFragment()

        fun hide(fragmentManager: FragmentManager) {
            val fragment = fragmentManager.findFragmentByTag(TAG)
            fragment?.apply {
                fragmentManager.beginTransaction()
                        .remove(this)
                        .commit()
            }
        }
    }
}