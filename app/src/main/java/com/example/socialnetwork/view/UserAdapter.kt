package com.example.socialnetwork.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.model.dataclass.User

class UserAdapter(val onClickListener: MutableLiveData<Int> = MutableLiveData()) :
    ListAdapter<User, UserAdapter.ViewHolder>(UserComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        view.setOnClickListener { }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPos = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.value = if (itemPos.isActive) getItem(position).id else -1
        }
        holder.bind(itemPos)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.user_item_name)
        private val email: TextView = view.findViewById(R.id.user_item_email)
        private val isActive: ImageView = view.findViewById(R.id.is_active)

        fun bind(itemPos: User) {
            name.text = itemPos.name
            email.text = itemPos.email
            when (itemPos.isActive) {
                true -> isActive.setImageResource(R.drawable.circle_active)
                false -> isActive.setImageResource(R.drawable.circle_non_active)
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