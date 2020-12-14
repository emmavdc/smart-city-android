package com.smartcity.petsservices.ui.fragment

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
import com.smartcity.petsservices.databinding.RegistrationFragmentBinding
import com.smartcity.petsservices.model.Customer
import com.smartcity.petsservices.model.Supplier
import com.smartcity.petsservices.model.User
import com.smartcity.petsservices.ui.viewModel.RegistrationViewModel
import java.util.regex.Pattern


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



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = registrationViewModel
        binding.lifecycleOwner = this

        //EditText
        emailEditText = binding.emailEditText.editText!!
        passwordEditText = binding.passwordEditText.editText!!
        passwordValidationEditText = binding.validationPasswordEditText.editText!!
        firstnameEditText = binding.firstnameEditText.editText!!
        lastnameEditText = binding.lastnameEditText.editText!!
        phoneEditText = binding.phoneEditText.editText!!
        streetNumberEditText = binding.streetNumberEditText.editText!!
        streetNameEditText = binding.streetNameEditText.editText!!
        cityEditText = binding.cityEditText.editText!!
        postalCodeEditText = binding.postalCodeEditText.editText!!
        countryDropDown = binding.countryDropdown

        // CHeck Box
        isHostCheckBox = binding.checkboxHost
        isAnimalWalkerCheckBox = binding.checkboxAnimalWalker
        searchHostCheckBox = binding.checkboxSearchHost
        searchAnimalWalkerCheckBox = binding.checkboxSearchWalker

        // country
        /*val items = listOf("Material", "Design", "Components", "Android")
        val adapter = ArrayAdapter(requireContext(), R.layout.country_item, items)
        (countryEdittext as? AutoCompleteTextView)?.setAdapter(adapter)*/

        val COUNTRIES = arrayOf("Belgique", "France", "Luxembourg")

        val adapter = ArrayAdapter(
                requireContext(),
                R.layout.country_item,
                COUNTRIES)

        countryDropDown.setAdapter(adapter)


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
                if(!validateForm()){
                    Toast.makeText(activity, R.string.form_error, Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(activity, R.string.roles_error, Toast.LENGTH_SHORT).show()
                }

            }
        }

        inputsVerifier()

        registrationViewModel.getEmailMediator().observe(viewLifecycleOwner, Observer { validationResult ->
            isValidateEmail = validationResult
        })

        registrationViewModel.getValidationPasswordMediator().observe(viewLifecycleOwner, Observer { validationResult ->
            isValidateValidationPassword = validationResult
        })

        registrationViewModel.getPasswordMediator().observe(viewLifecycleOwner, Observer { validationResult ->
            isValidatePassword = validationResult
        })

        registrationViewModel.getLastnameMediator().observe(viewLifecycleOwner, Observer { validationResult ->
            isValidateLastname = validationResult
        })

        registrationViewModel.getFirstnameMediator().observe(viewLifecycleOwner, Observer { validationResult ->
            isValidateFirstname = validationResult
        })

        registrationViewModel.getPhoneMediator().observe(viewLifecycleOwner, Observer { validationResult ->
            isValidatePhone = validationResult
        })

        registrationViewModel.getStreetNameMediator().observe(viewLifecycleOwner, Observer { validationResult ->
            isValidateStreetName = validationResult
        })

        registrationViewModel.getStreetNumberMediator().observe(viewLifecycleOwner, Observer { validationResult ->
            isValidateStreetNumber = validationResult
        })

        registrationViewModel.getLocalityMediator().observe(viewLifecycleOwner, Observer { validationResult ->
            isValidateLocality = validationResult
        })

        registrationViewModel.getPostalCodeMediator().observe(viewLifecycleOwner, Observer { validationResult ->
            isValidatePostalCode = validationResult
        })

       return binding.root
    }

    private fun addCustomer(searchHost: Boolean, searchAnimalWalker: Boolean) : Customer?{

        if(searchHost || searchAnimalWalker){
            var customer = Customer(null, searchAnimalWalker, searchHost)
            return customer
        }
        else{
            return null
        }
    }

    private fun addSuppplier(isHost: Boolean, isAnimalWalker: Boolean) : Supplier? {
        if(isHost || isAnimalWalker){
            var supplier = Supplier(isHost, isAnimalWalker, null, null, null)
            return supplier
        }
        else{
            return null
        }
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

    private fun inputsVerifier(){
        emailInputVerifier()
        validationPasswordInputtVerifier()
        passwordInputVerifier()
        lastnameInputVerifier()
        firstnameInputVerifier()
        phoneInputVerifier()
        streetNameInputVerifier()
        streetNumberInputVerifier()
        localityInputVerifier()
        postalCodeInputVerifier()

    }

    private fun emailInputVerifier(){
        emailEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (emailEditText.text.isEmpty()) {
                    emailEditText.error = getString(R.string.email_empty_error)
                } else {
                    if (!(Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches())) {
                        emailEditText.error = getString(R.string.email_format_error)
                    } else {
                        emailEditText.error = null
                    }
                }
            }
        })
    }

    private fun passwordInputVerifier(){
        passwordEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (passwordEditText.text.isEmpty()) {
                    passwordEditText.error = getString(R.string.password_empty_error)
                } else {
                    passwordEditText.error = null
                }
            }
        })
    }

    private fun validationPasswordInputtVerifier(){
        passwordValidationEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (passwordValidationEditText.text.isEmpty()) {
                    passwordValidationEditText.error = getString(R.string.validation_password_empty_error)
                } else {
                    if (passwordEditText.text.equals(passwordValidationEditText.text)) {
                        passwordValidationEditText.error = getString(R.string.validation_password_format_error)
                    } else {
                        passwordValidationEditText.error = null
                    }
                }
            }
        })
    }

    private fun lastnameInputVerifier(){
        lastnameEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (lastnameEditText.text.isEmpty()) {
                    lastnameEditText.error = getString(R.string.lastname_empty_error)
                } else {
                    if (!(Pattern.compile("^[a-zéèçàïôëA-Z]{1,50}(-| )?([a-zéèçàïôëA-Z]{1,50})?$").matcher(lastnameEditText.text).matches())) {
                        lastnameEditText.error = getString(R.string.lastname_format_error)
                    } else {
                        lastnameEditText.error = null
                    }
                }
            }
        })
    }

    private fun firstnameInputVerifier(){
        firstnameEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (firstnameEditText.text.isEmpty()) {
                    firstnameEditText.error = getString(R.string.firstname_empty_error)
                } else {
                    if (!(Pattern.compile("^[a-zéèçàïôëA-Z]{1,50}(-| )?([a-zéèçàïôëA-Z]{1,50})?$").matcher(firstnameEditText.text).matches())) {
                        firstnameEditText.error = getString(R.string.firstname_format_error)
                    } else {
                        firstnameEditText.error = null
                    }
                }
            }
        })
    }

    private fun phoneInputVerifier(){
        phoneEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (phoneEditText.text.isEmpty()) {
                    phoneEditText.error = getString(R.string.phone_empty_error)
                } else {
                    if (!(Patterns.PHONE.matcher(phoneEditText.text).matches())) {
                        phoneEditText.error = getString(R.string.phone_format_error)
                    } else {
                        phoneEditText.error = null
                    }
                }
            }
        })
    }

    private fun streetNameInputVerifier(){
        streetNameEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (streetNameEditText.text.isEmpty()) {
                    streetNameEditText.error = getString(R.string.street_name_empty_error)
                } else {
                    if (!(Pattern.compile("^\\s*[a-zA-Z]{1}[a-zA-Z][a-zA-Z '-]*\$").matcher(streetNameEditText.text).matches())) {
                        streetNameEditText.error = getString(R.string.street_name_format_error)
                    } else {
                        streetNameEditText.error = null

                    }
                }
            }
        })
    }

    private fun streetNumberInputVerifier(){
        streetNumberEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (streetNumberEditText.text.isEmpty()) {
                    streetNumberEditText.error = getString(R.string.street_number_empty_error)
                } else {
                    streetNumberEditText.error = null
                }
            }
        })
    }

    private fun localityInputVerifier(){
        cityEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (cityEditText.text.isEmpty()) {
                    cityEditText.error = getString(R.string.locality_empty_error)
                } else {
                    if (!(Pattern.compile("^\\s*[a-zA-Z]{1}[a-zA-Z][a-zA-Z '-]*\$").matcher(cityEditText.text).matches())) {
                        cityEditText.error = getString(R.string.locality_format_error)
                    } else {
                        cityEditText.error = null

                    }
                }
            }
        })
    }

    private fun postalCodeInputVerifier(){
        postalCodeEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (postalCodeEditText.text.isEmpty()) {
                    postalCodeEditText.error = getString(R.string.postale_code_empty_error)
                } else {
                    if (!(Pattern.compile("^(\\d{4,10})\$").matcher(postalCodeEditText.text).matches())) {
                        postalCodeEditText.error = getString(R.string.postale_code_format_error)
                    } else {
                        postalCodeEditText.error = null

                    }
                }
            }
        })
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

            var customer: Customer? = addCustomer(searchHost, searchAnimalWalker)
            var supplier: Supplier? = addSuppplier(isHost, isAnimalWalker)

            var user = User(email,
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
                    supplier)

            //return user

            AddUserTask().execute(user)
    }


    inner class AddUserTask : AsyncTask<User, Void, String>(){
        override fun doInBackground(vararg users: User): String {
            registrationViewModel.addUser(users[0])

            return users[0].firstname
        }

        /*override fun onPostExecute(result: String) {
            Toast.makeText(activity!!.applicationContext, "User $result added", Toast.LENGTH_SHORT).show()
        }*/

    }


}