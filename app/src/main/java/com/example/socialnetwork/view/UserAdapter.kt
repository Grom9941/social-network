package com.example.socialnetwork.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.model.dataclass.User
import retrofit2.Response

class UserAdapter(private val dataSet: List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.user_item_name)
        private val email: TextView = view.findViewById(R.id.user_item_email)

        fun bind(nameText: String?, emailText: String?) {
            name.text = nameText
            email.text = emailText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPos = dataSet[position]
        holder.bind(itemPos.name, itemPos.email)
    }

    override fun getItemCount() = dataSet.size
}