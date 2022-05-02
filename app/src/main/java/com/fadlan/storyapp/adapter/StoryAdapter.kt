package com.fadlan.storyapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadlan.storyapp.data.remote.response.ListStoryItem
import com.fadlan.storyapp.databinding.StoryItemBinding
import com.fadlan.storyapp.helper.Constanta.EXTRA_CAPTION
import com.fadlan.storyapp.helper.Constanta.EXTRA_IMAGE
import com.fadlan.storyapp.helper.Constanta.EXTRA_UPLOAD_DATE
import com.fadlan.storyapp.helper.Constanta.EXTRA_USER_NAME
import com.fadlan.storyapp.helper.setLocalDateFormat
import com.fadlan.storyapp.ui.detail.DetailActivity

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DiffCallback) {
    class ListViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, data: ListStoryItem) {
            binding.apply {
                tvUserName.text = data.name
                tvCaption.text = data.description
                tvUploadDate.setLocalDateFormat(data.createdAt)
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(ivStory)

                root.setOnClickListener {
                    val moveToDetail = Intent(itemView.context, DetailActivity::class.java)

                    moveToDetail.putExtra(EXTRA_USER_NAME, data.name)
                    moveToDetail.putExtra(EXTRA_UPLOAD_DATE, data.createdAt)
                    moveToDetail.putExtra(EXTRA_CAPTION, data.description)
                    moveToDetail.putExtra(EXTRA_IMAGE, data.photoUrl)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivStory, "story_image"),
                            Pair(tvUploadDate, "upload_date"),
                            Pair(tvUserName, "user_name"),
                            Pair(tvCaption, "caption"),
                        )

                    itemView.context.startActivity(moveToDetail, optionsCompat.toBundle())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(holder.itemView.context, story)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}