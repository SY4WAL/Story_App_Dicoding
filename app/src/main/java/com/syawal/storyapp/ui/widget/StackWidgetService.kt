package com.syawal.storyapp.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.syawal.storyapp.di.Injection

class StackWidgetService : RemoteViewsService() {
    private val repository by lazy { Injection.provideRepository(this.applicationContext) }
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext, repository)
}