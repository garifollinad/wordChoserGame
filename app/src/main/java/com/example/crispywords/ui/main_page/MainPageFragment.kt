package com.example.crispywords.ui.main_page

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import com.example.crispywords.R
import com.example.crispywords.base.MainActivity
import com.example.crispywords.base.Screen
import com.example.crispywords.di.Injectable
import com.example.crispywords.ui.add_word_page.AddWordFragment
import com.example.crispywords.ui.add_word_page.NewWordListener
import com.example.crispywords.utils.Constants
import com.example.crispywords.utils.widgets.LineView
import com.example.crispywords.utils.widgets.SearchWordView
import com.example.crispywords.utils.widgets.ToolbarView
import java.lang.Math.abs
import java.lang.Math.ceil
import java.lang.StringBuilder
import javax.inject.Inject

class MainPageFragment : Fragment(), Injectable {

    companion object {
        fun newInstance(data: Bundle? = null): MainPageFragment =
            MainPageFragment().apply {
                arguments = data
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MainPageViewModel::class.java)
    }

    private lateinit var gridView: GridView
    private lateinit var gridLayout: FrameLayout
    private lateinit var foundGridLayout: FrameLayout
    private lateinit var toolbar: ToolbarView
    private lateinit var wordsContainer: GridLayout
    private var positionValues: MutableSet<List<Int>> = mutableSetOf()
    private var initPos: Int = -1
    private var lastPos: Int? = -1
    private var startX: Int? = -1
    private var startY: Int? = -1
    private var endX: Int? = -1
    private var endY: Int? = -1
    private var direction: Int? = null
    private var words: HashMap<String, Boolean> = HashMap()
    private var foundWordsCnt = 0

