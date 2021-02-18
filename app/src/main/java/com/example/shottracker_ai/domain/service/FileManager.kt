package com.example.shottracker_ai.domain.service

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.example.shottracker_ai.MainApplication
import com.example.shottracker_ai.utilities.toFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FileManager {

    private val appContext
        get() = MainApplication.appContext

    private val keyUserImage = "userImage"

    suspend fun saveAssetImage(fileName: String): Uri = suspendCoroutine { continuation ->
        GlobalScope.launch(Dispatchers.IO) {
            val inputStream = appContext.assets.open(fileName)
            continuation.resume(saveImage(inputStream, fileName))
        }
    }

    suspend fun saveUserImage(imagePath: Uri): Uri {
        return imagePath.path?.let { File(it) }?.let { imageFile ->
            saveImage(imageFile.inputStream(), "${keyUserImage}.png")
        } ?: error("Unable to find file at ${imagePath.path}")
    }

    fun getImagePath(imageName: String): Uri? {
        return getInternalFilePath(imageName).takeIf { it.exists() }?.absolutePath?.toUri()
    }

    fun doesImageFileExists(imageFilePath: Uri) = imageFilePath.path?.let { File(it) }?.exists() == true

    private suspend fun saveImage(inputStream: InputStream, outputFileName: String) = suspendCoroutine<Uri> { continuation ->
        GlobalScope.launch(Dispatchers.IO) {
            try {
                appContext.openFileOutput(outputFileName, Context.MODE_PRIVATE).use { output ->
                    output.write(inputStream.readBytes())
                }
            } catch (e: Exception) {
                getInternalFilePath(outputFileName).delete()
                error("Error saving $outputFileName to internal storage")
            } finally {
                inputStream.close()
                continuation.resume(getInternalFilePath(outputFileName).absolutePath.toUri())
            }
        }
    }

    private fun getInternalFilePath(fileName: String) = appContext.getFileStreamPath(fileName)

}