package com.example.jetpack.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.jetpack.R

class EligibilityView : ConstraintLayout {

    private lateinit var textView: TextView

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_eligibility, this, true)
        textView = view.findViewById(R.id.text)
    }

    fun setText(str: String): EligibilityView {
        textView.text = str
        return this
    }
}