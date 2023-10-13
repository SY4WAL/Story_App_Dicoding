package com.syawal.storyapp.ui.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.syawal.storyapp.R
import com.syawal.storyapp.data.Repository
import com.syawal.storyapp.data.ResultState
import com.syawal.storyapp.data.api.response.ListStoryItem
import com.syawal.storyapp.ui.detail.DetailFragment.Companion.EXTRA_ID
import com.syawal.storyapp.ui.widget.StoryWidget.Companion.EXTRA_ITEM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class StackRemoteViewsFactory(
    private val context: Context,
    private val repository: Repository
): RemoteViewsService.RemoteViewsFactory {

    private val storyList = arrayListOf<ListStoryItem>()
    private val storyBitmap = arrayListOf<Bitmap>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = repository.getSession().toString()
                val response = repository.getStories().value
                when (response) {
                    is ResultState.Success -> {
                        val stories = response.data
                        val bitmap = stories.map {
                            Glide.with(context)
                                .asBitmap()
                                .load(it.photoUrl)
                                .submit()
                                .get()
                        }

                        storyBitmap.clear()
                        storyList.clear()
                        storyBitmap.addAll(bitmap)
                        storyList.addAll(stories)

                        Log.d("Widget stack", "storyList size: ${storyList.size}")
                    }
                    is ResultState.Error -> {
                        val message = response.error
                        Log.e("Widget stack", "Error loading stories: $message")
                    }

                    is ResultState.Loading -> {
                        Log.d("Widget", "Still loading")
                    }

                    else -> {
                        Log.d("Widget", "Unhandled response state: $response")
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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