package com.smartcity.petsservices.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.FragmentEditProfileBinding
import com.smartcity.petsservices.model.Customer
import com.smartcity.petsservices.model.Error
import com.smartcity.petsservices.model.Supplier
import com.smartcity.petsservices.model.Token
import com.smartcity.petsservices.model.User
import com.smartcity.petsservices.ui.viewModel.EditProfileViewModel
import java.util.*
import java.util.regex.Pattern
import kotlin.properties.Delegates

class EditProfileFragment : Fragment() {
    lateinit var binding: FragmentEditProfileBinding
    lateinit var editProfileViewModel : EditProfileViewModel

    lateinit var countryDropDown : AutoCompleteTextView

    // check form
    var isValidateEmail : Boolean = false
    var isValidateFirstname : Boolean = false
    var isValidateLastname : Boolean = false
    var isValidatePhone : Boolean = false
    var isValidateStreetName : Boolean = false
    var isValidateStreetNumber : Boolean = false
    var isValidateLocality : Boolean = false
    var isValidatePostalCode : Boolean = false

    // check form for supplier and customer
    var isValidateSupplierLocality : Boolean = false
    var isValidateWeightMax : Boolean = false
    var isValidateCustomerLocality : Boolean = false

    //SharedPreferences
    lateinit var sharedPref : SharedPreferences
    // arguments
    lateinit var email : String
    lateinit var lastname :String
    lateinit var firstname : String
    lateinit var phone : String
    lateinit var streetName : String
    lateinit var streetNumber : String
    lateinit var locality : String
    var postalCode by Delegates.notNull<Int>()
    lateinit var country : String
    var isHost : Boolean = false
    var isAnimalWalker: Boolean = false
    var searchHost : Boolean = false
    var searchAnimalWalker : Boolean = false
    var slogan : String = R.string.EMPTY.toString()
    var weightMax : Int = -1
    var supplierLocality : String  = R.string.EMPTY.toString()
    var customerLocality : String = R.string.EMPTY.toString()

