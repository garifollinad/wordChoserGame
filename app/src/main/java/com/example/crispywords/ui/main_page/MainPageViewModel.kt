package com.example.crispywords.ui.main_page

import androidx.lifecycle.ViewModel
import com.example.crispywords.utils.Constants
import javax.inject.Inject

class MainPageViewModel @Inject constructor(): ViewModel() {

    fun getRandomLetter(): List<Char> {

        val listOfRandomLetter: ArrayList<Char> = arrayListOf()

        for(i in 0 until 100){
            val index = (Math.random() * 26).toInt()
            listOfRandomLetter.add(Constants.LETTERS.get(index))
        }
        return listOfRandomLetter
    }

    fun getSearchWords(): List<String> {

        val listOfSearchWords: ArrayList<String> = arrayListOf()

        for(i in 0 until Constants.WORDS.size){
            listOfSearchWords.add(Constants.WORDS.get(i))
        }
        return listOfSearchWords
    }
}