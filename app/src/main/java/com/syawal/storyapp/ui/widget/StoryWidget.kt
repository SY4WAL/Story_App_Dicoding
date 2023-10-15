package com.syawal.storyapp.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.syawal.storyapp.R
import com.syawal.storyapp.ui.MainActivity
import com.syawal.storyapp.ui.detail.DetailFragment
import com.syawal.storyapp.ui.detail.DetailFragment.Companion.EXTRA_ID


class StoryWidget : AppWidgetProvider() {
    companion object {
        private const val INTENT_ACTION = "com.syawal.storyapp.INTENT_ACTION"
        const val EXTRA_ITEM = "com.syawal.storyapp.EXTRA_ITEM"

        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.story_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val actionIntent = Intent(context, StoryWidget::class.java)
            actionIntent.action = INTENT_ACTION
            actionIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            val actionPendingIntent = PendingIntent.getBroadcast(
                context, 0, actionIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else 0
            )
            views.setPendingIntentTemplate(R.id.stack_view, actionPendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (INTENT_ACTION == intent.action) {
            val storyId = intent.getStringExtra(EXTRA_ID)
//            val toDetailIntent = Intent(context, MainActivity::class.java).apply {
//                putExtra(EXTRA_ID, storyId)
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            }
//            context.startActivity(toDetailIntent)

//            if (storyId != null) {
//                val detailFragment = DetailFragment.newInstance(storyId)
//                val fragmentManager = (context as AppCompatActivity).supportFragmentManager
//                fragmentManager.beginTransaction()
//                    .replace(R.id.detailFragment, detailFragment)
//                    .addToBackStack(null)
//                    .commit()
//            }

            if (storyId != null) {
                val navIntent = NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.main_navigation)
                    .setDestination(R.id.detailFragment)
                    .setArguments(bundleOf(EXTRA_ID to storyId))
                    .createTaskStackBuilder()
                    .intents[0]
                context.startActivity(navIntent)
            }
        }
    }
}