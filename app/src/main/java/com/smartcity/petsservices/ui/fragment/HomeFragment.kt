package com.smartcity.petsservices.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.FragmentHomeBinding

class HomeFragment : Fragment(){

    lateinit var binding : FragmentHomeBinding
    lateinit var sharedPref : SharedPreferences
    lateinit var test: EditText


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // get preferences
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)



        //displayPreferedValue()

        return binding.root;
    }

    private fun displayPreferedValue(){
        var value: Int = sharedPref.getInt(getString(R.string.user_id_payload), 42)
        test.setText(Integer.toString(value))
    }
}