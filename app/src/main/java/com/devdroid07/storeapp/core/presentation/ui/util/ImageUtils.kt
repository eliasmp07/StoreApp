package com.devdroid07.storeapp.core.presentation.ui.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File

fun imageCamara(image: String): String{
    return File(image).readBytes().toBase64()
}

private fun ByteArray.toBase64(): String {
    return Base64.encodeToString(this, Base64.NO_WRAP)
}


fun reduceImageSize(imagePath: String): String {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeFile(imagePath, options)

    // Calcula el factor de reducción
    val targetWidth = 800  // ancho objetivo
    val scaleFactor = (options.outWidth / targetWidth).coerceAtLeast(1)

    // Decodifica la imagen con el factor de reducción
    options.inJustDecodeBounds = false
    options.inSampleSize = scaleFactor

    val bitmap = BitmapFactory.decodeFile(imagePath, options)
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()

    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}