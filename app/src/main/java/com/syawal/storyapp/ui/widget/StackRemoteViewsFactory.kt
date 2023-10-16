package com.syawal.storyapp.ui.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.syawal.storyapp.R
import com.syawal.storyapp.data.local.StoryDatabase
import com.syawal.storyapp.data.local.entity.StoryEntity
import com.syawal.storyapp.ui.detail.DetailFragment.Companion.EXTRA_ID
import com.syawal.storyapp.ui.widget.StoryWidget.Companion.EXTRA_ITEM

internal class StackRemoteViewsFactory(
    private val context: Context,
) : RemoteViewsService.RemoteViewsFactory {

    private var storyList = listOf<StoryEntity>()
    private val storyBitmap = arrayListOf<Bitmap>()


    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val db = StoryDatabase.getInstance(context)
        storyList = db.storyDao().getWidgetStory()
        Log.d("widget", storyList.toString())

        val bitmap = storyList.map {
            Glide.with(context)
                .asBitmap()
                .load(it.photoUrl)
                .submit()
                .get()
        }

        storyBitmap.clear()
        storyBitmap.addAll(bitmap)

        Log.d("widget data", bitmap.toString())
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = storyList.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.iv_story, storyBitmap[position])

        val fillInIntent = Intent().apply {
            action = EXTRA_ITEM
            putExtra(EXTRA_ID, storyList[position].id)
        }

        rv.setOnClickFillInIntent(R.id.iv_story, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

}