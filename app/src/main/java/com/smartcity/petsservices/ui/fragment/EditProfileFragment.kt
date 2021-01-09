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
import kotlin.properties.Delegates

class EditProfileFragment : Fragment() {
    lateinit var binding: FragmentEditProfileBinding
    lateinit var editProfileViewModel : EditProfileViewModel

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

    // check form for supplier and customer
    var isValidateSlogan : Boolean = false
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

        inputsVerifier()

        countryDropDown = binding.countryDropdown


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
                updateUser()
                NavHostFragment.findNavController(this).navigate(R.id.action_editProfileFragment_to_profileFragment)
                //showFieldsError()
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
    private fun inputsVerifier(){
        emailTextChangedListener()
        /*passwordTextChangedListener()
        validationPasswordTextChangedListener()
        lastnameTextChangedListener()
        firstnameTextChangedListener()
        phoneTextChangedListener()
        streetNameTextChangedListener()
        streetNumberTextChangedListener()
        localityTextChangedListener()
        postalCodeTextChangedListener()
        countrytextChangedListener()*/

    }


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

    private fun showFieldsError(){
        emailCheckError()
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