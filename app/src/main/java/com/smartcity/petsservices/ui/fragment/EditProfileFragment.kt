package com.smartcity.petsservices.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.FragmentEditProfileBinding
import com.smartcity.petsservices.ui.viewModel.EditProfileViewModel
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

    // check form for supplier
    var isValidateSlogan : Boolean = false
    var isValidateSupplierLocality : Boolean = false
    var isValidateWeightMax : Boolean = false

    // check form for customer
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
    lateinit var password : String
    var isHost : Boolean = false
    var isAnimalWalker: Boolean = false
    var searchHost : Boolean = false
    var searchAnimalWalker : Boolean = false
    var slogan : String = R.string.EMPTY.toString()
    var weightMax : Int = -1
    var supplierLocality : String  = R.string.EMPTY.toString()
    var customerLocality : String = R.string.EMPTY.toString()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        editProfileViewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        binding.viewModel = editProfileViewModel
        binding.lifecycleOwner = this
        sharedPref = requireActivity().getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);

        //requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).isVisible = false


        countryDropDown = binding.countryDropdown
        // init country dropdown
        initCountryDropDown()
        completeFields()


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

    // ----------------- Complete Fields -------------------------
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
        password = requireArguments().getString(ARG_PASSWORD)!!
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
        //binding.countryDropdown.editText!!.setText(email)

        // pas besoin
        //binding.passwordTextInputLayout.editText!!.setText(password)


        binding.checkboxHost.isChecked = isHost
        binding.checkboxAnimalWalker.isChecked = isAnimalWalker
        binding.checkboxSearchHost.isChecked = searchHost
        binding.checkboxSearchWalker.isChecked = searchAnimalWalker

        // CAN BE NULL
        binding.sloganTextInputLayout.editText!!.setText(slogan)
        binding.weightMaxTextInputLayout.editText!!.setText(if (weightMax != -1 ) weightMax.toString() else  getString(R.string.EMPTY))
        binding.supplierCommuneTextInputLayout.editText!!.setText(supplierLocality)
        binding.customerCommuneTextInputLayout.editText!!.setText(customerLocality)
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
        private const val ARG_PASSWORD = "ARG_PASSWORD"
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
                         password: String,
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
            args.putString(ARG_PASSWORD, password!!)

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


}