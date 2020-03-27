package com.example.jetpack.utilities

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import java.text.NumberFormat


@BindingAdapter(value = ["imageUrl", "placeHolder", "error", "round"], requireAll = false)
fun loadImage(
    imageView: ImageView,
    url: String?,
    holder: Drawable?,
    error: Drawable?,
    round: Boolean = false
) {
    if (url == null) {
        imageView.setImageDrawable(holder)
    } else {
        val glide = Glide.with(imageView).load(url)
            .placeholder(holder)
            .error(error)
        if (round) glide.apply(RequestOptions.bitmapTransform(CircleCrop()))
        glide.into(imageView)
    }
}


@BindingAdapter(value = ["thousandNum"])
fun transformThousandNum(textView: TextView, number: String?) {
    //不对number做非空校验，可能会引起空指针异常
    if (number.isNullOrEmpty()) return
    val numStr = try {
        NumberFormat.getNumberInstance().format(number.toLong())
    } catch (e: Exception) {
        ""
    }
    textView.text = "₹$numStr"
}
