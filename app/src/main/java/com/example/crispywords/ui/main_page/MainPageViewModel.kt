package com.example.crispywords.ui.main_page

import androidx.lifecycle.ViewModel
import com.example.crispywords.utils.Constants
import javax.inject.Inject

class MainPageViewModel @Inject constructor(): ViewModel() {

    val words = mutableListOf("SWIFT", "KOTLIN", "OBJECTIVEC",
        "VARIABLE", "JAVA", "MOBILE")

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

        for(i in 0 until words.size){
            listOfSearchWords.add(words.get(i))
        }
        return listOfSearchWords
    }

    fun addSearchWords(newWord: String) {
        words.add(newWord)
    }
}