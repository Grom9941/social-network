package com.example.socialnetwork.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.model.dataclass.User

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class UserInfoAdapter(val onClickListener: MutableLiveData<Int> = MutableLiveData()) :
    ListAdapter<User, RecyclerView.ViewHolder>(UserComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.create(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.create(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val itemPos = getItem(position)
                holder.itemView.setOnClickListener {
                    onClickListener.value = if (itemPos.isActive) getItem(position).id else -1
                }
                holder.bind(itemPos.name, itemPos.email)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is User ->  ITEM_VIEW_TYPE_ITEM
            else -> ITEM_VIEW_TYPE_HEADER
        }
    }

    class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view){
        companion object {
            fun create(parent: ViewGroup): HeaderViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.header, parent, false)
                return HeaderViewHolder(view)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.user_item_name)
        private val email: TextView = view.findViewById(R.id.user_item_email)

        fun bind(nameText: String?, emailText: String?) {
            name.text = nameText
            email.text = emailText
        }

        companion object {
            fun create(parent: ViewGroup): ViewHolder{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.user_item, parent, false)
                return ViewHolder(view)
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