package com.vatsal.master.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vatsal.master.R

fun getProgressDrawable(context : Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(url : String?, progressDrawable: CircularProgressDrawable ?){
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.dolphin)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(imageView: ImageView, url: String?){
    imageView.loadImage(url, getProgressDrawable(imageView.context))
}