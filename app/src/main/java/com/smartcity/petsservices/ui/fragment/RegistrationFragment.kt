package com.smartcity.petsservices.ui.fragment

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.RegistrationFragmentBinding
import com.smartcity.petsservices.model.User
import com.smartcity.petsservices.ui.viewModel.RegistrationViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegistrationFragment : Fragment() {

    private lateinit var binding: RegistrationFragmentBinding
    private lateinit var registrationViewModel : RegistrationViewModel
    // Edit Text
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var passwordValidationEditText: EditText
    lateinit var firstnameEditText: EditText
    lateinit var lastnameEditText: EditText
    lateinit var streetNumberEditText: EditText
    lateinit var streetNameEditText: EditText
    lateinit var cityEditText: EditText
    lateinit var postalCodeEditText: EditText
    // Check Box
    lateinit var isHostCheckBox: CheckBox
    lateinit var isAnimalWalkerCheckBox: CheckBox
    lateinit var searchAnimalWalkerCheckBox: CheckBox
    lateinit var searchHostCheckBox: CheckBox




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = registrationViewModel
        binding.lifecycleOwner = this

        //EditText
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText
        passwordValidationEditText = binding.validationPasswordEditText
        firstnameEditText = binding.firstnameEditText
        lastnameEditText = binding.lastnameEditText
        streetNumberEditText = binding.streetNumberEditText
        streetNameEditText = binding.streetNameEditText
        cityEditText = binding.cityEditText
        postalCodeEditText = binding.postalCodeEditText

        // CHeck Box
        isHostCheckBox = binding.checkboxHost
        isAnimalWalkerCheckBox = binding.checkboxAnimalWalker
        searchHostCheckBox = binding.checkboxSearchHost
        searchAnimalWalkerCheckBox = binding.checkboxSearchWalker


        // Back button
        binding.registrationBackButton.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        //test
        //binding.emailEditText.setText("emma@yahoo.fr");
        //System.out.println(emailEditText.text.toString())


       return binding.root;
    }

    // ----------------- ADD USER & AsyncTask  -------------------------

    private fun addUser(view : View){
        var email : String = emailEditText.text.toString()
        var password : String = passwordEditText.text.toString()
        var firstname : String = firstnameEditText.text.toString()
        var lastname : String = lastnameEditText.text.toString()
        var streetNumber : String = streetNumberEditText.text.toString()
        var streetName : String = streetNameEditText.text.toString()
        var city : String = cityEditText.text.toString()
        var postalCode : Int = (postalCodeEditText.text.toString()).toInt()
        var isHost : Boolean = isHostCheckBox.isChecked
        var isAnimalWalker : Boolean = isHostCheckBox.isChecked
        var searchHost : Boolean = isHostCheckBox.isChecked
        var searchAnimalWalker : Boolean = isHostCheckBox.isChecked

    }

    private class AddUserTask : AsyncTask<User, Void, String>(){

        override fun doInBackground(vararg params: User?): String {
            TODO("Not yet implemented")
        }

        override fun onPostExecute(result: String) {

        }

    }



}