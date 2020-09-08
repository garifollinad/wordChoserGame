package com.example.crispywords.utils.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.crispywords.R

class SearchWordView : ConstraintLayout {

    private lateinit var searchWord: TextView

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.search_word_view, this)
        searchWord = findViewById(R.id.searchWord)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        init(context)
    }

    fun setSearchWordText (data: String) {
        searchWord.text = data
    }

    fun getSearchWord(): TextView {
        return searchWord
    }

}