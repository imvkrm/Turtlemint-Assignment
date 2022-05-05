package com.vikram.turtlemintassignment.ui.issues

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.vikram.turtlemintassignment.data.local.IssuesItems
import com.vikram.turtlemintassignment.databinding.LayoutIssuesItemsBinding
import com.vikram.turtlemintassignment.other.formattedDate
import com.vikram.turtlemintassignment.other.smartTruncate
import javax.inject.Inject


class IssuesAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<IssuesAdapter.IssuesItemViewHolder>() {

    class IssuesItemViewHolder(private val itemBinding: LayoutIssuesItemsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {


        fun bind(issuesItem: IssuesItems, glide: RequestManager) {
            itemBinding.apply {
                glide.load(issuesItem.avatar).into(ivAvatar)
                tvName.text = issuesItem.name
                tvTitle.text = issuesItem.title
                tvDescription.text = issuesItem.description.smartTruncate(200)
                tvDate.text = formattedDate(issuesItem.date)
            }


        }
    }


    private val diffCallback = object : DiffUtil.ItemCallback<IssuesItems>() {

        override fun areItemsTheSame(oldItem: IssuesItems, newItem: IssuesItems): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: IssuesItems, newItem: IssuesItems): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var issuesItems: List<IssuesItems>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssuesItemViewHolder {

        val binding =
            LayoutIssuesItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IssuesItemViewHolder(
            binding
        )
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }


    override fun onBindViewHolder(holder: IssuesItemViewHolder, position: Int) {
        val issuesItem = issuesItems[position]

        holder.bind(issuesItem, glide)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(issuesItem.issueNo)
            }
        }
    }

    override fun getItemCount(): Int {
        return issuesItems.size
    }
}







