package com.smartcity.petsservices.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.FragmentRegistrationBinding
import com.smartcity.petsservices.model.*
import com.smartcity.petsservices.ui.activity.MainActivity
import com.smartcity.petsservices.ui.viewModel.RegistrationViewModel
import java.util.regex.Pattern



class RegistrationFragment : Fragment() {

    lateinit var binding: FragmentRegistrationBinding
    lateinit var registrationViewModel : RegistrationViewModel
    lateinit var countryDropDown : AutoCompleteTextView

    // check form
    var isValidateEmail : Boolean = false
    var isValidatePassword : Boolean = false
    var isValidateValidationPassword : Boolean = false
    var isValidateFirstname : Boolean = false
    var isValidateLastname : Boolean = false
    var isValidatePhone : Boolean = false
    var isValidateStreetName : Boolean = false
    var isValidateStreetNumber : Boolean = false
    var isValidateLocality : Boolean = false
    var isValidatePostalCode : Boolean = false
    var isRegister : Boolean = false

    lateinit var sharedPref : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        binding.viewModel = registrationViewModel
        binding.lifecycleOwner = this

        sharedPref = requireActivity().getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
        countryDropDown = binding.countryDropdown

        initCountryDropDown()

        // Back button
        binding.registrationBackButton.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        //Registration button
        binding.registrationButton.setOnClickListener {

            if(validateForm() && validateRoles()){
                addUser()
            }
            else{
                showFieldsError()
                if(!validateForm()){
                    Toast.makeText(activity, R.string.form_error, Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(activity, R.string.roles_error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        inputsVerifier()

        registrationViewModel.getEmailMediator().observe(
            viewLifecycleOwner,
            Observer { validationResult ->
                isValidateEmail = validationResult
            })

        registrationViewModel.getValidationPasswordMediator().observe(
            viewLifecycleOwner,
            Observer { validationResult ->
                isValidateValidationPassword = validationResult
            })

        registrationViewModel.getPasswordMediator().observe(
            viewLifecycleOwner,
            Observer { validationResult ->
                isValidatePassword = validationResult
            })

        registrationViewModel.getLastnameMediator().observe(
            viewLifecycleOwner,
            Observer { validationResult ->
                isValidateLastname = validationResult
            })

        registrationViewModel.getFirstnameMediator().observe(
            viewLifecycleOwner,
            Observer { validationResult ->
                isValidateFirstname = validationResult
            })

        registrationViewModel.getPhoneMediator().observe(
            viewLifecycleOwner,
            Observer { validationResult ->
                isValidatePhone = validationResult
            })

        registrationViewModel.getStreetNameMediator().observe(
            viewLifecycleOwner,
            Observer { validationResult ->
                isValidateStreetName = validationResult
            })

        registrationViewModel.getStreetNumberMediator().observe(
            viewLifecycleOwner,
            Observer { validationResult ->
                isValidateStreetNumber = validationResult
            })

        registrationViewModel.getLocalityMediator().observe(
            viewLifecycleOwner,
            Observer { validationResult ->
                isValidateLocality = validationResult
            })

        registrationViewModel.getPostalCodeMediator().observe(
            viewLifecycleOwner,
            Observer { validationResult ->
                isValidatePostalCode = validationResult
            })

        registrationViewModel.error.observe(viewLifecycleOwner){ error: Error -> this.displayErrorScreen(
            error
        )
            if(isRegister){

                Toast.makeText(activity, getString((R.string.inscription)), Toast.LENGTH_SHORT).show()
            }
        }

        registrationViewModel.jwt.observe(viewLifecycleOwner){ token: Token -> this.savePreferedValue(
            token
        )
            goToProfileActivity()
        }

        return binding.root
    }

    private fun addCustomer(searchHost: Boolean, searchAnimalWalker: Boolean) : Customer{
        var customer = Customer(null, searchAnimalWalker, searchHost)
        return customer
    }

    private fun addSuppplier(isHost: Boolean, isAnimalWalker: Boolean) : Supplier {
        var supplier = Supplier(isHost, isAnimalWalker, null, null, null)
        return supplier
    }

    // ----------------- Country Dropdown -------------------------

    private fun initCountryDropDown(){
        val COUNTRIES = arrayOf(
            getString((R.string.country_belgique)), getString((R.string.country_france)), getString(
                (R.string.country_luxembourg)
            )
        )
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_countries,
            COUNTRIES
        )
        countryDropDown.setAdapter(adapter)
    }

    // ----------------- Network Error -------------------------
    private  fun displayErrorScreen(error: Error){
        when(error){
            Error.TECHNICAL_ERROR -> Toast.makeText(
                activity,
                R.string.technical_error,
                Toast.LENGTH_SHORT
            ).show()
            Error.NO_CONNECTION -> Toast.makeText(
                activity,
                R.string.connectivity_error,
                Toast.LENGTH_SHORT
            ).show()
            Error.REQUEST_ERROR -> Toast.makeText(
                activity,
                R.string.request_error,
                Toast.LENGTH_SHORT
            ).show()
            Error.USER_ALREADY_EXIST -> Toast.makeText(
                activity,
                R.string.user_already_exist,
                Toast.LENGTH_SHORT
            ).show()
            else -> {
              isRegister = true
            }
        }
    }

    // ----------------- SharedPreferences  -------------------------
    private fun savePreferedValue(token: Token){
        var editor : SharedPreferences.Editor = sharedPref.edit()
        editor.putString(getString(R.string.email_payload), token.email)
        editor.putInt(getString(R.string.user_id_payload), token.userId!!)
        editor.putString(getString(R.string.token), token.token)
        editor.putLong(getString(R.string.exp_date_payload), token.expDate!!.getTime()).apply()
    }

    // ----------------- Navigation -------------------------
    private fun goToProfileActivity(){
        var intent : Intent = Intent(
            requireActivity().applicationContext,
            MainActivity::class.java
        )
        startActivity(intent)
    }

    // ----------------- Fields validation -------------------------

    private fun validateForm():Boolean{
        return isValidateEmail
                && isValidatePassword
                && isValidateValidationPassword
                && isValidateFirstname
                && isValidateLastname
                && isValidatePhone
                && isValidateStreetName
                && isValidateStreetNumber
                && isValidateLocality
                && isValidatePostalCode
    }

    private fun validateRoles():Boolean{
        return binding.checkboxHost.isChecked
                || binding.checkboxAnimalWalker.isChecked
                || binding.checkboxSearchWalker.isChecked
                || binding.checkboxSearchHost.isChecked
    }

    private fun showFieldsError(){
        emailCheckError()
        passwordCheckError()
        validationPasswordCheckError()
        lastnameCheckError()
        firstnameCheckError()
        phoneCheckError()
        streetNameCheckError()
        streetNumberCheckError()
        localityCheckError()
        postalCodeCheckError()
        countryCheckError()

    }

    private fun inputsVerifier(){
        emailTextChangedListener()
        passwordTextChangedListener()
        validationPasswordTextChangedListener()
        lastnameTextChangedListener()
        firstnameTextChangedListener()
        phoneTextChangedListener()
        streetNameTextChangedListener()
        streetNumberTextChangedListener()
        localityTextChangedListener()
        postalCodeTextChangedListener()
        countrytextChangedListener()

    }

    private fun emailTextChangedListener(){
        binding.emailTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                emailCheckError()
            }
        })
    }

