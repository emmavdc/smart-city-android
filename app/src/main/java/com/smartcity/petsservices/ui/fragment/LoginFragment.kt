package com.smartcity.petsservices.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.LoginFragmentBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    private lateinit var binding : LoginFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = LoginFragmentBinding.inflate(inflater, container, false)
        var root : View  = binding.root

        /*binding.loginConnectionButton.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/
        binding.loginRegistration.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        return root
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.login_connection_button).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }*/
}