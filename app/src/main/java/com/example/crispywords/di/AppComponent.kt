package com.example.crispywords.di

import android.app.Application
import android.content.Context
import com.example.crispywords.CrispyApp
import com.example.crispywords.di.modules.ActivityModule
import com.example.crispywords.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules =[
        AndroidInjectionModule::class,
        ActivityModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<CrispyApp> {

    companion object {
        fun create(application: Application): AppComponent {
            return DaggerAppComponent.builder()
                .applicationContext(application.applicationContext)
                .build()
        }
    }

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun applicationContext(context: Context): Builder

        fun build(): AppComponent
    }

}
