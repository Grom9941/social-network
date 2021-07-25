package com.example.socialnetwork.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.databinding.FragmentUserBinding
import com.example.socialnetwork.model.Resource
import com.example.socialnetwork.model.dataclass.User
import com.example.socialnetwork.view.UserInfoFragment.Companion.USER_OFFLINE
import com.example.socialnetwork.viewmodel.NetworkStatus
import com.example.socialnetwork.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private lateinit var userAdapter: UserAdapter
    private var isDataLoaded = false

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        binding.progressBar.visibility = View.VISIBLE
        createRecyclerView()

        context?.let { context ->
            NetworkStatus(context).observe(viewLifecycleOwner, {
                userViewModel.getUsers.observe(viewLifecycleOwner, {
                    handleRequest(it)
                })
            })
        }

        userViewModel.getAllUsers.observe(viewLifecycleOwner, {
            if (!isDataLoaded) {
                handleRequest(it)
            }
        })

        binding.fab.setOnClickListener {
            isDataLoaded = false
            userViewModel.deleteAll()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isDataLoaded = false
    }

    private fun handleRequest(it: Resource<List<User>>) {
        when (it.status) {
            Resource.Status.SUCCESS -> {
                if (!it.data.isNullOrEmpty()) {
                    binding.progressBar.visibility = View.GONE
                    isDataLoaded = true
                    userAdapter.submitList(it.data)
                }
            }
            Resource.Status.ERROR -> Log.e(USER_INFO_FRAGMENT_TAG, "error status")
            Resource.Status.LOADING -> Log.i(USER_INFO_FRAGMENT_TAG, "loading status")
        }
    }

    private fun createRecyclerView() {
        val recyclerView: RecyclerView = binding.recycleView
        userAdapter = UserAdapter()
        userAdapter.onClickListener.observe(viewLifecycleOwner, {
            Log.i(USER_INFO_FRAGMENT_TAG, "click listener $it")
            when (it) {
                USER_OFFLINE -> Toast.makeText(context, "User is offline", Toast.LENGTH_SHORT)
                    .show()
                else -> {
                    userViewModel.setData(it)
                    transactionToInfo()
                }
            }
        })

        recyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun transactionToInfo() {
        val userInfoFragmentMessage =
            "BACK_STACK_" + activity?.supportFragmentManager?.backStackEntryCount
        Log.i(USER_INFO_FRAGMENT_TAG, userInfoFragmentMessage)

        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.nav_host_fragment_container, UserInfoFragment(), userInfoFragmentMessage)
            ?.addToBackStack(userInfoFragmentMessage)
            ?.commit()
    }

    companion object {
        private const val USER_INFO_FRAGMENT_TAG = "com.example.socialnetwork.view_UserFragment"
    }
}