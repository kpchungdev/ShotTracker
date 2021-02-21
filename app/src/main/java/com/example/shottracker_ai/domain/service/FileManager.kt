package com.example.shottracker_ai.domain.service

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.example.shottracker_ai.MainApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FileManager {

    private val appContext
        get() = MainApplication.appContext

    suspend fun saveAsset(assetFileName: String): Uri = suspendCoroutine { continuation ->
        GlobalScope.launch(Dispatchers.IO) {
            val inputStream = appContext.assets.open(assetFileName)
            continuation.resume(saveFile(inputStream, assetFileName))
        }
    }

    suspend fun saveFile(targetFilePath: Uri, outputFileName: String): Uri? {
        return targetFilePath.path?.let { File(it) }?.let { file ->
            saveFile(file.inputStream(), outputFileName)
        }
    }

    private suspend fun saveFile(inputStream: InputStream, outputFileName: String) = suspendCoroutine<Uri> { continuation ->
        GlobalScope.launch(Dispatchers.IO) {
            try {
                appContext.openFileOutput(outputFileName, Context.MODE_PRIVATE).use { output ->
                    output.write(inputStream.readBytes())
                }
            } catch (e: Exception) {
                getInternalFile(outputFileName).delete()
                Timber.d("Error saving $outputFileName to storage $e")
            } finally {
                inputStream.close()
                continuation.resume(getImagePath(outputFileName) ?: error("File was not generated"))
            }
        }
    }

    fun getImagePath(imageName: String): Uri? {
        return getInternalFile(imageName).takeIf { it.exists() }?.absolutePath?.toUri()
    }

    fun doesFileExists(imageFilePath: Uri) = imageFilePath.path?.let { File(it) }?.exists() == true

    private fun getInternalFile(fileName: String) = appContext.getFileStreamPath(fileName)

}