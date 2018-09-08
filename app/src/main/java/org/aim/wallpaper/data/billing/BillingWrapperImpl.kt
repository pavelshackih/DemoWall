package org.aim.wallpaper.data.billing

import android.content.Context
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import io.reactivex.Single

class BillingWrapperImpl(val context: Context) : BillingWrapper, BillingProcessor.IBillingHandler {

    private lateinit var processor: BillingProcessor

    override fun onBillingInitialized() {
    }

    override fun onPurchaseHistoryRestored() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun onProductPurchased(p0: String?, p1: TransactionDetails?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun onBillingError(p0: Int, p1: Throwable?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isUserAlreadyPayed(): Single<Boolean> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getProducts(): Single<List<String>> {
        throw UnsupportedOperationException("not implemented")
    }
}