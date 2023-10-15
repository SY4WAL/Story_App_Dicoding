package com.syawal.storyapp.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.syawal.storyapp.di.Injection

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}