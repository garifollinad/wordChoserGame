package com.example.crispywords.ui.main_page

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.crispywords.R

class LettersAdapter(val context: Context): BaseAdapter() {

    private var lettersList: List<Char> = arrayListOf()
    private var usedPositions: MutableSet<Int> = mutableSetOf()

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return lettersList.size
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val gridItem = LayoutInflater.from(context).inflate(R.layout.grid_item_view, null)

        val letterView: TextView = gridItem.findViewById(R.id.letterView)
        letterView.id = position
        letterView.text = lettersList[position].toString()

        return gridItem
    }

    fun setLetter(listOfLetter: List<Char>){
        lettersList = listOfLetter
        notifyDataSetChanged()
    }

    fun clearLetters(){
        lettersList = emptyList()
        notifyDataSetChanged()
    }

    fun clearUsedPosition() {
        usedPositions = mutableSetOf()
    }

    fun setWord(word: String) {
        val rand = generateRandom10Number()
        getAlgorithm(rand, word)
    }

    private fun leftToRight(word: String) {
        var index = generateRandom100Number()
        var emptyPosition = true

        while(emptyPosition){
            var cnt = 0
            var innerIndex = index
            loop@ for(i in 1..word.length){
                if(usedPositions.contains(innerIndex)){
                    cnt = 0
                    break@loop
                }
                innerIndex++
                cnt++
            }
            if((index % 10) + word.length <= 10 && cnt == word.length){
                emptyPosition = false
                break
            }
            val rand = generateRandom10Number()
            getAlgorithm(rand, word)
            break
        }

        if(!emptyPosition) {
            word.forEach { wordLetter ->
                (lettersList as MutableList<Char>)[index] = wordLetter
                usedPositions.add(index)
                ++index
                notifyDataSetChanged()
            }
        }
    }

    private fun rightToLeft(word: String) {
        var index = generateRandom100Number()
        var emptyPosition = true

        while(emptyPosition){
            var cnt = 0
            var innerIndex = index
            loop@ for(i in 1..word.length){
                if(usedPositions.contains(innerIndex)){
                    cnt = 0
                    break@loop
                }
                innerIndex--
                cnt++
            }
            if(index % 10 >= word.length - 1 && cnt == word.length){
                emptyPosition = false
                break
            }
            val rand = generateRandom10Number()
            getAlgorithm(rand, word)
            break
        }

        if(!emptyPosition) {
            word.forEach { wordLetter ->
                (lettersList as MutableList<Char>)[index] = wordLetter
                usedPositions.add(index)
                --index
                notifyDataSetChanged()
            }
        }
    }

    private fun verticalDown(word: String) {
        var index = generateRandom100Number()
        var emptyPosition = true

        while(emptyPosition){
            var cnt = 0
            var innerIndex = index
            loop@ for(i in 1..word.length){
                if(usedPositions.contains(innerIndex)){
                    cnt = 0
                    break@loop
                }
                innerIndex += 10
                cnt++
            }
            if((index / 10) + word.length <= 10 && cnt == word.length){
                emptyPosition = false
                break
            }
            val rand = generateRandom10Number()
            getAlgorithm(rand, word)
            break
        }

        if(!emptyPosition) {
            word.forEach { wordLetter ->
                (lettersList as MutableList<Char>)[index] = wordLetter
                usedPositions.add(index)
                index += 10
                notifyDataSetChanged()
            }
        }
    }

    private fun verticalUp(word: String) {
        var index = generateRandom100Number()
        var emptyPosition = true

        while(emptyPosition){
            var cnt = 0
            var innerIndex = index
            loop@ for(i in 1..word.length){
                if(usedPositions.contains(innerIndex)){
                    cnt = 0
                    break@loop
                }
                innerIndex -= 10
                cnt++
            }
            if((index / 10) - word.length >= 0 && cnt == word.length){
                emptyPosition = false
                break
            }
            val rand = generateRandom10Number()
            getAlgorithm(rand, word)
            break
        }

        if(!emptyPosition) {
            word.forEach { wordLetter ->
                (lettersList as MutableList<Char>)[index] = wordLetter
                usedPositions.add(index)
                index -= 10
                notifyDataSetChanged()
            }
        }
    }

    private fun diagonalDownRight(word: String) {
        var index = generateRandom100Number()
        var emptyPosition = true

        while(emptyPosition){
            var cnt = 0
            var innerIndex = index
            loop@ for(i in 1..word.length){
                if(usedPositions.contains(innerIndex)){
                    cnt = 0
                    break@loop
                }
                if(innerIndex >= 0 && innerIndex <= 99){
                    innerIndex += 11
                    cnt++
                }
            }

            loop@for(i in 0..9){
                if( (index % 10) + word.length <= 10 &&
                    ((index + i) % 11 == 0  || (index - i) % 11 == 0 ) &&
                    (index % 10) + word.length <= (10 - i) && cnt == word.length){
                    emptyPosition = false
                    break@loop
                }
            }
            if(!emptyPosition){
                break
            }
            val rand = generateRandom10Number()
            getAlgorithm(rand, word)
            break
        }

        if(!emptyPosition) {
            word.forEach { wordLetter ->
                (lettersList as MutableList<Char>)[index] = wordLetter
                usedPositions.add(index)
                index += 11
                notifyDataSetChanged()
            }
        }
    }

    private fun diagonalDownLeft(word: String) {
        var index = generateRandom100Number()
        var emptyPosition = true

        while(emptyPosition){
            var cnt = 0
            var innerIndex = index
            loop@ for(i in 1..word.length){
                if(usedPositions.contains(innerIndex)){
                    cnt = 0
                    break@loop
                }
                if(innerIndex >= 0 && innerIndex <= 99){
                    innerIndex += 9
                    cnt++
                }
            }

            loop@for(i in 0..9){
                if( index % 10 >= word.length - 1 &&
                    ((index + i) % 11 == 0  || (index - i) % 11 == 0 ) &&
                    (index % 10) + word.length <= (10 - i) && cnt == word.length){
                    emptyPosition = false
                    break@loop
                }
            }
            if(!emptyPosition){
                break
            }
            val rand = generateRandom10Number()
            getAlgorithm(rand, word)
            break
        }

        if(!emptyPosition) {
            word.forEach { wordLetter ->
                (lettersList as MutableList<Char>)[index] = wordLetter
                usedPositions.add(index)
                index += 9
                notifyDataSetChanged()
            }
        }
    }

    private fun diagonalUpRight(word: String) {
        var index = generateRandom100Number()
        var emptyPosition = true

        while(emptyPosition){
            var cnt = 0
            var innerIndex = index
            loop@ for(i in 1..word.length){
                if(usedPositions.contains(innerIndex)){
                    cnt = 0
                    break@loop
                }
                if(innerIndex >= 0 && innerIndex <= 99){
                    innerIndex -= 9
                    cnt++
                }
            }

            loop@for(i in 0..9){
                if( (index % 10) + word.length <= 10 &&
                    ((index + i) % 11 == 0  || (index - i) % 11 == 0 ) &&
                    (index % 10) + word.length <= (10 - i) && cnt == word.length){
                    emptyPosition = false
                    break@loop
                }
            }
            if(!emptyPosition){
                break
            }
            val rand = generateRandom10Number()
            getAlgorithm(rand, word)
            break
        }

        if(!emptyPosition) {
            word.forEach { wordLetter ->
                (lettersList as MutableList<Char>)[index] = wordLetter
                usedPositions.add(index)
                index -= 9
                notifyDataSetChanged()
            }
        }
    }

    private fun diagonalUpLeft(word: String) {
        var index = generateRandom100Number()
        var emptyPosition = true

        while(emptyPosition){
            var cnt = 0
            var innerIndex = index
            loop@ for(i in 1..word.length){
                if(usedPositions.contains(innerIndex)){
                    cnt = 0
                    break@loop
                }
                if(innerIndex >= 0 && innerIndex <= 99){
                    innerIndex -= 11
                    cnt++
                }
            }

            loop@for(i in 0..9){
                if( index % 10 >= word.length - 1 &&
                    ((index + i) % 11 == 0  || (index - i) % 11 == 0 ) &&
                    (index % 10) + word.length <= (10 - i) && cnt == word.length){
                    emptyPosition = false
                    break@loop
                }
            }
            if(!emptyPosition){
                break
            }
            val rand = generateRandom10Number()
            getAlgorithm(rand, word)
            break
        }

        if(!emptyPosition) {
            word.forEach { wordLetter ->
                (lettersList as MutableList<Char>)[index] = wordLetter
                usedPositions.add(index)
                index -= 11
                notifyDataSetChanged()
            }
        }
    }

    private fun getAlgorithm(position: Int, word: String){
        when(position){
            1 -> leftToRight(word)
            2 -> rightToLeft(word)
            3 -> verticalDown(word)
            4 -> verticalUp(word)
            5 -> diagonalDownRight(word)
            6 -> diagonalDownLeft(word)
            7 -> diagonalUpRight(word)
            8 -> diagonalUpLeft(word)
        }
    }

    private fun generateRandom100Number(): Int{
        val index = ((0..99).random())
        return index
    }

    private fun generateRandom10Number(): Int{
        val index = ((1..8).random())
        return index
    }

}