    private val lettersAdapter by lazy {
        context?.let { LettersAdapter(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setAdapter()
        setData()
    }

    private fun bindViews(view: View) = with(view) {
        gridView = findViewById(R.id.gridView)
        foundGridLayout = findViewById(R.id.foundGridLayout)
        gridLayout = findViewById(R.id.gridLayout)
        toolbar = findViewById(R.id.toolbar)
        wordsContainer = findViewById(R.id.wordsContainer)
        toolbar.setToolbarText(getString(R.string.app_name))

        toolbar.getRefresh().setOnClickListener {
            lettersAdapter?.clearLetters()
            words = HashMap()
            lettersAdapter?.clearUsedPosition()
            setAdapter()
            wordsContainer.removeAllViews()
            setSearchWords()
            foundGridLayout.removeViews(1, foundGridLayout.childCount - 1)
            foundWordsCnt = 0
        }

        toolbar.getAdd().setOnClickListener {
            val addWordFragment = AddWordFragment.newInstance()
            (context as MainActivity).navigateTo(
                fragment = addWordFragment,
                tag = Screen.ADD_WORD.name,
                addToStack = true)

            val newWordListener: NewWordListener = object :
                NewWordListener {
                override fun onAddedWord(newWord: String) {
                    lettersAdapter?.clearLetters()
                    words = HashMap()
                    lettersAdapter?.clearUsedPosition()
                    setAdapter()
                    wordsContainer.removeAllViews()
                    viewModel.addSearchWords(newWord)
                    setSearchWords()
                    foundGridLayout.removeViews(1, foundGridLayout.childCount - 1)
                    foundWordsCnt = 0
                }
            }
            addWordFragment.setNewWordListener(newWordListener)
        }
}

    private fun setAdapter() {
        gridView.adapter = lettersAdapter
        lettersAdapter?.setLetter(viewModel.getRandomLetter())
        setWords()
    }

    private fun setWords()  {
        viewModel.getSearchWords().forEach { word ->
            words.put(word, false)
            lettersAdapter?.setWord(word)
        }
    }

    private fun setSearchWords() {
        viewModel.getSearchWords().forEach { searchWord ->
            val searchWordView = SearchWordView(requireContext())
            searchWordView.setSearchWordText(searchWord)
            searchWordView.tag = searchWord
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(10, 16, 10, 16)
            searchWordView.setLayoutParams(params)
            wordsContainer.addView(searchWordView)
        }
    }

    private fun setData() {
        setSearchWords()

        val onTouchListener = View.OnTouchListener { v: View, event: MotionEvent ->

            val actionMasked: Int = event.actionMasked

            val gridView: GridView = v as GridView

            val position: Int = gridView.pointToPosition(event.x.toInt(), event.y.toInt())

            if (position in 0..99)
            {
                val item: TextView = gridView.findViewById(position)

                val rect = Rect()

                item.getDrawingRect(rect)

                gridView.offsetDescendantRectToMyCoords(item, rect)

                endX = rect.centerX() + item.width / 2
                endY = rect.centerY() + item.height / 2

                when (actionMasked)
                {
                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_MOVE ->
                    {
                        v.parent.requestDisallowInterceptTouchEvent(true)
                        when (actionMasked)
                        {
                            MotionEvent.ACTION_DOWN ->
                            {
                                startX = rect.centerX() + item.width / 2
                                startY = rect.centerY() + item.height / 2

                                initPos = position
                                lastPos = null
                            }
                            MotionEvent.ACTION_MOVE -> {
                                findDirection(
                                    checkNotNull(startX),
                                    checkNotNull(startY),
                                    checkNotNull(endX),
                                    checkNotNull(endY))
                                lastPos = position
                            }
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        buildWord(
                            initPos,
                            lastPos,
                            checkNotNull(startX),
                            checkNotNull(startY),
                            checkNotNull(endX),
                            checkNotNull(endY)
                        )
                    }
                }
            }
            else if (position == -1 && actionMasked == MotionEvent.ACTION_UP)
            {
                buildWord(
                    initPos,
                    lastPos,
                    checkNotNull(startX),
                    checkNotNull(startY),
                    checkNotNull(endX),
                    checkNotNull(endY)
                )
            }

            return@OnTouchListener true
        }

        gridView.setOnTouchListener(onTouchListener)
    }

    private fun findDirection(startX: Int, startY: Int, endX: Int, endY: Int){
        if(startX == endX && startY < endY){
            if(direction == null || direction == Constants.VERTICAL_DOWN) {
                direction = Constants.VERTICAL_DOWN
                drawLine(startX, startY, endX, endY)
            }
        }
        else if(startX == endX && startY > endY){
            if(direction == null || direction == Constants.VERTICAL_UP) {
                direction = Constants.VERTICAL_UP
                drawLine(startX, startY, endX, endY)
            }
        }
        else if(startY == endY && startX < endX){
            if(direction == null || direction == Constants.LEFT_TO_RIGHT) {
                direction = Constants.LEFT_TO_RIGHT
                drawLine(startX, startY, endX, endY)
            }
        }
        else if(startY == endY && startX > endX){
            if(direction == null || direction == Constants.RIGHT_TO_LEFT) {
                direction = Constants.RIGHT_TO_LEFT
                drawLine(startX, startY, endX, endY)
            }
        }
        else if((endX - startX) / 100 == (endY - startY) / 94 && (endY - startY) > 0){
            if(direction == null || direction == Constants.DIAGONAL_DOWN_RIGHT) {
                direction = Constants.DIAGONAL_DOWN_RIGHT
                drawLine(startX, startY, endX, endY)
            }
        }
        else if(abs(endX - startX) / 100 == (endY - startY) / 94 && (endX - startX) < 0){
            if( direction == Constants.DIAGONAL_DOWN_LEFT || direction == null) {
                direction = Constants.DIAGONAL_DOWN_LEFT
                drawLine(startX, startY, endX, endY)
            }
        }
        else if((endX - startX) / 100 == abs(endY - startY) / 94 && (endY - startY) < 0){
            if(direction == null || direction == Constants.DIAGONAL_UP_RIGHT) {
                direction = Constants.DIAGONAL_UP_RIGHT
                drawLine(startX, startY, endX, endY)
            }
        }
        else if(abs(endX - startX) / 100 == abs(endY - startY) / 94  && (endX - startX) < 0){
            if(direction == null || direction == Constants.DIAGONAL_UP_LEFT) {
                direction = Constants.DIAGONAL_UP_LEFT
                drawLine(startX, startY, endX, endY)
            }
        }
    }

    private fun drawLine(startX: Int, startY: Int, endX: Int, endY: Int){
        val lineView = LineView(context)
        lineView.setPosition(
            Color.RED,
            endX.toFloat() - 40f,
            endY.toFloat() - 40f,
            startX.toFloat() - 40f,
            startY.toFloat() - 40f)
        val list: MutableList<Int> = arrayListOf()
        list.add(startX)
        list.add(startY)
        list.add(endX)
        list.add(endY)
        if(!positionValues.contains(list)){
            gridLayout.addView(lineView)
        }
        positionValues.add(list)
    }

    private fun removeViewOrHighlight(
        word: String,
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int
    ){
        gridLayout.removeViews(1, gridLayout.childCount - 1)
        if(checkNotNull(words.keys).contains(word)  && words.getValue(word) == false)  {
            val lineView = LineView(context)
            lineView.setPosition(
                Color.GREEN,
                endX.toFloat() - 40f,
                endY.toFloat() - 40f,
                startX.toFloat() - 40f,
                startY.toFloat() - 40f)
            foundGridLayout.addView(lineView)
            words.set(word, true)
            foundWordsCnt++
            val searchWordView = wordsContainer.findViewWithTag(word) as SearchWordView
            searchWordView.getSearchWord().setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)

            if(foundWordsCnt == checkNotNull(words).size){
                winPrompt()
            }
        }
    }

    private fun buildWord(initP: Int, lastP: Int?, startX: Int, startY: Int, endX: Int, endY: Int) {
        val builtWord = StringBuilder()
        var cnt = 0

        when(direction){
            Constants.VERTICAL_DOWN -> {
                for(i in initP ..checkNotNull(lastP) step 10){
                    i.let {
                        val cellView: TextView = gridView.findViewById(it)
                        builtWord.append(cellView.text)
                        ++cnt
                    }
                }
            }
            Constants.VERTICAL_UP -> {
                for(i in initP downTo checkNotNull(lastP) step 10){
                    i.let {
                        val cellView: TextView = gridView.findViewById(it)
                        builtWord.append(cellView.text)
                        ++cnt
                    }
                }
            }
            Constants.LEFT_TO_RIGHT -> {
                for(i in initP .. checkNotNull(lastP)){
                    i.let {
                        val cellView: TextView = gridView.findViewById(it)
                        builtWord.append(cellView.text)
                        ++cnt
                    }
                }
            }
            Constants.RIGHT_TO_LEFT -> {
                for(i in initP downTo checkNotNull(lastP)){
                    i.let {
                        val cellView: TextView = gridView.findViewById(it)
                        builtWord.append(cellView.text)
                        ++cnt
                    }
                }
            }
            Constants.DIAGONAL_DOWN_RIGHT -> {
                for(i in initP .. checkNotNull(lastP) step 11){
                    i.let {
                        val cellView: TextView = gridView.findViewById(it)
                        builtWord.append(cellView.text)
                        ++cnt
                    }
                }
            }
            Constants.DIAGONAL_DOWN_LEFT -> {
                for(i in initP .. checkNotNull(lastP) step 9){
                    i.let {
                        val cellView: TextView = gridView.findViewById(it)
                        builtWord.append(cellView.text)
                        ++cnt
                    }
                }
            }
            Constants.DIAGONAL_UP_RIGHT -> {
                for(i in initP downTo checkNotNull(lastP) step 9){
                    i.let {
                        val cellView: TextView = gridView.findViewById(it)
                        builtWord.append(cellView.text)
                        ++cnt
                    }
                }
            }
            Constants.DIAGONAL_UP_LEFT -> {
                for(i in initP downTo checkNotNull(lastP) step 11){
                    i.let {
                        val cellView: TextView = gridView.findViewById(it)
                        builtWord.append(cellView.text)
                        ++cnt
                    }
                }
            }
        }
        direction = null
        removeViewOrHighlight(
            builtWord.toString(),
            startX,
            startY,
            endX,
            endY
        )
    }

    private fun winPrompt() {
        lateinit var alertText: TextView
        lateinit var close: ImageView
        lateinit var dialog: Dialog
        val alertDialog = AlertDialog.Builder(context)
        val mainView = LayoutInflater.from(context).inflate(R.layout.win_prompt_view, null)
        alertText = mainView.findViewById(R.id.alertText)
        close = mainView.findViewById(R.id.close)
        dialog = alertDialog.setView(mainView)
            .setCancelable(false)
            .create()
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        alertText.setOnClickListener {
            dialog?.dismiss()
        }

        close.setOnClickListener {
            dialog?.dismiss()
        }
    }

}