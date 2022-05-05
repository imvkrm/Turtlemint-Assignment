package com.vikram.turtlemintassignment.ui.info

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.vikram.turtlemintassignment.data.local.CommentsItems
import com.vikram.turtlemintassignment.data.local.IssuesItems
import com.vikram.turtlemintassignment.databinding.LayoutInfoCommentsBinding
import com.vikram.turtlemintassignment.databinding.LayoutIssuesItemsBinding
import com.vikram.turtlemintassignment.other.formattedDate
import com.vikram.turtlemintassignment.other.smartTruncate
import javax.inject.Inject


class CommentsAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<CommentsAdapter.CommentsItemViewHolder>() {

    class CommentsItemViewHolder(private val itemBinding: LayoutInfoCommentsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {


        fun bind(commentItem: CommentsItems, glide: RequestManager) {
            itemBinding.apply {
                glide.load(commentItem.avatar).into(ivAvatarComments)
                tvNameComments.text = commentItem.name
                tvDescriptionComments.text = commentItem.description.smartTruncate(200)
                tvDateComments.text = formattedDate(commentItem.date)
            }


        }
    }


    private val diffCallback = object : DiffUtil.ItemCallback<CommentsItems>() {

        override fun areItemsTheSame(oldItem: CommentsItems, newItem: CommentsItems): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CommentsItems, newItem: CommentsItems): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var commentsItems: List<CommentsItems>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsItemViewHolder {

        val binding =
            LayoutInfoCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentsItemViewHolder(
            binding
        )
    }



    override fun onBindViewHolder(holder: CommentsItemViewHolder, position: Int) {
        val commentItem = commentsItems[position]

        holder.bind(commentItem, glide)


    }

    override fun getItemCount(): Int {
        return commentsItems.size
    }
}







