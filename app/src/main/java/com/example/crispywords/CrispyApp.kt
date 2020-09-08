package com.example.crispywords

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.crispywords.di.AppComponent
import com.example.crispywords.di.Injectable
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class CrispyApp: Application(), HasAndroidInjector, Application.ActivityLifecycleCallbacks {

    private val appComponent by lazy {
        AppComponent.create(this)
    }

    @Inject
    internal lateinit var dispatchingAndroidInjectorAny: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        registerActivityLifecycleCallbacks(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjectorAny

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is HasAndroidInjector) {
            AndroidInjection.inject(activity)

        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager
                .registerFragmentLifecycleCallbacks(
                    object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(
                            fm: FragmentManager,
                            f: Fragment,
                            savedInstanceState: Bundle?
                        ) {
                            if (f is Injectable) {
                                AndroidSupportInjection.inject(f)
                            }
                        }
                    }, true
                )
        }
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}
}