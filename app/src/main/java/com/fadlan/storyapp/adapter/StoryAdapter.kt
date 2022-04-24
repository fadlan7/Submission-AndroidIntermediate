package com.fadlan.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadlan.storyapp.data.remote.response.ListStoryItem
import com.fadlan.storyapp.databinding.StoryItemBinding
import com.fadlan.storyapp.helper.Constanta.EXTRA_CAPTION
import com.fadlan.storyapp.helper.Constanta.EXTRA_IMAGE
import com.fadlan.storyapp.helper.Constanta.EXTRA_USER_NAME
import com.fadlan.storyapp.helper.StoryDiffCallback
import com.fadlan.storyapp.ui.detail.DetailActivity

class StoryAdapter :
    RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

    private var storyList = ArrayList<ListStoryItem>()

    fun initUserData(data: ArrayList<ListStoryItem>) {
        storyList.apply {
            clear()
            addAll(data)
        }

        val diffCallback = StoryDiffCallback(this.storyList, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            StoryItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(storyList[position])
    }

    override fun getItemCount(): Int = storyList.size

    class ListViewHolder(private val view: StoryItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(data: ListStoryItem) {
            view.apply {
                tvUserName.text = data.name
                tvCaption.text = data.description
            }

            Glide.with(itemView.context)
                .load(data.photoUrl)
                .circleCrop()
                .into(view.ivStory)

            itemView.setOnClickListener {
                val moveToDetail = Intent(itemView.context, DetailActivity::class.java)

                moveToDetail.putExtra(EXTRA_USER_NAME, data.name)
                moveToDetail.putExtra(EXTRA_CAPTION, data.description)
                moveToDetail.putExtra(EXTRA_IMAGE, data.photoUrl)

//                itemView.context.startActivity(moveToDetail)
                itemView.context.startActivity(moveToDetail, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
            }
        }
    }
}