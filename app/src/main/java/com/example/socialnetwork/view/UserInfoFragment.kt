package com.example.socialnetwork.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.socialnetwork.databinding.FragmentUserInfoBinding

class UserInfoFragment : Fragment() {

    private lateinit var binding: FragmentUserInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phoneNumber = binding.phone.text.toString()
        val addresses = arrayOf(binding.email.text.toString())
        val location = binding.address.text.split('(')
        val latitudeLongitude = location[location.size-1].dropLast(1)

        binding.phone.setOnClickListener { dialPhone(phoneNumber) }
        binding.icPhone.setOnClickListener { dialPhone(phoneNumber) }
        binding.email.setOnClickListener { composeEmail(addresses) }
        binding.icEmail.setOnClickListener { composeEmail(addresses) }
        binding.address.setOnClickListener { showLocation(latitudeLongitude) }
        binding.icLocation.setOnClickListener { showLocation(latitudeLongitude) }
    }

    private fun dialPhone(phoneNumber: String){
        Log.v("SocialNetwork_UserInfoFragment_dialPhone", phoneNumber)
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        resolve(intent)
    }

    private fun composeEmail(addresses: Array<String>){
        Log.v("SocialNetwork_UserInfoFragment_composeEmail", addresses.joinToString())
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, addresses)
        }
        resolve(intent)
    }

    private fun showLocation(latitudeLongitude: String){
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