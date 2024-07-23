package com.devdroid07.storeapp.core.presentation.ui.util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.core.content.FileProvider
import com.devdroid07.storeapp.R
import org.apache.commons.io.FileUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID

class ComposeFileProvider : FileProvider(R.xml.path_provider) {

    companion object {

        fun createFileFromUri(
            context: Context,
            uri: Uri
        ): File? {
            return try {
                val stream = context.contentResolver.openInputStream(uri)
                val file = File.createTempFile(
                    "${System.currentTimeMillis()}",
                    ".png",
                    context.cacheDir
                )
                FileUtils.copyInputStreamToFile(
                    stream,
                    file
                )
                return file
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        fun getPathFromBitmap(
            context: Context,
            bitmap: Bitmap
        ): String {
            val wrapper = ContextWrapper(context)
            var file = wrapper.getDir(
                "Images",
                Context.MODE_PRIVATE
            )
            file = File(
                file,
                "${UUID.randomUUID()}.jpg"
            )
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                stream
            )
            stream.flush()
            stream.close()
            return file.path
        }

    }
}
