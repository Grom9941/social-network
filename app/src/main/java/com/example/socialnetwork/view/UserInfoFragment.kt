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
import android.widget.Toast
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

    private lateinit var binding: FragmentUserInfoBinding
    private lateinit var userInfoAdapter: UserInfoAdapter

    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory((activity?.application as UserApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        binding.progressBar.visibility = View.VISIBLE
        createRecyclerView()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.usersData.observe(viewLifecycleOwner, {
            it?.let { userCache ->
                userViewModel.getData().observe(viewLifecycleOwner, { usersListId ->

                    var position = activity?.supportFragmentManager?.backStackEntryCount ?: 0
                    position = if (position > 0) position - 1 else position
                    val userInfo = userCache[usersListId[position]]

                    val odt = OffsetDateTime.parse(userInfo.registered.replace(" ", ""))
                    val registered = odt.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy"))
                    binding.registered.text = registered

                    when (userInfo.eyeColor) {
                        "blue" -> binding.eyeColor.setImageResource(R.drawable.eye_blue)
                        "brown" -> binding.eyeColor.setImageResource(R.drawable.eye_brown)
                        "green" -> binding.eyeColor.setImageResource(R.drawable.eye_green)
                    }

                    val listOfFriends = mutableListOf(userInfo)
                    userInfo.friends.forEach { friend ->
                        Log.v("userFriends", friend.id.toString())
                        listOfFriends.add(userCache[friend.id])
                    }

                    Log.v(
                        USER_INFO_FRAGMENT_LOG_MESSAGE + "cacheRoomRequest",
                        listOfFriends.toString()
                    )

                    binding.progressBar.visibility = View.GONE
                    userInfoAdapter.submitList(listOfFriends)
                })
            }
        })
    }

    private fun createRecyclerView() {
        val recyclerView: RecyclerView = binding.recycleViewListFriends

        userInfoAdapter = UserInfoAdapter()
        userInfoAdapter.onClickListener.observe(viewLifecycleOwner, {
            Log.v(USER_INFO_FRAGMENT_LOG_MESSAGE + "onClickListener", it.toString())

            val dataInt = it.keys.elementAt(0)
            val dataStr = it.values.elementAt(0)
            Log.v("clickList", dataInt.toString())
            Log.v("clickList", dataStr)

            when (dataInt) {
                USER_OFFLINE -> Toast.makeText(context, "User is offline", Toast.LENGTH_SHORT)
                    .show()
                DIAL_PHONE -> dialPhone(dataStr)
                COMPOSE_EMAIL -> composeEmail(arrayOf(dataStr))
                SHOW_LOCATION -> showLocation(dataStr)
                else -> {
                    Log.v("clickList", "pressed")
                    val position = activity?.supportFragmentManager?.backStackEntryCount ?: 0
                    userViewModel.setData(dataInt, position)
                    transactionToInfo()
                }
            }
        })

        recyclerView.apply {
            adapter = userInfoAdapter
            layoutManager = LinearLayoutManager(activity)
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

    companion object {
        private const val USER_INFO_FRAGMENT_LOG_MESSAGE = "SocialNetwork_UserFragment_"
        const val USER_OFFLINE = -1
        const val DIAL_PHONE = -2
        const val COMPOSE_EMAIL = -3
        const val SHOW_LOCATION = -4
    }
}