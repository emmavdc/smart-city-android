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
import com.google.android.material.textfield.TextInputLayout
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.RegistrationFragmentBinding
import com.smartcity.petsservices.model.*
import com.smartcity.petsservices.ui.activity.EditProfileActivity
import com.smartcity.petsservices.ui.viewModel.RegistrationViewModel
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegistrationFragment : Fragment() {

    lateinit var binding: RegistrationFragmentBinding
    lateinit var registrationViewModel : RegistrationViewModel
    // TextInputLayout
    lateinit var emailTextInputLayout: TextInputLayout
    lateinit var passwordTextInputLayout: TextInputLayout
    lateinit var passwordValidationTextInputLayout: TextInputLayout
    lateinit var firstnameTextInputLayout: TextInputLayout
    lateinit var lastnameTextInputLayout: TextInputLayout
    lateinit var phoneTextInputLayout: TextInputLayout
    lateinit var streetNumberTextInputLayout: TextInputLayout
    lateinit var streetNameTextInputLayout: TextInputLayout
    lateinit var cityTextInputLayout: TextInputLayout
    lateinit var postalCodeTextInputLayout: TextInputLayout
    lateinit var countryTextInputLayout: TextInputLayout
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
    lateinit var countryDropDown :AutoCompleteTextView
    // Check Box
    lateinit var isHostCheckBox: CheckBox
    lateinit var isAnimalWalkerCheckBox: CheckBox
    lateinit var searchAnimalWalkerCheckBox: CheckBox
    lateinit var searchHostCheckBox: CheckBox

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

    //SharedPreferences
    lateinit var sharedPref : SharedPreferences



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = registrationViewModel
        binding.lifecycleOwner = this

        // get preferences
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)

        // TextInputLayout
        emailTextInputLayout = binding.emailTextInputLayout
        passwordTextInputLayout = binding.passwordTextInputLayout
        passwordValidationTextInputLayout = binding.validationPasswordTextInputLayout
        firstnameTextInputLayout = binding.firstnameTextInputLayout
        lastnameTextInputLayout = binding.lastnameTextInputLayout
        phoneTextInputLayout = binding.phoneTextInputLayout
        streetNumberTextInputLayout = binding.streetNumberTextInputLayout
        streetNameTextInputLayout = binding.streetNameTextInputLayout
        cityTextInputLayout = binding.cityTextInputLayout
        postalCodeTextInputLayout = binding.postalCodeTextInputLayout
        countryTextInputLayout = binding.countryTextInputLayout

        //EditText
        emailEditText = binding.emailTextInputLayout.editText!!
        passwordEditText = binding.passwordTextInputLayout.editText!!
        passwordValidationEditText = binding.validationPasswordTextInputLayout.editText!!
        firstnameEditText = binding.firstnameTextInputLayout.editText!!
        lastnameEditText = binding.lastnameTextInputLayout.editText!!
        phoneEditText = binding.phoneTextInputLayout.editText!!
        streetNumberEditText = binding.streetNumberTextInputLayout.editText!!
        streetNameEditText = binding.streetNameTextInputLayout.editText!!
        cityEditText = binding.cityTextInputLayout.editText!!
        postalCodeEditText = binding.postalCodeTextInputLayout.editText!!
        countryDropDown = binding.countryDropdown

        // CHeck Box
        isHostCheckBox = binding.checkboxHost
        isAnimalWalkerCheckBox = binding.checkboxAnimalWalker
        searchHostCheckBox = binding.checkboxSearchHost
        searchAnimalWalkerCheckBox = binding.checkboxSearchWalker

        // init country dropdown
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

        registrationViewModel.getError().observe(viewLifecycleOwner){ error: NetworkError -> this.displayErrorScreen(
            error
        )
            if(isRegister){

                Toast.makeText(activity, "Vous êtes inscrit !", Toast.LENGTH_SHORT).show()
            }
        }

        registrationViewModel.getJwt().observe(viewLifecycleOwner){ token: Token -> this.savePreferedValue(
            token
        )
            //navigate here ?
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

    private fun fillFields(){
        //emailEditText.setText("samy.demarthe@domain.be")
        passwordEditText.setText("235")
        firstnameEditText.setText("Samy");
        lastnameEditText.setText("Demarthe");
        passwordValidationEditText.setText("235");
        phoneEditText.setText("0497898965");
        streetNameEditText.setText("Rue de l'Ecluse");
        streetNumberEditText.setText("12");
        cityEditText.setText("Lobbes");
        postalCodeEditText.setText("6540");
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
            R.layout.country_item,
            COUNTRIES
        )
        countryDropDown.setAdapter(adapter)
    }

    // ----------------- Network Error -------------------------
    private  fun displayErrorScreen(error: NetworkError){
        when(error){
            NetworkError.TECHNICAL_ERROR -> Toast.makeText(
                activity,
                R.string.technical_error,
                Toast.LENGTH_SHORT
            ).show()
            NetworkError.NO_CONNECTION -> Toast.makeText(
                activity,
                R.string.connectivity_error,
                Toast.LENGTH_SHORT
            ).show()
            NetworkError.REQUEST_ERROR -> Toast.makeText(
                activity,
                R.string.request_error,
                Toast.LENGTH_SHORT
            ).show()
            NetworkError.USER_ALREADY_EXIST -> Toast.makeText(
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
        editor.putLong(getString(R.string.exp_date_payload), token.expDate!!.getTime()).apply()
    }

    // ----------------- Navigation -------------------------
    private fun goToProfileActivity(){
        var intent : Intent = Intent(
            requireActivity().applicationContext,
            EditProfileActivity::class.java
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
        return isHostCheckBox.isChecked
                || isAnimalWalkerCheckBox.isChecked
                || searchAnimalWalkerCheckBox.isChecked
                || searchHostCheckBox.isChecked
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
        emailEditText.addTextChangedListener(object : TextWatcher {

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
        passwordEditText.addTextChangedListener(object : TextWatcher {

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
        passwordValidationEditText.addTextChangedListener(object : TextWatcher {

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
        lastnameEditText.addTextChangedListener(object : TextWatcher {

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
        firstnameEditText.addTextChangedListener(object : TextWatcher {

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
        phoneEditText.addTextChangedListener(object : TextWatcher {

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
        streetNameEditText.addTextChangedListener(object : TextWatcher {

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
        streetNumberEditText.addTextChangedListener(object : TextWatcher {

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
        cityEditText.addTextChangedListener(object : TextWatcher {

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
        postalCodeEditText.addTextChangedListener(object : TextWatcher {

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
        if (emailEditText.text.isEmpty()) {
            emailTextInputLayout.setErrorEnabled(true)
            emailTextInputLayout.error = getString(R.string.email_empty_error)
        } else {
            if (!(Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches())) {
                emailTextInputLayout.setErrorEnabled(true)
                emailTextInputLayout.error =  getString(R.string.email_format_error)
            } else {
                emailTextInputLayout.setErrorEnabled(false)
                emailTextInputLayout.error = null
            }
        }
    }

    private fun passwordCheckError(){
        if (passwordEditText.text.isEmpty()) {
            passwordTextInputLayout.setErrorEnabled(true)
            passwordTextInputLayout.error = getString(R.string.password_empty_error)
        } else {
            passwordTextInputLayout.setErrorEnabled(true)
            passwordTextInputLayout.error = null
        }
    }

    private fun validationPasswordCheckError(){
        if (passwordValidationEditText.text.isEmpty()) {
            passwordValidationTextInputLayout.setErrorEnabled(true)
            passwordValidationTextInputLayout.error = getString(R.string.validation_password_empty_error)
        } else {
            if (!((passwordValidationEditText.text.toString()).equals(passwordEditText.text.toString()))) {
                passwordValidationTextInputLayout.setErrorEnabled(true)
                passwordValidationTextInputLayout.error = getString(R.string.validation_password_format_error)
            }
            else {
                passwordValidationTextInputLayout.setErrorEnabled(false)
                passwordValidationTextInputLayout.error = null
            }
        }
    }

    private fun lastnameCheckError(){
        if (lastnameEditText.text.isEmpty()) {
            lastnameTextInputLayout.setErrorEnabled(true)
            lastnameTextInputLayout.error = getString(R.string.lastname_empty_error)
        } else {
            if (!(Pattern.compile("^[a-zéèçàïôëA-Z]{1,50}(-| )?([a-zéèçàïôëA-Z]{1,50})?$").matcher(
                    lastnameEditText.text
                ).matches())) {
                lastnameTextInputLayout.setErrorEnabled(true)
                lastnameTextInputLayout.error = getString(R.string.lastname_format_error)
            } else {
                lastnameTextInputLayout.setErrorEnabled(false)
                lastnameTextInputLayout.error = null
            }
        }
    }

    private fun firstnameCheckError(){
        if (firstnameEditText.text.isEmpty()) {
            firstnameTextInputLayout.setErrorEnabled(true)
            firstnameTextInputLayout.error = getString(R.string.firstname_empty_error)
        } else {
            if (!(Pattern.compile("^[a-zéèçàïôëA-Z]{1,50}(-| )?([a-zéèçàïôëA-Z]{1,50})?$").matcher(
                    firstnameEditText.text
                ).matches())) {
                firstnameTextInputLayout.setErrorEnabled(true)
                firstnameTextInputLayout.error = getString(R.string.firstname_format_error)
            } else {
                firstnameTextInputLayout.setErrorEnabled(false)
                firstnameTextInputLayout.error = null
            }
        }
    }

    private fun phoneCheckError(){
        if (phoneEditText.text.isEmpty()) {
            phoneTextInputLayout.setErrorEnabled(true)
            phoneTextInputLayout.error = getString(R.string.phone_empty_error)
        } else {
            if (!(Patterns.PHONE.matcher(phoneEditText.text).matches())) {
                phoneTextInputLayout.setErrorEnabled(true)
                phoneTextInputLayout.error = getString(R.string.phone_format_error)
            } else {
                phoneTextInputLayout.setErrorEnabled(false)
                phoneTextInputLayout.error = null
            }
        }
    }

    private fun streetNameCheckError(){
        if (streetNameEditText.text.isEmpty()) {
            streetNameTextInputLayout.setErrorEnabled(true)
            streetNameTextInputLayout.error = getString(R.string.street_name_empty_error)
        }
        else {
            if (!(Pattern.compile("^\\s*[a-zA-Z]{1}[a-zA-Z][a-zA-Z '-]*\$").matcher(
                    streetNameEditText.text
                ).matches())) {
                streetNameTextInputLayout.setErrorEnabled(true)
                streetNameTextInputLayout.error = getString(R.string.street_name_format_error)
            } else {
                streetNameTextInputLayout.setErrorEnabled(false)
                streetNameTextInputLayout.error = null

            }
        }
    }

    private fun streetNumberCheckError(){
        if (streetNumberEditText.text.isEmpty()) {
            streetNumberTextInputLayout.setErrorEnabled(true)
            streetNumberTextInputLayout.error = getString(R.string.street_number_empty_error)
        } else {
            if (!( Pattern.compile("^(\\d{1,3})\\w{0,3}\$").matcher(streetNumberEditText.text).matches())){
                streetNumberTextInputLayout.setErrorEnabled(true)
                streetNumberTextInputLayout.error = getString(R.string.street_number_format_error)
            }
            else {
                streetNumberTextInputLayout.setErrorEnabled(false)
                streetNumberTextInputLayout.error = null
            }
        }
    }

    private fun localityCheckError(){
        if (cityEditText.text.isEmpty()) {
            cityTextInputLayout.setErrorEnabled(true)
            cityTextInputLayout.error = getString(R.string.locality_empty_error)
        } else {
            if (!(Pattern.compile("^\\s*[a-zA-Z]{1}[a-zA-Z][a-zA-Z '-]*\$").matcher(cityEditText.text).matches())) {
                cityTextInputLayout.setErrorEnabled(true)
                cityTextInputLayout.error = getString(R.string.locality_format_error)
            } else {
                cityTextInputLayout.setErrorEnabled(false)
                cityTextInputLayout.error = null

            }
        }
    }

    private fun postalCodeCheckError(){
        if (postalCodeEditText.text.isEmpty()) {
            postalCodeTextInputLayout.setErrorEnabled(true)
            postalCodeTextInputLayout.error = getString(R.string.postale_code_empty_error)
        } else {
            if (!(Pattern.compile("^(\\d{4,10})\$").matcher(postalCodeEditText.text).matches())) {
                postalCodeTextInputLayout.setErrorEnabled(true)
                postalCodeTextInputLayout.error = getString(R.string.postale_code_format_error)
            } else {
                postalCodeTextInputLayout.setErrorEnabled(true)
                postalCodeTextInputLayout.error = null

            }
        }
    }

    private fun countryCheckError(){
        if(countryDropDown.text.toString() == ""){
            countryTextInputLayout.setErrorEnabled(true)
            countryTextInputLayout.error = getString(R.string.country_error)
        }
        else{
            countryTextInputLayout.setErrorEnabled(false)
            countryTextInputLayout.error = null
        }
    }





    // ----------------- ADD USER & AsyncTask  -------------------------

    private fun addUser(){


            var email: String = emailEditText.text.toString()
            var password: String = passwordEditText.text.toString()
            var firstname: String = firstnameEditText.text.toString()
            var lastname: String = lastnameEditText.text.toString()
            var phone: String = phoneEditText.text.toString()
            var streetNumber: String = streetNumberEditText.text.toString()
            var streetName: String = streetNameEditText.text.toString()
            var locality: String = cityEditText.text.toString()
            var postalCode: Int = (postalCodeEditText.text.toString()).toInt()
            var isHost: Boolean = isHostCheckBox.isChecked
            var isAnimalWalker: Boolean = isAnimalWalkerCheckBox.isChecked
            var searchHost: Boolean = searchHostCheckBox.isChecked
            var searchAnimalWalker: Boolean = searchAnimalWalkerCheckBox.isChecked
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

            //return user

            AddUserTask().execute(user)
    }


    inner class AddUserTask : AsyncTask<User, Void, String>(){
        override fun doInBackground(vararg users: User): String {
            registrationViewModel.addUser(users[0])

            return users[0].firstname
        }

        /*override fun onPostExecute(user: String) {
            Toast.makeText(activity!!.applicationContext, "${user}", Toast.LENGTH_SHORT).show()

        }*/

    }


}