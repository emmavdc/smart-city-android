package com.smartcity.petsservices.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.textfield.TextInputLayout
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.FragmentEditProfileBinding
import com.smartcity.petsservices.databinding.FragmentRegistrationBinding
import com.smartcity.petsservices.ui.viewModel.EditProfileViewModel
import com.smartcity.petsservices.ui.viewModel.RegistrationViewModel

class EditProfileFragment : Fragment() {
    lateinit var binding: FragmentEditProfileBinding
    lateinit var editProfileViewModel : EditProfileViewModel

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
    // TextInputLayout as a supplier
    lateinit var sloganTextInputLayout: TextInputLayout
    lateinit var supplierCityTextInputLayout: TextInputLayout
    lateinit var weightMaxTextInputLayout: TextInputLayout

    // TextInputLayout as a customer
    lateinit var customerCityTextInputLayout: TextInputLayout

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
    lateinit var countryDropDown : AutoCompleteTextView
    // Edit Text as a supplier
    lateinit var sloganEditText: EditText
    lateinit var supplierCityEditText: EditText
    lateinit var weighteMaxEditText: EditText

    // Edit Text as a customer
    lateinit var customerCityEditText: EditText

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

    // check form for supplier
    var isValidateSlogan : Boolean = false
    var isValidateSupplierLocality : Boolean = false
    var isValidateWeightMax : Boolean = false

    // check form for customer
    var isValidateCustomerLocality : Boolean = false

    //SharedPreferences
    lateinit var sharedPref : SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        editProfileViewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        binding.viewModel = editProfileViewModel
        binding.lifecycleOwner = this
        sharedPref = requireActivity().getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);

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

        sloganTextInputLayout = binding.sloganTextInputLayout
        supplierCityTextInputLayout = binding.supplierCommuneTextInputLayout
        weightMaxTextInputLayout = binding.weightMaxTextInputLayout
        customerCityTextInputLayout = binding.customerCommuneTextInputLayout

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

        sloganEditText = binding.sloganTextInputLayout.editText!!
        supplierCityEditText = binding.supplierCommuneTextInputLayout.editText!!
        weighteMaxEditText = binding.weightMaxTextInputLayout.editText!!
        customerCityEditText = binding.customerCommuneTextInputLayout.editText!!

        // CHeck Box
        isHostCheckBox = binding.checkboxHost
        isAnimalWalkerCheckBox = binding.checkboxAnimalWalker
        searchHostCheckBox = binding.checkboxSearchHost
        searchAnimalWalkerCheckBox = binding.checkboxSearchWalker

        // init country dropdown
        initCountryDropDown()

        // Cancelled button
        binding.cancelledEditProfileButton.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_editProfileFragment_to_profileFragment)
        }

        // edit profile button
        binding.editProfileButton.setOnClickListener {
           // save le user

        }

        return binding.root
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
}