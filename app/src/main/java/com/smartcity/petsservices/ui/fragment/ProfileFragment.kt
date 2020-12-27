package com.smartcity.petsservices.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smartcity.petsservices.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment() {

    lateinit var binding : ProfileFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ProfileFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this



        return binding.root;
    }
}