package com.example.socialnetwork.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.databinding.UserItemBinding
import com.example.socialnetwork.model.dataclass.User

class UserAdapter(val onClickListener: MutableLiveData<Int> = MutableLiveData()) :
    ListAdapter<User, UserAdapter.ViewHolder>(UserComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPos = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.value = if (itemPos.isActive) getItem(position).id else -1
        }
        holder.bind(itemPos)
    }

    class ViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemPos: User) {
            binding.userItemName.text = itemPos.name
            binding.userItemEmail.text = itemPos.email
            when (itemPos.isActive) {
                true -> binding.isActive.setImageResource(R.drawable.circle_active)
                false -> binding.isActive.setImageResource(R.drawable.circle_non_active)
            }
        }
    }

    class UserComparator : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}