package com.example.crispywords.ui.add_word_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.crispywords.R
import com.example.crispywords.base.MainActivity
import com.example.crispywords.di.Injectable
import com.example.crispywords.ui.main_page.MainPageViewModel
import com.example.crispywords.utils.widgets.ToolbarView
import javax.inject.Inject

interface NewWordListener {
    fun onAddedWord(newWord: String)
}

class AddWordFragment : Fragment(), Injectable {

    companion object {
        fun newInstance(data: Bundle? = null): AddWordFragment =
            AddWordFragment().apply {
                arguments = data
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MainPageViewModel::class.java)
    }

    private lateinit var toolbar: ToolbarView
    private lateinit var wordEdit: EditText
    private lateinit var buttonSave: Button
    private var newWordListener: NewWordListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_word, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
    }

    private fun bindViews(view: View) = with(view) {
        toolbar = findViewById(R.id.toolbar)
        wordEdit = findViewById(R.id.wordEdit)
        buttonSave = findViewById(R.id.buttonSave)
        toolbar.invisibleRefreshAndAdd()
        toolbar.getBackArrow().setOnClickListener {
            (context as MainActivity).onBackPressed()
        }

        buttonSave.setOnClickListener {
            newWordListener?.onAddedWord(wordEdit.text.toString())
        }
    }

    fun setNewWordListener(newWord: NewWordListener) {
        newWordListener = newWord
    }

}