package com.example.crispywords.utils.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.crispywords.R

class ToolbarView : LinearLayout {

    private lateinit var ivBack: ImageView
    private lateinit var tvAdd: TextView
    private lateinit var ivRefresh: ImageView
    private lateinit var tvTitleToolbar: TextView
    private lateinit var toolbar: Toolbar

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    fun getBackArrow(): ImageView {
        return ivBack
    }

    fun getAdd(): TextView {
        return tvAdd
    }

    fun getRefresh(): ImageView {
        return ivRefresh
    }

    fun setToolbarText(title: String) {
        tvTitleToolbar.text = title
    }

    fun invisibleRefreshAndAdd() {
        ivRefresh.visibility = View.GONE
        tvAdd.visibility = View.GONE
    }

    private fun init(context: Context) {
        inflate(context, R.layout.toolbar_view, this)
        ivBack = findViewById(R.id.ivBack)
        tvAdd = findViewById(R.id.tvAdd)
        ivRefresh = findViewById(R.id.ivRefresh)
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar)
        toolbar = findViewById(R.id.customToolbar)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        init(context)
    }
}
