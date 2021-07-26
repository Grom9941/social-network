package com.example.socialnetwork.view

import android.content.Intent
import android.net.Uri
import android.os.Build
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
import com.example.socialnetwork.databinding.FragmentUserInfoBinding
import com.example.socialnetwork.model.Resource
import com.example.socialnetwork.model.dataclass.User
import com.example.socialnetwork.viewmodel.NetworkStatus
import com.example.socialnetwork.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class UserInfoFragment : Fragment() {

    private lateinit var binding: FragmentUserInfoBinding
    private lateinit var userInfoAdapter: UserInfoAdapter

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        binding.progressBar.visibility = View.VISIBLE
        createRecyclerView()

        context?.let { context ->
            NetworkStatus(context).observe(viewLifecycleOwner, {
                userViewModel.update()
            })
        }

        userViewModel.getAllUsers.observe(viewLifecycleOwner, {
            handleRequest(it)
        })

        return binding.root
    }

    private fun handleRequest(it: Resource<List<User>>) {
        when (it.status) {
            Resource.Status.SUCCESS -> {
                val usersListId = userViewModel.getData().value
                val userCache = it.data

                var position = activity?.supportFragmentManager?.backStackEntryCount ?: 0
                position = if (position > 0) position - 1 else position
                val userInfo: User?
                try {
                    userInfo = usersListId?.get(position)?.let { it1 -> userCache?.get(it1) }

                    var registered = ""
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val odt = OffsetDateTime.parse(userInfo?.registered?.replace(" ", ""))
                        registered = odt.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yy"))
                    } else {
                        val fromUser =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                        val myFormat = SimpleDateFormat("HH:mm dd.MM.yy", Locale.getDefault())

                        try {
                            registered = myFormat.format(
                                fromUser.parse(userInfo?.registered?.replace(" ", "")!!)!!
                            )
                        } catch (e: Exception) {
                            when (e) {
                                is ParseException, is NullPointerException ->
                                    Log.e(USER_INFO_FRAGMENT_TAG, e.printStackTrace().toString())
                                else -> Log.e(USER_INFO_FRAGMENT_TAG, "some error")
                            }
                        }
                    }

                    binding.registered.text = registered

                    when (userInfo?.eyeColor) {
                        "blue" -> binding.eyeColor.setImageResource(R.drawable.eye_blue)
                        "brown" -> binding.eyeColor.setImageResource(R.drawable.eye_brown)
                        "green" -> binding.eyeColor.setImageResource(R.drawable.eye_green)
                    }

                    val listOfFriends = mutableListOf(userInfo)
                    userInfo?.friends?.forEach { friend ->
                        listOfFriends.add(userCache?.get(friend.id))
                    }

                    Log.i(USER_INFO_FRAGMENT_TAG, listOfFriends.toString())

                    binding.progressBar.visibility = View.GONE
                    userInfoAdapter.submitList(listOfFriends)
                } catch (e: Exception) {
                    when (e) {
                        is IndexOutOfBoundsException, is NullPointerException -> Log.e(
                            USER_INFO_FRAGMENT_TAG,
                            e.message.toString()
                        )
                    }

                }
            }
            Resource.Status.ERROR -> Log.e(USER_INFO_FRAGMENT_TAG, "error status")
            Resource.Status.LOADING -> Log.i(USER_INFO_FRAGMENT_TAG, "loading status")
        }
    }

    private fun createRecyclerView() {
        val recyclerView: RecyclerView = binding.recycleViewListFriends

        userInfoAdapter = UserInfoAdapter()
        userInfoAdapter.onClickListener.observe(viewLifecycleOwner, {
            Log.i(USER_INFO_FRAGMENT_TAG, "onClickListener$it")

            val dataInt = it.keys.elementAt(0)
            val dataStr = it.values.elementAt(0)

            when (dataInt) {
                USER_OFFLINE -> Toast.makeText(context, "User is offline", Toast.LENGTH_SHORT)
                    .show()
                DIAL_PHONE -> dialPhone(dataStr)
                COMPOSE_EMAIL -> composeEmail(arrayOf(dataStr))
                SHOW_LOCATION -> showLocation(dataStr)
                else -> {
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
        val userInfoFragmentMessage =
            "BACK_STACK_" + activity?.supportFragmentManager?.backStackEntryCount
        Log.i(userInfoFragmentMessage, userInfoFragmentMessage)

        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.nav_host_fragment_container, UserInfoFragment(), userInfoFragmentMessage)
            ?.addToBackStack(userInfoFragmentMessage)
            ?.commit()
    }

    private fun dialPhone(phoneNumber: String) {
        Log.i(USER_INFO_FRAGMENT_TAG, "dial phone $phoneNumber")
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        resolve(intent)
    }

    private fun composeEmail(addresses: Array<String>) {
        Log.i(USER_INFO_FRAGMENT_TAG, "compose email ${addresses.joinToString()}")
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
        }
        resolve(intent)
    }

    private fun showLocation(latitudeLongitude: String) {
        Log.i(USER_INFO_FRAGMENT_TAG, "show location $latitudeLongitude")
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
        private const val USER_INFO_FRAGMENT_TAG = "com.example.socialnetwork.view_UserInfoFragment"
        const val USER_OFFLINE = -1
        const val DIAL_PHONE = -2
        const val COMPOSE_EMAIL = -3
        const val SHOW_LOCATION = -4
    }
}