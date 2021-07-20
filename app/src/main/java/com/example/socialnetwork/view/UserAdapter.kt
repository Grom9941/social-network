package com.example.socialnetwork.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.model.dataclass.User

class UserAdapter : ListAdapter<User, UserAdapter.ViewHolder>(UserComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPos = getItem(position)
        holder.bind(itemPos.name, itemPos.email)
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.user_item_name)
        private val email: TextView = view.findViewById(R.id.user_item_email)

        fun bind(nameText: String?, emailText: String?) {
            view.setOnClickListener {
                //TODO: to next fragment
            }
            name.text = nameText
            email.text = emailText
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