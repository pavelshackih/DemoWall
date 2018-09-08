package org.aim.wallpaper.ui.common

import android.app.Fragment
import android.app.FragmentManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.aim.wallpaper.R
import org.aim.wallpaper.ext.bundle
import java.io.IOException

class ErrorFragment : Fragment() {

    private lateinit var textView: TextView
    private lateinit var descTextView: TextView
    private lateinit var button: Button

    private lateinit var error: Throwable

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        error = arguments.getSerializable(ERROR) as Throwable
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_error, container, false)
        textView = view.findViewById(R.id.text_view) as TextView
        descTextView = view.findViewById(R.id.desc_text_view) as TextView
        button = view.findViewById(R.id.button) as Button
        val host = activity as HostActivity
        button.setOnClickListener { host.onRetry() }
        if (error is IOException) {
            handler.postDelayed({
                descTextView.setText(R.string.network_error)
                descTextView.visibility = View.VISIBLE
            }, 300)
        }
        return view
    }


    fun show(fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
                .replace(R.id.container, this, TAG)
                .commit()
    }

    companion object {

        const val TAG = "ErrorFragment"
        private const val ERROR = "error"

        fun newInstance(error: Throwable): ErrorFragment {
            val fragment = ErrorFragment()
            fragment.arguments = bundle { putSerializable(ERROR, error) }
            return fragment
        }

        fun hide(fragmentManager: FragmentManager) {
            val fragment = fragmentManager.findFragmentByTag(TAG)
            if (fragment != null) {
                fragmentManager.beginTransaction()
                        .remove(fragment)
                        .commit()
            }
        }
    }
}