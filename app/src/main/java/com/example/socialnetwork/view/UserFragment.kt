package com.example.socialnetwork.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.databinding.FragmentUserBinding
import com.example.socialnetwork.model.Repository
import com.example.socialnetwork.model.database.UserRoomDatabase
import com.example.socialnetwork.model.dataclass.Friend
import com.example.socialnetwork.model.dataclass.User
import com.example.socialnetwork.model.network.RetrofitInstance
import com.example.socialnetwork.viewmodel.TestViewModel
import com.example.socialnetwork.viewmodel.ViewModelFactory

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentUserBinding.inflate(inflater, container, false)


        val recyclerView: RecyclerView = binding.recycleView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = UserAdapter(listOf(User(1,true,1,"f","f","f","f","f","f","f","f",1.9f, 1.9f,
                listOf(Friend(1)), "f")))
        }

        //testViewModel.getUsersNetwork()
        Log.v("UserFragment", "network request")

        //testViewModel.allUsersNetwork.observe(viewLifecycleOwner, {
        //        userNetwork -> recyclerView.adapter = userNetwork.body()?.let { UserAdapter(it) }
        //})

        return binding.root
    }
    /*
        view.findViewById<Button>(R.id.button).setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_userFragment_to_userInfoFragment)
        }
         */
}