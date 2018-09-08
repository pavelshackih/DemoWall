package org.aim.wallpaper.data.share

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.content.FileProvider
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import java.io.FileOutputStream

class ImageShareWrapperImpl(private val context: Context) : ImageShareWrapper {

    override fun shareImage(bitmap: Bitmap): Completable = loadFile(bitmap)
            .map { internalShare(it) }
            .toCompletable()

    private fun loadFile(bitmap: Bitmap): Single<File> {
        return Single.fromCallable {
            val fileName = System.currentTimeMillis().toString()
            val file = File(context.externalCacheDir, "$fileName.png")
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.flush()
            }
            file
        }
    }

    private fun internalShare(file: File) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val exportedUri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
        intent.putExtra(Intent.EXTRA_STREAM, exportedUri)
        intent.type = "image/png"
        val packageManager = context.packageManager
        val resolveResult = packageManager.queryIntentActivities(intent, 0)
        if (resolveResult?.isNotEmpty() ?: false) {
            context.startActivity(intent)
        }
    }
}