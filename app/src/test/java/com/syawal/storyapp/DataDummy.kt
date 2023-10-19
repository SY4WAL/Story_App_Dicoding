package com.syawal.storyapp

import com.syawal.storyapp.data.local.entity.StoryEntity

object DataDummy {
    fun generateDummyStoryEntity() : List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..15) {
            val story = StoryEntity(
                i.toString(),
                "photo  + $i",
                "name + $i",
                "desc + $i",
                "created + $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}