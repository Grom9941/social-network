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
import com.example.socialnetwork.view.UserInfoFragment.Companion.USER_OFFLINE
import com.example.socialnetwork.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private lateinit var userAdapter: UserAdapter

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        binding.progressBar.visibility = View.VISIBLE

        createRecyclerView()

        userViewModel.getAllUsers.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    if (!it.data.isNullOrEmpty()) userAdapter.submitList(it.data)
                }
                Resource.Status.ERROR -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                Resource.Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
            }
        })


        binding.fab.setOnClickListener {
            //TODO: delete cache
            //userViewModel.delete()
        }

        //startNetworkCallback()
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        //stopNetworkCallback()
        Log.v("userViewModel", "stopNetworkCallback")
    }
/*
    private fun startNetworkCallback() {
        val cm: ConnectivityManager =
            activity?.application?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        cm.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    isNetworkConnected = true
                    if (!userViewModel.getIsCached()) {
                        userViewModel.getUsersData()
                    }
                }

                override fun onLost(network: Network) {
                    isNetworkConnected = false
                }
            })
    }

    private fun stopNetworkCallback() {
        try {
            val cm: ConnectivityManager =
                activity?.application?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            cm.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
        } catch (exception: IllegalArgumentException) {
            Log.i("NetworkCallback", "already unregistered")
        }

    }
 */

    private fun createRecyclerView() {
        val recyclerView: RecyclerView = binding.recycleView
        userAdapter = UserAdapter()
        userAdapter.onClickListener.observe(viewLifecycleOwner, {
            Log.v(USER_INFO_FRAGMENT_LOG_MESSAGE + "onClickListener", it.toString())
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
        val USER_INFO_FRAGMENT_TAG =
            "BACK_STACK_INFO_TAG_" + activity?.supportFragmentManager?.backStackEntryCount
        Log.v(USER_INFO_FRAGMENT_LOG_MESSAGE + "tagFragment", USER_INFO_FRAGMENT_TAG)

        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.nav_host_fragment_container, UserInfoFragment(), USER_INFO_FRAGMENT_TAG)
            ?.addToBackStack(USER_INFO_FRAGMENT_TAG)
            ?.commit()
    }

    companion object {
        private const val USER_INFO_FRAGMENT_LOG_MESSAGE = "SocialNetwork_UserFragment_"
    }
}