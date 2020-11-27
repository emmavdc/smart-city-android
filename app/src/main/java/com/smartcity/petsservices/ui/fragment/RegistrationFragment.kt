package com.smartcity.petsservices.ui.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.RegistrationFragmentBinding
import com.smartcity.petsservices.model.User
import com.smartcity.petsservices.ui.viewModel.RegistrationViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegistrationFragment : Fragment() {

    lateinit var binding: RegistrationFragmentBinding
    lateinit var registrationViewModel : RegistrationViewModel
    // Edit Text
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var passwordValidationEditText: EditText
    lateinit var firstnameEditText: EditText
    lateinit var lastnameEditText: EditText
    lateinit var phoneEditText: EditText
    lateinit var streetNumberEditText: EditText
    lateinit var streetNameEditText: EditText
    lateinit var cityEditText: EditText
    lateinit var postalCodeEditText: EditText
    // Check Box
    lateinit var isHostCheckBox: CheckBox
    lateinit var isAnimalWalkerCheckBox: CheckBox
    lateinit var searchAnimalWalkerCheckBox: CheckBox
    lateinit var searchHostCheckBox: CheckBox




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

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
        phoneEditText = binding.phoneEditText
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

        //Registration button
        binding.registrationButton.setOnClickListener {
            //registrationViewModel.addUser(addUser());
            addUser()
        }
        //test
        binding.emailEditText.setText("coucouc@yahoo.fr");
        binding.passwordEditText.setText("123");
        binding.firstnameEditText.setText("Emma");
        binding.lastnameEditText.setText("vandecasteele");
        binding.validationPasswordEditText.setText("123");
        binding.phoneEditText.setText("0497898965");
        binding.streetNameEditText.setText("chemin");
        binding.streetNumberEditText.setText("12");
        binding.cityEditText.setText("Thuin");
        binding.postalCodeEditText.setText("6530");
        //System.out.println(emailEditText.text.toString())


       return binding.root;
    }


    // ----------------- ADD USER & AsyncTask  -------------------------

    private fun addUser(){
        var email : String = emailEditText.text.toString()
        var password : String = passwordEditText.text.toString()
        var firstname : String = firstnameEditText.text.toString()
        var lastname : String = lastnameEditText.text.toString()
        var phone : String = phoneEditText.text.toString()
        var streetNumber : String = streetNumberEditText.text.toString()
        var streetName : String = streetNameEditText.text.toString()
        var locality : String = cityEditText.text.toString()
        var postalCode : Int = (postalCodeEditText.text.toString()).toInt()
        var isHost : Boolean = isHostCheckBox.isChecked
        var isAnimalWalker : Boolean = isHostCheckBox.isChecked
        var searchHost : Boolean = isHostCheckBox.isChecked
        var searchAnimalWalker : Boolean = isHostCheckBox.isChecked

         var user = User(email,
                password,
                firstname,
                lastname,
                phone,
                locality,
                postalCode,
                streetNumber,
                streetName,
                country = "Belgique",
                null,
                null,
                null)

        //return user

        AddUserTask().execute(user)
    }

    inner class AddUserTask : AsyncTask<User, Void, String>(){
        override fun doInBackground(vararg users: User): String {
            registrationViewModel.addUser(users[0])

            return users[0].firstname
        }

        override fun onPostExecute(result: String) {
            Toast.makeText(activity!!.applicationContext, "User $result added", Toast.LENGTH_SHORT).show()
        }

    }



}