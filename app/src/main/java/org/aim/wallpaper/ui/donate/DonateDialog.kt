package org.aim.wallpaper.ui.donate

import android.app.Dialog
import android.app.DialogFragment
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import org.aim.wallpaper.BuildConfig
import org.aim.wallpaper.R
import org.aim.wallpaper.ext.inflate
import org.aim.wallpaper.ext.setSafeOnClickListener

class DonateDialog : DialogFragment(), BillingProcessor.IBillingHandler {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var cancelButton: Button
    private lateinit var buyButton: Button

    private var processor: BillingProcessor? = null

    private val products = Array(3, { i -> "${BuildConfig.APPLICATION_ID}.product.item_${i + 1}" })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        val view = View.inflate(activity, R.layout.activity_donate, null)
        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        progressBar = view.findViewById(R.id.progress_bar) as ProgressBar
        cancelButton = view.findViewById(R.id.cancel_button) as Button
        buyButton = view.findViewById(R.id.ok_button) as Button

        buyButton.setSafeOnClickListener(View.OnClickListener {
            val adapter = recyclerView.adapter as ProductAdapter
            val product = adapter.getSelectedProduct()
            product?.let {
                processor?.purchase(activity, product.id)
            }
        })

        cancelButton.setOnClickListener { dismissAllowingStateLoss() }
        builder.setView(view)
        builder.setTitle(getString(R.string.support_development))
        Handler(Looper.getMainLooper()).postDelayed({
            processor = BillingProcessor(activity, getString(R.string.bill_key), this)
        }, 500)
        return builder.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        processor?.release()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        processor?.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onBillingInitialized() {
        refreshList()
    }

    fun refreshList() {
        if (activity != null && isAdded) {
            val p = processor as BillingProcessor
            val allProducts = products
            val listOfProducts = allProducts
                    .map { p.getPurchaseListingDetails(it) }
                    .filterNotNull()
                    .map { Product(it.productId, "${it.description} - ${it.priceText}", false) }
            if (listOfProducts.isEmpty()) {
                Toast.makeText(activity, getString(R.string.thank_you), Toast.LENGTH_SHORT).show()
                dismissAllowingStateLoss()
                return
            }
            val adapter = ProductAdapter(listOfProducts)
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.adapter = adapter
            buyButton.visibility = View.VISIBLE
        }
    }

    override fun onPurchaseHistoryRestored() {
    }

    override fun onProductPurchased(productId: String?, details: TransactionDetails?) {
        dismissAllowingStateLoss()
        val host = activity
        if (host is DonateHost) {
            host.onPurchased()
        }
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Toast.makeText(activity, getString(R.string.error_billing_opening), Toast.LENGTH_LONG).show()
        dismissAllowingStateLoss()
    }

    data class Product(val id: String, val desc: String, var selected: Boolean)

    class ProductViewHolder(itemView: View, val f: (Int) -> Unit) : RecyclerView.ViewHolder(itemView), CompoundButton.OnCheckedChangeListener {

        private val radioButton = itemView as RadioButton

        init {
            radioButton.setOnCheckedChangeListener(this)
        }

        var onBind = false

        fun bind(product: Product) {
            radioButton.text = product.desc
            onBind = true
            radioButton.isChecked = product.selected
            onBind = false
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if (!onBind) {
                f(adapterPosition)
            }
        }
    }

    class ProductAdapter(val products: List<Product>) : RecyclerView.Adapter<ProductViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
                ProductViewHolder(parent.inflate(R.layout.view_holder_radio), { i -> onCheck(i) })

        private fun onCheck(position: Int) {
            products.forEachIndexed { index, product ->
                product.selected = index == position
            }
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: ProductViewHolder?, position: Int) {
            holder?.bind(products[position])
        }

        override fun getItemCount(): Int = products.size

        fun getSelectedProduct(): Product? = products.find { it.selected }
    }
}