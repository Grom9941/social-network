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
import com.example.socialnetwork.UserApplication
import com.example.socialnetwork.databinding.FragmentUserBinding
import com.example.socialnetwork.viewmodel.TestViewModel
import com.example.socialnetwork.viewmodel.ViewModelFactory

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding

    private val testViewModel: TestViewModel by activityViewModels {
        ViewModelFactory((activity?.application as UserApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentUserBinding.inflate(inflater, container, false)

        val recyclerView: RecyclerView = binding.recycleView
        val userAdapter = UserAdapter()
        recyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        testViewModel.allUsersNetwork.observe(viewLifecycleOwner, { userNetwork ->
            Log.v("SocialNetwork_UserFragment_networkRetrofitRequest", userNetwork.toString())
            //userNetwork.let { userAdapter.submitList(it.body()) }
            userNetwork.let { it.body()?.let { it1 -> testViewModel.insert(it1[0]) } }
        })

        testViewModel.allUsersCache.observe(viewLifecycleOwner, { userCache ->
            Log.v("SocialNetwork_UserFragment_cacheRoomRequest", userCache.toString())
            userCache.let { userAdapter.submitList(it) }
        })

        testViewModel.getUsersNetwork()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val TAG = "BACK_STACK_INFO_TAG_" + activity?.supportFragmentManager?.backStackEntryCount
        Log.v("SocialNetwork_UserFragment_tagFragment", TAG)

        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.nav_host_fragment_container, UserInfoFragment(), TAG)
            ?.addToBackStack(TAG)
            ?.commit()
    }
}