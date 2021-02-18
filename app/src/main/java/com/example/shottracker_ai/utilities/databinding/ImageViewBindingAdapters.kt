package com.example.shottracker_ai.utilities.databinding

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.File

object ImageViewBindingAdapters {

    @JvmStatic
    @BindingAdapter(
        value = ["imageUri", "circleCropTransformation", "imageCornerRadius"],
        requireAll = false
    )
    fun setImageWithGlide(
        imageView: ImageView,
        imageUri: Uri?,
        circleCropTransformation: Boolean? = null,
        imageCornerRadius: Float? = null
    ) {
        imageUri?.path?.let { filePath ->
            Glide.with(imageView)
                .load(File(filePath))
                .apply {
                    if (circleCropTransformation == true) {
                        circleCrop()
                    }

                    imageCornerRadius?.toInt()?.let { cornerRadius ->
                        transform(RoundedCorners(cornerRadius))
                    }
                }
                .into(imageView)
        }
    }


}