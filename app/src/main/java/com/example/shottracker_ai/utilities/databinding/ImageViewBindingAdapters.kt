package com.example.shottracker_ai.utilities.databinding

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.File


object ImageViewBindingAdapters {

    @JvmStatic
    @BindingAdapter(
            value = ["imageUri", "circleCropTransformation", "imageCornerRadius", "noCache"],
            requireAll = false
    )
    fun setImageWithGlide(
            imageView: ImageView,
            imageUri: Uri?,
            circleCropTransformation: Boolean? = null,
            imageCornerRadius: Float? = null,
            noCache: Boolean? = null
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

                    if (noCache == true) diskCacheStrategy(DiskCacheStrategy.NONE)
                    if (noCache == true) skipMemoryCache(true)

                }
                .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("tintRes")
    fun setForegroundTint(imageView: ImageView, @ColorRes tintColorRes: Int?) {
        with(imageView) {
            if (tintColorRes != null && tintColorRes != 0) {
                imageTintList = ColorStateList.valueOf(context.getColor(tintColorRes))
            } else {
                imageTintList = null
            }
        }
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageUri(view: ImageView, imageUri: String?) {
        if (imageUri == null) {
            view.setImageURI(null)
        } else {
            view.setImageURI(Uri.parse(imageUri))
        }
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageUri(view: ImageView, imageUri: Uri?) {
        view.setImageURI(imageUri)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageDrawable(view: ImageView, drawable: Drawable?) {
        view.setImageDrawable(drawable)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

}