package com.example.socialnetwork.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.UserApplication
import com.example.socialnetwork.databinding.FragmentUserBinding
import com.example.socialnetwork.viewmodel.UserViewModel
import com.example.socialnetwork.viewmodel.ViewModelFactory

class UserFragment : Fragment() {

    private val USER_INFO_FRAGMENT_LOG_MESSAGE = "SocialNetwork_UserFragment_"
    private lateinit var binding: FragmentUserBinding
    private lateinit var userAdapter: UserAdapter

    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory((activity?.application as UserApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentUserBinding.inflate(inflater, container, false)
        createRecyclerView()

        userViewModel.allUsersNetwork.observe(viewLifecycleOwner, { userNetwork ->
            Log.v(USER_INFO_FRAGMENT_LOG_MESSAGE+"networkRetrofitRequest", userNetwork.toString())
            userNetwork.let { userAdapter.submitList(it.body()) }
            userNetwork.let { it.body()?.let { it1 -> userViewModel.insert(it1) } }
        })

        userViewModel.allUsersCache.observe(viewLifecycleOwner, { userCache ->
            Log.v(USER_INFO_FRAGMENT_LOG_MESSAGE+"cacheRoomRequest", userCache.toString())
            userCache.let { userAdapter.submitList(it) }
        })

        userViewModel.getUsersNetwork()
        return binding.root
    }

    private fun createRecyclerView() {
        val recyclerView: RecyclerView = binding.recycleView
        userAdapter = UserAdapter()
        userAdapter.onClickListener.observe(viewLifecycleOwner, {
            Log.v(USER_INFO_FRAGMENT_LOG_MESSAGE+"onClickListener", it.toString())
            if (it != -1) {
                userViewModel.setData(it)
                transactionToInfo()
            } else {
                Toast.makeText(context, "User is offline", Toast.LENGTH_SHORT).show()
            }
        })

        recyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun transactionToInfo() {
        val USER_INFO_FRAGMENT_TAG = "BACK_STACK_INFO_TAG_" + activity?.supportFragmentManager?.backStackEntryCount
        Log.v(USER_INFO_FRAGMENT_LOG_MESSAGE+"tagFragment", USER_INFO_FRAGMENT_TAG)

        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.nav_host_fragment_container, UserInfoFragment(), USER_INFO_FRAGMENT_TAG)
            ?.addToBackStack(USER_INFO_FRAGMENT_TAG)
            ?.commit()
    }
}