package com.example.socialnetwork.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.socialnetwork.R
import com.example.socialnetwork.databinding.FragmentUserBinding
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
}