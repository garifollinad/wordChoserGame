package com.example.crispywords.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crispywords.di.DaggerViewModelFactory
import com.example.crispywords.di.ViewModelKey
import com.example.crispywords.ui.main_page.MainPageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(
        factory: DaggerViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainPageViewModel::class)
    internal abstract fun provideMainPageViewModel(viewModel: MainPageViewModel): ViewModel

}