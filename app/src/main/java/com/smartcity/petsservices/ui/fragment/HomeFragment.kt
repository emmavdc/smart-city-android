package com.smartcity.petsservices.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.FragmentHomeBinding
import com.smartcity.petsservices.model.Token
import com.smartcity.petsservices.ui.viewModel.ProfileViewModel
import java.util.*

class HomeFragment : Fragment(){

    lateinit var binding : FragmentHomeBinding
    lateinit var sharedPref : SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        sharedPref = requireActivity().getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);



        return binding.root;
    }


}