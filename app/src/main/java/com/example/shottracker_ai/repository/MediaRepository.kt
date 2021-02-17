package com.example.shottracker_ai.repository

import android.content.Context
import com.example.shottracker_ai.MainApplication
import com.example.shottracker_ai.utilities.toFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MediaRepository @Inject constructor() {

    private val appContext
        get() = MainApplication.appContext

    private val keyUserImage = "userImage"

    suspend fun saveAssetImage(fileName: String): String = suspendCoroutine { continuation ->
        GlobalScope.launch(Dispatchers.IO) {
            val inputStream = appContext.assets.open(fileName)
            continuation.resume(saveImage(inputStream, fileName))
        }
    }

    suspend fun saveUserImage(filePath: String): String {
        val imageFile = filePath.toFile()
        return saveImage(imageFile.inputStream(), "${keyUserImage}.png")
    }

    fun getImagePath(imageName: String): String? {
        return getInternalFilePath(imageName).takeIf { it.exists() }?.absolutePath
    }

    private suspend fun saveImage(inputStream: InputStream, outputFileName: String) = suspendCoroutine<String> { continuation ->
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
                continuation.resume(getInternalFilePath(outputFileName).absolutePath)
            }
        }
    }

    private fun getInternalFilePath(fileName: String) = appContext.getFileStreamPath(fileName)

}