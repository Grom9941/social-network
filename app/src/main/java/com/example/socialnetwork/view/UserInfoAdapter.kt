package com.example.socialnetwork.view

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.databinding.HeaderBinding
import com.example.socialnetwork.databinding.UserItemBinding
import com.example.socialnetwork.model.dataclass.User
import com.example.socialnetwork.view.UserInfoFragment.Companion.COMPOSE_EMAIL
import com.example.socialnetwork.view.UserInfoFragment.Companion.DIAL_PHONE
import com.example.socialnetwork.view.UserInfoFragment.Companion.SHOW_LOCATION
import com.example.socialnetwork.view.UserInfoFragment.Companion.USER_OFFLINE

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1


class UserInfoAdapter(val onClickListener: MutableLiveData<Map<Int, String>> = MutableLiveData()) :
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
                    Log.v("clickListAdapter", itemPos.isActive.toString())
                    onClickListener.value = if (itemPos.isActive) mapOf(getItem(position).id to "")
                    else mapOf(USER_OFFLINE to "")
                }
                holder.bind(itemPos)
            }
            is HeaderViewHolder -> {
                holder.bind(getItem(position), onClickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_VIEW_TYPE_HEADER
            else -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class HeaderViewHolder(private val binding: HeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(userInfo: User, onClickListener: MutableLiveData<Map<Int, String>>) {
            binding.name.text = userInfo.name
            binding.age.text = userInfo.age.toString()
            binding.company.text = userInfo.company

            val phoneNumber = userInfo.phone
            binding.phone.text = phoneNumber

            val email = userInfo.email
            binding.email.text = email

            val latitudeLongitude = "${userInfo.latitude},${userInfo.longitude}"
            binding.address.text = "${userInfo.address} ($latitudeLongitude)"

            binding.about.text = userInfo.about
            when (userInfo.favoriteFruit) {
                "apple" -> binding.favoriteFruit.setImageResource(R.drawable.ic_apple)
                "banana" -> binding.favoriteFruit.setImageResource(R.drawable.ic_banana)
                else -> binding.favoriteFruit.setImageResource(R.drawable.ic_strawberry)
            }

            binding.phone.setOnClickListener {
                onClickListener.value = mapOf(DIAL_PHONE to phoneNumber)
            }
            binding.email.setOnClickListener {
                onClickListener.value = mapOf(COMPOSE_EMAIL to email)
            }
            binding.address.setOnClickListener {
                onClickListener.value = mapOf(SHOW_LOCATION to latitudeLongitude)
            }
        }

        companion object {
            fun create(parent: ViewGroup): HeaderViewHolder {
                val view = HeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HeaderViewHolder(view)
            }
        }
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

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view =
                    UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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