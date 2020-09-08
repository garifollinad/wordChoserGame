package com.example.crispywords.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.crispywords.R
import com.example.crispywords.ui.main_page.MainPageFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            navigateTo(
                fragment = MainPageFragment.newInstance(),
                tag = Screen.MAIN_PAGE.name,
                addToStack = true
            )
        }
    }

    fun navigateTo(
        fragment: Fragment,
        tag: String,
        addToStack: Boolean = false
    ) {
        changeFragment(
            layoutId = R.id.menuContainer,
            fragment = fragment,
            tagFragmentName = tag,
            addToStack = addToStack
        )
    }
}