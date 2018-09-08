package org.aim.wallpaper.ui.common

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import org.aim.wallpaper.R
import org.aim.wallpaper.ext.inflate
import java.util.*

class BottomListBuilder(val context: Context) {

    private val TAG = "BottomSheetBehavior"

    private val items = ArrayList<Item>()
    var itemLayout: Int = 0
    var header: CharSequence? = null
    var listener: ((Item, Int) -> Unit)? = null // on item click listener (item, position) -> Unit

    fun item(item: Item): BottomListBuilder {
        items.add(item)
        return this
    }

    fun header(header: CharSequence?): BottomListBuilder {
        this.header = header
        return this
    }

    fun header(@StringRes header: Int): BottomListBuilder {
        return header(context.getString(header))
    }

    fun listener(listener: (Item, Int) -> Unit): BottomListBuilder {
        this.listener = listener
        return this
    }

    fun item(id: Int, @StringRes header: Int, @DrawableRes ico: Int): BottomListBuilder {
        return item(Item(id, context.getString(header), null, ico))
    }

    fun item(id: Int, header: String, @DrawableRes ico: Int): BottomListBuilder {
        return item(Item(id, header, null, ico))
    }

    fun create(): BottomSheetDialog {
        return create(R.layout.bottom_sheet_item)
    }

    fun create(@LayoutRes itemLayoutId: Int): BottomSheetDialog {
        if (items.isEmpty()) {
            throw IllegalArgumentException("At least one item in list should be provided!")
        }

        itemLayout = itemLayoutId

        val dialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_list, null)

        val headerView = view.findViewById(R.id.header) as TextView
        headerView.text = header

        if (header.isNullOrEmpty()) {
            headerView.visibility = View.GONE
        }

        val recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        val linearManager = LinearLayoutManager(context)
        linearManager.isAutoMeasureEnabled = true
        recyclerView.layoutManager = linearManager
        recyclerView.adapter = BottomAdapter(items, { item: Item, i: Int ->
            if (listener != null) {
                dialog.dismiss()
                listener?.invoke(item, i)
            }
        })

        dialog.setContentView(view, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        val layoutParams = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = layoutParams.behavior
        if (behavior is BottomSheetBehavior<View>) {
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dialog.dismiss()
                    }
                }
            })
            Handler(Looper.getMainLooper()).postDelayed({
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.peekHeight = view.height
            }, 100)
        }
        return dialog
    }

    data class Item(val id: Int, val header: CharSequence, val subHeader: CharSequence? = null, @DrawableRes val ico: Int = 0)

    private class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val headerView: TextView = itemView.findViewById(R.id.bs_list_title) as TextView
        val subHeaderView: TextView = itemView.findViewById(R.id.bs_list_sub_title) as TextView
        val imageView: ImageView = itemView.findViewById(R.id.bs_image) as ImageView

        fun bind(item: Item, listener: ((Item, Int) -> Unit)?) {
            headerView.text = item.header
            subHeaderView.text = item.subHeader
            imageView.setImageResource(item.ico)
            if (item.ico == 0) {
                imageView.visibility = View.GONE
            } else {
                imageView.visibility = View.VISIBLE
            }
            itemView.setOnClickListener {
                listener?.invoke(item, adapterPosition)
            }
        }
    }

    private inner class BottomAdapter(val items: ArrayList<Item>, val listener: ((Item, Int) -> Unit)?) : RecyclerView.Adapter<ItemViewHolder>() {

        override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
            holder?.bind(items[position], listener)
        }

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemViewHolder? =
                ItemViewHolder(parent.inflate(itemLayout))

        override fun getItemCount(): Int = items.size
    }
}