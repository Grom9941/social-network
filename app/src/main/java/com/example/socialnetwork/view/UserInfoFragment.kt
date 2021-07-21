package com.example.socialnetwork.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.UserApplication
import com.example.socialnetwork.databinding.FragmentUserInfoBinding
import com.example.socialnetwork.viewmodel.UserViewModel
import com.example.socialnetwork.viewmodel.ViewModelFactory
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class UserInfoFragment : Fragment() {

    private val USER_INFO_FRAGMENT_LOG_MESSAGE = "SocialNetwork_UserFragment_"
    private lateinit var binding: FragmentUserInfoBinding
    private lateinit var userInfoAdapter: UserAdapter

    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory((activity?.application as UserApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        createRecyclerView()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getData().observe(viewLifecycleOwner, {
            userViewModel.getUserInfo(it).observe(viewLifecycleOwner, { userInfo ->
                userInfo?.let {
                    /*
                  binding.name.text = userInfo.name
                  binding.age.text = userInfo.age.toString()
                  binding.company.text = userInfo.company
                  binding.phone.text = userInfo.phone
                  binding.email.text = userInfo.email
                  binding.address.text = "${userInfo.address} (${userInfo.latitude},${userInfo.longitude})"
                  binding.about.text = userInfo.about

                  when (userInfo.favoriteFruit) {
                      "apple" -> binding.favoriteFruit.setImageResource(R.drawable.ic_apple)
                      "banana" -> binding.favoriteFruit.setImageResource(R.drawable.ic_banana)
                      else -> binding.favoriteFruit.setImageResource(R.drawable.ic_strawberry)
                  }
                  */
                    val odt = OffsetDateTime.parse(userInfo.registered.replace(" ", ""))
                    val registered = odt.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy"))
                    binding.registered.text = registered

                    userInfo.friends.forEach { friendId ->
                        friendId.id
                    }
                }

            })
        })

        userViewModel.allUsersCache.observe(viewLifecycleOwner, { userCache ->
            Log.v(USER_INFO_FRAGMENT_LOG_MESSAGE+"cacheRoomRequest", userCache.toString())
            //TODO: change function invocation
            userCache.let { userInfoAdapter.submitList(it) }
        })

        /*
        val phoneNumber = binding.phone.text.toString()
        val addresses = arrayOf(binding.email.text.toString())
        val location = binding.address.text.split('(')
        val latitudeLongitude = location[location.size - 1].dropLast(1)

        binding.phone.setOnClickListener { dialPhone(phoneNumber) }
        binding.email.setOnClickListener { composeEmail(addresses) }
        binding.address.setOnClickListener { showLocation(latitudeLongitude) }
         */
    }

    private fun createRecyclerView() {
        val recyclerView: RecyclerView = binding.recycleViewListFriends
        userInfoAdapter = UserAdapter()
        userInfoAdapter.onClickListener.observe(viewLifecycleOwner, {
            Log.v(USER_INFO_FRAGMENT_LOG_MESSAGE+"onClickListener", it.toString())
            userViewModel.setData(it)
            transactionToInfo()
        })

        recyclerView.apply {
            adapter = userInfoAdapter
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

    private fun dialPhone(phoneNumber: String) {
        Log.v("SocialNetwork_UserInfoFragment_dialPhone", phoneNumber)
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        resolve(intent)
    }

    private fun composeEmail(addresses: Array<String>) {
        Log.v("SocialNetwork_UserInfoFragment_composeEmail", addresses.joinToString())
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
        }
        resolve(intent)
    }

    private fun showLocation(latitudeLongitude: String) {
        Log.v("SocialNetwork_UserInfoFragment_showLocation", latitudeLongitude)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("geo:$latitudeLongitude")
        }
        resolve(intent)
    }

    private fun resolve(intent: Intent) {
        if (activity?.let { intent.resolveActivity(it.packageManager) } != null) {
            startActivity(intent)
        }
    }


}