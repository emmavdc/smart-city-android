package com.smartcity.petsservices.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.RegistrationFragmentBinding
import com.smartcity.petsservices.ui.viewModel.RegistrationViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegistrationFragment : Fragment() {

    private lateinit var binding: RegistrationFragmentBinding
    private lateinit var registrationViewModel : RegistrationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = registrationViewModel
        binding.lifecycleOwner = this

        binding.registrationBackButton.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_SecondFragment_to_FirstFragment)
        }


       return binding.root;
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.registration_back_button).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }*/
}