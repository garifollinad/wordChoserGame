package com.example.crispywords.di.modules

import com.example.crispywords.ui.add_word_page.AddWordFragment
import com.example.crispywords.ui.main_page.MainPageFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMainPageFragment(): MainPageFragment

    @ContributesAndroidInjector
    abstract fun contributeAddWordFragment(): AddWordFragment

}