    private fun passwordTextChangedListener(){
        binding.passwordTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                passwordCheckError()
            }
        })
    }

    private fun validationPasswordTextChangedListener(){
        binding.validationPasswordTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                validationPasswordCheckError()
            }
        })
    }

    private fun lastnameTextChangedListener(){
        binding.lastnameTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                lastnameCheckError()
            }
        })
    }

    private fun firstnameTextChangedListener(){
        binding.firstnameTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                firstnameCheckError()
            }
        })
    }

    private fun phoneTextChangedListener(){
        binding.phoneTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                phoneCheckError()
            }
        })
    }

    private fun streetNameTextChangedListener(){
        binding.streetNameTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                streetNameCheckError()
            }
        })
    }

    private fun streetNumberTextChangedListener(){
        binding.streetNumberTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                streetNumberCheckError()
            }
        })
    }

    private fun localityTextChangedListener(){
        binding.cityTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                localityCheckError()
            }
        })
    }

    private fun postalCodeTextChangedListener(){
        binding.postalCodeTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                postalCodeCheckError()
            }
        })
    }

    private fun countrytextChangedListener(){
        countryDropDown.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                countryCheckError()
            }
        })
    }


    private fun emailCheckError(){
        if (binding.emailTextInputLayout.editText!!.text.isEmpty()) {
            binding.emailTextInputLayout.setErrorEnabled(true)
            binding.emailTextInputLayout.error = getString(R.string.email_empty_error)
        } else {
            if (!(Patterns.EMAIL_ADDRESS.matcher(binding.emailTextInputLayout.editText!!.text).matches())) {
                binding.emailTextInputLayout.setErrorEnabled(true)
                binding.emailTextInputLayout.error =  getString(R.string.email_format_error)
            } else {
                binding.emailTextInputLayout.setErrorEnabled(false)
                binding.emailTextInputLayout.error = null
            }
        }
    }

    private fun passwordCheckError(){
        if (binding.passwordTextInputLayout.editText!!.text.isEmpty()) {
            binding.passwordTextInputLayout.setErrorEnabled(true)
            binding.passwordTextInputLayout.error = getString(R.string.password_empty_error)
        } else {
            binding.passwordTextInputLayout.setErrorEnabled(true)
            binding.passwordTextInputLayout.error = null
        }
    }

    private fun validationPasswordCheckError(){
        if (binding.validationPasswordTextInputLayout.editText!!.text.isEmpty()) {
            binding.validationPasswordTextInputLayout.setErrorEnabled(true)
            binding.validationPasswordTextInputLayout.error = getString(R.string.validation_password_empty_error)
        } else {
            if (!((binding.validationPasswordTextInputLayout.editText!!.text.toString()).equals(binding.passwordTextInputLayout.editText!!.text.toString()))) {
                binding.validationPasswordTextInputLayout.setErrorEnabled(true)
                binding.validationPasswordTextInputLayout.error = getString(R.string.validation_password_format_error)
            }
            else {
                binding.validationPasswordTextInputLayout.setErrorEnabled(false)
                binding.validationPasswordTextInputLayout.error = null
            }
        }
    }

    private fun lastnameCheckError(){
        if (binding.lastnameTextInputLayout.editText!!.text.isEmpty()) {
            binding.lastnameTextInputLayout.setErrorEnabled(true)
            binding.lastnameTextInputLayout.error = getString(R.string.lastname_empty_error)
        } else {
            if (!(Pattern.compile("^[a-zéèçàïôëA-Z]{1,50}(-| )?([a-zéèçàïôëA-Z]{1,50})?$").matcher(binding.lastnameTextInputLayout.editText!!.text
                ).matches())) {
                binding.lastnameTextInputLayout.setErrorEnabled(true)
                binding.lastnameTextInputLayout.error = getString(R.string.lastname_format_error)
            } else {
                binding.lastnameTextInputLayout.setErrorEnabled(false)
                binding.lastnameTextInputLayout.error = null
            }
        }
    }

    private fun firstnameCheckError(){
        if (binding.firstnameTextInputLayout.editText!!.text.isEmpty()) {
            binding.firstnameTextInputLayout.setErrorEnabled(true)
            binding.firstnameTextInputLayout.error = getString(R.string.firstname_empty_error)
        } else {
            if (!(Pattern.compile("^[a-zéèçàïôëA-Z]{1,50}(-| )?([a-zéèçàïôëA-Z]{1,50})?$").matcher(
                            binding.firstnameTextInputLayout.editText!!.text
                ).matches())) {
                binding.firstnameTextInputLayout.setErrorEnabled(true)
                binding.firstnameTextInputLayout.error = getString(R.string.firstname_format_error)
            } else {
                binding.firstnameTextInputLayout.setErrorEnabled(false)
                binding.firstnameTextInputLayout.error = null
            }
        }
    }

    private fun phoneCheckError(){
        if (binding.phoneTextInputLayout.editText!!.text.isEmpty()) {
            binding.phoneTextInputLayout.setErrorEnabled(true)
            binding.phoneTextInputLayout.error = getString(R.string.phone_empty_error)
        } else {
            if (!(Patterns.PHONE.matcher(binding.phoneTextInputLayout.editText!!.text).matches())) {
                binding.phoneTextInputLayout.setErrorEnabled(true)
                binding.phoneTextInputLayout.error = getString(R.string.phone_format_error)
            } else {
                binding.phoneTextInputLayout.setErrorEnabled(false)
                binding.phoneTextInputLayout.error = null
            }
        }
    }

    private fun streetNameCheckError(){
        if (binding.streetNameTextInputLayout.editText!!.text.isEmpty()) {
            binding.streetNameTextInputLayout.setErrorEnabled(true)
            binding.streetNameTextInputLayout.error = getString(R.string.street_name_empty_error)
        }
        else {
            if (!(Pattern.compile("^\\s*[a-zA-Z]{1}[a-zA-Z][a-zA-Z '-]*\$").matcher(binding.streetNameTextInputLayout.editText!!.text
                ).matches())) {
                binding.streetNameTextInputLayout.setErrorEnabled(true)
                binding.streetNameTextInputLayout.error = getString(R.string.street_name_format_error)
            } else {
                binding.streetNameTextInputLayout.setErrorEnabled(false)
                binding.streetNameTextInputLayout.error = null

            }
        }
    }

    private fun streetNumberCheckError(){
        if (binding.streetNumberTextInputLayout.editText!!.text.isEmpty()) {
            binding.streetNumberTextInputLayout.setErrorEnabled(true)
            binding.streetNumberTextInputLayout.error = getString(R.string.street_number_empty_error)
        } else {
            if (!( Pattern.compile("^(\\d{1,3})\\w{0,3}\$").matcher(binding.streetNumberTextInputLayout.editText!!.text).matches())){
                binding.streetNumberTextInputLayout.setErrorEnabled(true)
                binding.streetNumberTextInputLayout.error = getString(R.string.street_number_format_error)
            }
            else {
                binding.streetNumberTextInputLayout.setErrorEnabled(false)
                binding.streetNumberTextInputLayout.error = null
            }
        }
    }

    private fun localityCheckError(){
        if (binding.cityTextInputLayout.editText!!.text.isEmpty()) {
            binding.cityTextInputLayout.setErrorEnabled(true)
            binding.cityTextInputLayout.error = getString(R.string.locality_empty_error)
        } else {
            if (!(Pattern.compile("^\\s*[a-zA-Z]{1}[a-zA-Z][a-zA-Z '-]*\$").matcher(binding.cityTextInputLayout.editText!!.text).matches())) {
                binding.cityTextInputLayout.setErrorEnabled(true)
                binding.cityTextInputLayout.error = getString(R.string.locality_format_error)
            } else {
                binding.cityTextInputLayout.setErrorEnabled(false)
                binding.cityTextInputLayout.error = null

            }
        }
    }

    private fun postalCodeCheckError(){
        if (binding.postalCodeTextInputLayout.editText!!.text.isEmpty()) {
            binding.postalCodeTextInputLayout.setErrorEnabled(true)
            binding.postalCodeTextInputLayout.error = getString(R.string.postale_code_empty_error)
        } else {
            if (!(Pattern.compile("^(\\d{4,10})\$").matcher(binding.postalCodeTextInputLayout.editText!!.text).matches())) {
                binding.postalCodeTextInputLayout.setErrorEnabled(true)
                binding.postalCodeTextInputLayout.error = getString(R.string.postale_code_format_error)
            } else {
                binding.postalCodeTextInputLayout.setErrorEnabled(true)
                binding.postalCodeTextInputLayout.error = null

            }
        }
    }

    private fun countryCheckError(){
        if(countryDropDown.text.toString() == getString(R.string.EMPTY)){
            binding.countryTextInputLayout.setErrorEnabled(true)
            binding.countryTextInputLayout.error = getString(R.string.country_error)
        }
        else{
            binding.countryTextInputLayout.setErrorEnabled(false)
            binding.countryTextInputLayout.error = null
        }
    }





    // ----------------- ADD USER & AsyncTask  -------------------------

    private fun addUser(){


            var email: String = binding.emailTextInputLayout.editText!!.text.toString()
            var password: String = binding.passwordTextInputLayout.editText!!.text.toString()
            var firstname: String = binding.firstnameTextInputLayout.editText!!.text.toString()
            var lastname: String = binding.lastnameTextInputLayout.editText!!.text.toString()
            var phone: String = binding.phoneTextInputLayout.editText!!.text.toString()
            var streetNumber: String = binding.streetNumberTextInputLayout.editText!!.text.toString()
            var streetName: String = binding.streetNameTextInputLayout.editText!!.text.toString()
            var locality: String = binding.cityTextInputLayout.editText!!.text.toString()
            var postalCode: Int = (binding.postalCodeTextInputLayout.editText!!.text.toString()).toInt()
            var isHost: Boolean = binding.checkboxHost.isChecked
            var isAnimalWalker: Boolean = binding.checkboxAnimalWalker.isChecked
            var searchHost: Boolean = binding.checkboxSearchHost.isChecked
            var searchAnimalWalker: Boolean = binding.checkboxSearchWalker.isChecked
            var country : String = countryDropDown.text.toString()

            var customer: Customer = addCustomer(searchHost, searchAnimalWalker)
            var supplier: Supplier = addSuppplier(isHost, isAnimalWalker)

            var user = User(
                email,
                password,
                firstname,
                lastname,
                phone,
                locality,
                postalCode,
                streetNumber,
                streetName,
                country,
                null,
                customer,
                supplier
            )
            AddUserTask().execute(user)
    }


    inner class AddUserTask : AsyncTask<User, Void, String>(){
        override fun doInBackground(vararg users: User): String {
            registrationViewModel.addUser(users[0])

            return users[0].firstname
        }
    }


}