    private lateinit var jwt : Token


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        editProfileViewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        binding.viewModel = editProfileViewModel
        binding.lifecycleOwner = this
        sharedPref = requireActivity().getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);


        var userId = sharedPref.getInt(getString(R.string.user_id_payload), 0)
        var token = sharedPref.getString(getString(R.string.token), "")!!
        var email  = sharedPref.getString(getString(R.string.email_payload), "")!!
        var exp  =sharedPref.getLong(getString(R.string.exp_date_payload), 0)!!

        // convert long in date
        val exp_date = Date(exp * 1000)
        jwt =  Token(email, userId, exp_date, token)

        countryDropDown = binding.countryDropdown

        inputsVerifier()




        completeFields()
        displayFieldsAccordingRole()

        initCountryDropDown()

        editProfileViewModel.error.observe(viewLifecycleOwner){ error: Error -> this.displayErrorScreen(
                error
        ) }

        // Cancelled button
        binding.cancelledEditProfileButton.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_editProfileFragment_to_profileFragment)
        }

        // edit profile button
        binding.editProfileButton.setOnClickListener {
            if(validateForm() && validateRoles()){
                updateUser()
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
        return binding.root
    }



    private fun displayFieldsAccordingRole(){
        binding.supplierLayout.visibility = View.GONE
        binding.customerLayout.visibility = View.GONE
        if(isHost || isAnimalWalker){
            binding.supplierLayout.visibility = View.VISIBLE
        }
        if(searchAnimalWalker || searchHost){
            binding.customerLayout.visibility = View.VISIBLE
        }
    }

    private fun completeFields(){


        email = requireArguments().getString(ARG_EMAIL)!!
        lastname = requireArguments().getString(ARG_LASTNAME)!!
        firstname = requireArguments().getString(ARG_FIRSTNAME)!!
        phone = requireArguments().getString(ARG_PHONE)!!
        streetName = requireArguments().getString(ARG_STREET_NAME)!!
        streetNumber = requireArguments().getString(ARG_STREET_NUMBER)!!
        locality = requireArguments().getString(ARG_LOCALITY)!!
        postalCode = requireArguments().getInt(ARG_POSTAL_CODE)!!
        country = requireArguments().getString(ARG_COUNTRY)!!
        isHost = requireArguments().getBoolean(ARG_IS_HOST)!!
        isAnimalWalker = requireArguments().getBoolean(ARG_IS_ANIMAL_WALKER)!!
        searchAnimalWalker = requireArguments().getBoolean(ARG_IS_SEARCH_HOST)!!
        searchHost = requireArguments().getBoolean(ARG_IS_SEARCH_ANIMAL_WALKER)!!
        slogan = requireArguments().getString(ARG_SLOGAN)!!
        weightMax = requireArguments().getInt(ARG_WEIGHT_MAX)!!
        supplierLocality = requireArguments().getString(ARG_SUPPLIER_LOCALITY)!!
        customerLocality = requireArguments().getString(ARG_CUSTOMER_LOCALITY)!!

        binding.emailTextInputLayout.editText!!.setText(email)
        binding.lastnameTextInputLayout.editText!!.setText(lastname)
        binding.firstnameTextInputLayout.editText!!.setText(firstname)
        binding.phoneTextInputLayout.editText!!.setText(phone)
        binding.streetNameTextInputLayout.editText!!.setText(streetName)
        binding.streetNumberTextInputLayout.editText!!.setText(streetNumber)
        binding.cityTextInputLayout.editText!!.setText(locality)
        binding.postalCodeTextInputLayout.editText!!.setText(postalCode.toString())
        binding.countryDropdown.text = country.toEditable()


        binding.checkboxHost.isChecked = isHost
        binding.checkboxAnimalWalker.isChecked = isAnimalWalker
        binding.checkboxSearchHost.isChecked = searchHost
        binding.checkboxSearchWalker.isChecked = searchAnimalWalker

        binding.sloganTextInputLayout.editText!!.setText(slogan)
        binding.weightMaxTextInputLayout.editText!!.setText(if (weightMax != -1 ) weightMax.toString() else  getString(R.string.EMPTY))
        binding.supplierCommuneTextInputLayout.editText!!.setText(supplierLocality)
        binding.customerCommuneTextInputLayout.editText!!.setText(customerLocality)
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun addCustomer(searchHost: Boolean, searchAnimalWalker: Boolean, locality: String) : Customer{
        var l = if(locality.equals(getString(R.string.EMPTY))) null else locality

        var customer = Customer(l, searchAnimalWalker, searchHost)
        return customer
    }

    private fun addSuppplier(isHost: Boolean, isAnimalWalker: Boolean, slogan: String, locality: String, weightMax: Int?) : Supplier {
        var l = if(locality.equals(getString(R.string.EMPTY))) null else locality
        var s = if(slogan.equals(getString(R.string.EMPTY))) null else slogan

        var supplier = Supplier(isHost, isAnimalWalker, s, l, weightMax)
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

    // -----------------Fields Errors -------------------------

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
                NavHostFragment.findNavController(this).navigate(R.id.action_editProfileFragment_to_profileFragment)
            }
        }
    }

    private fun inputsVerifier(){
        emailTextChangedListener()
        lastnameTextChangedListener()
        localityTextChangedListener()
        postalCodeTextChangedListener()
        firstnameTextChangedListener()
        phoneTextChangedListener()
        streetNameTextChangedListener()
        streetNumberTextChangedListener()
        supplierLocalityTextChangedListener()
        customerLocalityTextChangedListener()
        supplierWeightMaxTextChangedListener()

    }

    private fun validateForm():Boolean{
        return isValidateEmail
                && isValidateFirstname
                && isValidateLastname
                && isValidatePhone
                && isValidateStreetName
                && isValidateStreetNumber
                && isValidateLocality
                && isValidatePostalCode
                && isValidateCustomerLocality
                && isValidateSupplierLocality
                && isValidateWeightMax
    }

    private fun validateRoles():Boolean{
        return binding.checkboxHost.isChecked
                || binding.checkboxAnimalWalker.isChecked
                || binding.checkboxSearchHost.isChecked
                || binding.checkboxSearchWalker.isChecked
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

    private fun supplierLocalityTextChangedListener(){
        binding.supplierCommuneTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                supplierLocalityCheckError()
            }
        })
    }

    private fun customerLocalityTextChangedListener(){
        binding.customerCommuneTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                customerLocalityCheckError()
            }
        })
    }

    private fun supplierWeightMaxTextChangedListener(){
        binding.weightMaxTextInputLayout.editText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                supplierWeightMaxCheckError()
            }
        })
    }

    // ----------------- Check Error -------------------------

    private fun emailCheckError(){
        if (binding.emailTextInputLayout.editText!!.text.isEmpty()) {
            binding.emailTextInputLayout.setErrorEnabled(true)
            binding.emailTextInputLayout.error = getString(R.string.email_empty_error)
            isValidateEmail = false
        } else {
            if (!(Patterns.EMAIL_ADDRESS.matcher(binding.emailTextInputLayout.editText!!.text).matches())) {
                binding.emailTextInputLayout.setErrorEnabled(true)
                binding.emailTextInputLayout.error =  getString(R.string.email_format_error)
                isValidateEmail = false
            } else {
                binding.emailTextInputLayout.setErrorEnabled(false)
                binding.emailTextInputLayout.error = null
                isValidateEmail = true
            }
        }
    }

    private fun lastnameCheckError(){
        if (binding.lastnameTextInputLayout.editText!!.text.isEmpty()) {
            binding.lastnameTextInputLayout.setErrorEnabled(true)
            binding.lastnameTextInputLayout.error = getString(R.string.lastname_empty_error)
            isValidateLastname = false
        } else {
            if (!(Pattern.compile("^[a-zéèçàïôëA-Z]{1,50}(-| )?([a-zéèçàïôëA-Z]{1,50})?$").matcher(
                            binding.lastnameTextInputLayout.editText!!.text
                    ).matches())) {
                binding.lastnameTextInputLayout.setErrorEnabled(true)
                binding.lastnameTextInputLayout.error = getString(R.string.lastname_format_error)
                isValidateLastname = false
            } else {
                binding.lastnameTextInputLayout.setErrorEnabled(false)
                binding.lastnameTextInputLayout.error = null
                isValidateLastname = true
            }
        }
    }

    private fun firstnameCheckError(){
        if (binding.firstnameTextInputLayout.editText!!.text.isEmpty()) {
            binding.firstnameTextInputLayout.setErrorEnabled(true)
            binding.firstnameTextInputLayout.error = getString(R.string.firstname_empty_error)
            isValidateFirstname = false
        } else {
            if (!(Pattern.compile("^[a-zéèçàïôëA-Z]{1,50}(-| )?([a-zéèçàïôëA-Z]{1,50})?$").matcher(
                            binding.firstnameTextInputLayout.editText!!.text
                    ).matches())) {
                binding.firstnameTextInputLayout.setErrorEnabled(true)
                binding.firstnameTextInputLayout.error = getString(R.string.firstname_format_error)
                isValidateFirstname = false
            } else {
                binding.firstnameTextInputLayout.setErrorEnabled(false)
                binding.firstnameTextInputLayout.error = null
                isValidateFirstname = true
            }
        }
    }

    private fun phoneCheckError(){
        if (binding.phoneTextInputLayout.editText!!.text.isEmpty()) {
            binding.phoneTextInputLayout.setErrorEnabled(true)
            binding.phoneTextInputLayout.error = getString(R.string.phone_empty_error)
            isValidatePhone = false
        } else {
            if (!(Patterns.PHONE.matcher(binding.phoneTextInputLayout.editText!!.text).matches())) {
                binding.phoneTextInputLayout.setErrorEnabled(true)
                binding.phoneTextInputLayout.error = getString(R.string.phone_format_error)
                isValidatePhone = false
            } else {
                binding.phoneTextInputLayout.setErrorEnabled(false)
                binding.phoneTextInputLayout.error = null
                isValidatePhone = true
            }
        }
    }

    private fun streetNameCheckError(){
        if (binding.streetNameTextInputLayout.editText!!.text.isEmpty()) {
            binding.streetNameTextInputLayout.setErrorEnabled(true)
            binding.streetNameTextInputLayout.error = getString(R.string.street_name_empty_error)
            isValidateStreetName = false
        }
        else {
            if (!(Pattern.compile("^\\s*[a-zA-Z]{1}[a-zA-Z][a-zA-Z '-]*\$").matcher(
                            binding.streetNameTextInputLayout.editText!!.text
                    ).matches())) {
                binding.streetNameTextInputLayout.setErrorEnabled(true)
                binding.streetNameTextInputLayout.error = getString(R.string.street_name_format_error)
                isValidateStreetName = false
            } else {
                binding.streetNameTextInputLayout.setErrorEnabled(false)
                binding.streetNameTextInputLayout.error = null
                isValidateStreetName = true

            }
        }
    }

    private fun streetNumberCheckError(){
        if (binding.streetNumberTextInputLayout.editText!!.text.isEmpty()) {
            binding.streetNumberTextInputLayout.setErrorEnabled(true)
            binding.streetNumberTextInputLayout.error = getString(R.string.street_number_empty_error)
            isValidateStreetNumber = false
        } else {
            if (!( Pattern.compile("^(\\d{1,3})\\w{0,3}\$").matcher(binding.streetNumberTextInputLayout.editText!!.text).matches())){
                binding.streetNumberTextInputLayout.setErrorEnabled(true)
                binding.streetNumberTextInputLayout.error = getString(R.string.street_number_format_error)
                isValidateStreetNumber = false
            }
            else {
                binding.streetNumberTextInputLayout.setErrorEnabled(false)
                binding.streetNumberTextInputLayout.error = null
                isValidateStreetNumber = true
            }
        }
    }

    private fun localityCheckError(){
        if (binding.cityTextInputLayout.editText!!.text.isEmpty()) {
            binding.cityTextInputLayout.setErrorEnabled(true)
            binding.cityTextInputLayout.error = getString(R.string.locality_empty_error)
            isValidateLocality = false
        } else {
            if (!(Pattern.compile("^\\s*[a-zA-Z]{1}[a-zA-Z][a-zA-Z '-]*\$").matcher(binding.cityTextInputLayout.editText!!.text).matches())) {
                binding.cityTextInputLayout.setErrorEnabled(true)
                binding.cityTextInputLayout.error = getString(R.string.locality_format_error)
                isValidateLocality = false
            } else {
                binding.cityTextInputLayout.setErrorEnabled(false)
                binding.cityTextInputLayout.error = null
                isValidateLocality = true

            }
        }
    }

    private fun supplierLocalityCheckError(){
        if (binding.supplierCommuneTextInputLayout.editText!!.text.isEmpty()) {
            binding.supplierCommuneTextInputLayout.setErrorEnabled(false)
            binding.supplierCommuneTextInputLayout.error = null
            isValidateSupplierLocality = true
        } else {
            if (!(Pattern.compile("^\\s*[a-zA-Z]{1}[a-zA-Z][a-zA-Z '-]*\$").matcher(binding.supplierCommuneTextInputLayout.editText!!.text).matches())) {
                binding.supplierCommuneTextInputLayout.setErrorEnabled(true)
                binding.supplierCommuneTextInputLayout.error = getString(R.string.locality_format_error)
                isValidateSupplierLocality = false
            } else {
                binding.supplierCommuneTextInputLayout.setErrorEnabled(false)
                binding.supplierCommuneTextInputLayout.error = null
                isValidateSupplierLocality = true

            }
        }
    }

    private fun customerLocalityCheckError(){
        if (binding.customerCommuneTextInputLayout.editText!!.text.isEmpty()) {
            binding.customerCommuneTextInputLayout.setErrorEnabled(false)
            binding.customerCommuneTextInputLayout.error = null
            isValidateCustomerLocality = true
        } else {
            if (!(Pattern.compile("^\\s*[a-zA-Z]{1}[a-zA-Z][a-zA-Z '-]*\$").matcher(binding.customerCommuneTextInputLayout.editText!!.text).matches())) {
                binding.customerCommuneTextInputLayout.setErrorEnabled(true)
                binding.customerCommuneTextInputLayout.error = getString(R.string.locality_format_error)
                isValidateCustomerLocality = false
            } else {
                binding.customerCommuneTextInputLayout.setErrorEnabled(false)
                binding.customerCommuneTextInputLayout.error = null
                isValidateCustomerLocality = true
            }
        }
    }

    private fun supplierWeightMaxCheckError(){
        if (binding.weightMaxTextInputLayout.editText!!.text.isEmpty()) {
            binding.weightMaxTextInputLayout.setErrorEnabled(false)
            binding.weightMaxTextInputLayout.error = null
            isValidateWeightMax = true
        } else {
            if (!(Pattern.compile("^(\\d{1,10})\$").matcher(binding.weightMaxTextInputLayout.editText!!.text).matches())) {
                binding.weightMaxTextInputLayout.setErrorEnabled(true)
                binding.weightMaxTextInputLayout.error = getString(R.string.weight_max_format_error)
                isValidateWeightMax = false
            } else {
                binding.weightMaxTextInputLayout.setErrorEnabled(false)
                binding.weightMaxTextInputLayout.error = null
                isValidateWeightMax = true
            }
        }
    }

    private fun postalCodeCheckError(){
        if (binding.postalCodeTextInputLayout.editText!!.text.isEmpty()) {
            binding.postalCodeTextInputLayout.setErrorEnabled(true)
            binding.postalCodeTextInputLayout.error = getString(R.string.postale_code_empty_error)
            isValidatePostalCode = false
        } else {
            if (!(Pattern.compile("^(\\d{4,10})\$").matcher(binding.postalCodeTextInputLayout.editText!!.text).matches())) {
                binding.postalCodeTextInputLayout.setErrorEnabled(true)
                binding.postalCodeTextInputLayout.error = getString(R.string.postale_code_format_error)
                isValidatePostalCode = false
            } else {
                binding.postalCodeTextInputLayout.setErrorEnabled(true)
                binding.postalCodeTextInputLayout.error = null
                isValidatePostalCode = true

            }
        }
    }



    // ----------------- Companion d'objet -------------------------
    companion object {
        private const val ARG_EMAIL = "ARG_EMAIL"
        private const val ARG_LASTNAME = "ARG_LASTNAME"
        private const val ARG_FIRSTNAME = "ARG_FIRSTNAME"
        private const val ARG_PHONE = "ARG_PHONE"
        private const val ARG_STREET_NAME = "ARG_STREET_NAME"
        private const val ARG_STREET_NUMBER = "ARG_STREET_NUMBER"
        private const val ARG_LOCALITY = "ARG_LOCALITY"
        private const val ARG_POSTAL_CODE = "ARG_POSTAL_CODE"
        private const val ARG_COUNTRY = "ARG_COUNTRY"
        private const val ARG_IS_HOST = "ARG_IS_HOST"
        private const val ARG_IS_ANIMAL_WALKER = "ARG_IS_ANIMAL_WALKER"
        private const val ARG_IS_SEARCH_HOST = "ARG_IS_SEARCH_HOST"
        private const val ARG_IS_SEARCH_ANIMAL_WALKER = "ARG_IS_SEARCH_ANIMAL_WALKER"
        private const val ARG_SLOGAN = "ARG_SLOGAN"
        private const val ARG_WEIGHT_MAX = "ARG_WEIGHT_MAX"
        private const val ARG_SUPPLIER_LOCALITY = "ARG_SUPPLIER_LOCALITY"
        private const val ARG_CUSTOMER_LOCALITY = "ARG_CUSTOMER_LOCALITY"
        fun newArguments(email: String,
                         lastname: String,
                         firstname: String,
                         phone: String,
                         streetName: String,
                         streetNumber: String,
                         locality: String,
                         postalCode: Int,
                         country: String,
                         isHost: Boolean,
                         isAnimalWalker: Boolean,
                         searchHost: Boolean,
                         searchAnimalWalker: Boolean,
                         slogan: String?,
                         weightMax: Int?,
                         supplierLocality: String?,
                         customerLocality: String?): Bundle {
            val args = Bundle()
            args.putString(ARG_EMAIL, email!!)
            args.putString(ARG_LASTNAME, lastname!!)
            args.putString(ARG_FIRSTNAME, firstname!!)
            args.putString(ARG_PHONE, phone!!)
            args.putString(ARG_STREET_NAME, streetName!!)
            args.putString(ARG_STREET_NUMBER, streetNumber!!)
            args.putString(ARG_LOCALITY, locality!!)
            args.putInt(ARG_POSTAL_CODE, postalCode!!)
            args.putString(ARG_COUNTRY, country!!)

            args.putBoolean(ARG_IS_HOST, isHost!!)
            args.putBoolean(ARG_IS_ANIMAL_WALKER, isAnimalWalker!!)
            args.putBoolean(ARG_IS_SEARCH_HOST, searchHost!!)
            args.putBoolean(ARG_IS_SEARCH_ANIMAL_WALKER, searchAnimalWalker!!)

            args.putString(ARG_SLOGAN,slogan)
            args.putInt(ARG_WEIGHT_MAX, weightMax ?: -1)
            args.putString(ARG_SUPPLIER_LOCALITY, supplierLocality)
            args.putString(ARG_CUSTOMER_LOCALITY, customerLocality)

            return args
        }
    }


    // ----------------- Update User AsyncTask -------------------------
    private fun updateUser(){
        var email: String = binding.emailTextInputLayout.editText!!.text.toString()
        var firstname: String = binding.firstnameTextInputLayout.editText!!.text.toString()
        var lastname: String = binding.lastnameTextInputLayout.editText!!.text.toString()
        var phone: String = binding.phoneTextInputLayout.editText!!.text.toString()
        var streetNumber: String = binding.streetNumberTextInputLayout.editText!!.text.toString()
        var streetName: String = binding.streetNameTextInputLayout.editText!!.text.toString()
        var locality: String = binding.cityTextInputLayout.editText!!.text.toString()
        var postalCode: Int = (binding.postalCodeTextInputLayout.editText!!.text.toString()).toInt()
        var isHost: Boolean = binding.checkboxHost.isChecked
        var isAnimalWalker: Boolean = binding.checkboxAnimalWalker.isChecked
        var searchHost: Boolean =binding.checkboxSearchHost.isChecked
        var searchAnimalWalker: Boolean = binding.checkboxSearchWalker.isChecked
        var country : String = countryDropDown.text.toString()

        var customer: Customer = addCustomer(searchHost, searchAnimalWalker, binding.customerCommuneTextInputLayout.editText!!.text.toString())

        var supplier: Supplier = addSuppplier(
                isHost,
                isAnimalWalker,
                binding.sloganTextInputLayout.editText!!.text.toString(),
                binding.supplierCommuneTextInputLayout.editText!!.text.toString(),
                if(binding.weightMaxTextInputLayout.editText!!.text.toString().equals(getString(R.string.EMPTY))) null else   binding.weightMaxTextInputLayout.editText!!.text.toString().toInt()
              )

        var user = User(
                email,
                null,
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
        UpdateUserTask().execute(user)
    }

    inner class UpdateUserTask : AsyncTask<User, Void, String>(){
        override fun doInBackground(vararg users: User): String {
            editProfileViewModel.updateUser(users[0],jwt)

            return users[0].firstname
        }

        /*override fun onPostExecute(user: String) {
            Toast.makeText(activity!!.applicationContext, "${user}", Toast.LENGTH_SHORT).show()

        }*/

    }
}