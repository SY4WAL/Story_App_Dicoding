package com.syawal.storyapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syawal.storyapp.data.local.StoryEntity
import com.syawal.storyapp.databinding.ItemStoriesBinding
import com.syawal.storyapp.ui.home.HomeFragmentDirections

class StoryAdapter: ListAdapter<StoryEntity, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

//    private lateinit var onItemClickCallback: OnItemClickCallback
//
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }

    inner class MyViewHolder(val binding: ItemStoriesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryEntity) {
            binding.tvItemName.text = story.name
            binding.tvItemDesc.text = story.description

            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)

            binding.root.setOnClickListener {
//                onItemClickCallback.onItemClicked(story)
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                action.idStory = story.id
                val extras = FragmentNavigatorExtras(binding.ivItemPhoto to "photo")
                itemView.findNavController().navigate(action, extras)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)

        holder.bind(story)
    }

//    interface OnItemClickCallback {
//        fun onItemClicked(story: StoryEntity)
//    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}