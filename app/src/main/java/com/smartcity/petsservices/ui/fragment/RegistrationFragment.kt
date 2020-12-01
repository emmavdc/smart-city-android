package com.smartcity.petsservices.ui.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.RegistrationFragmentBinding
import com.smartcity.petsservices.model.Customer
import com.smartcity.petsservices.model.Supplier
import com.smartcity.petsservices.model.User
import com.smartcity.petsservices.ui.viewModel.RegistrationViewModel
import retrofit2.Response.error


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
            addUser()
        }

        fillFields();
        inputsVerifier()

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
        passwordValidationEditText.setText("123");
        phoneEditText.setText("0497898965");
        streetNameEditText.setText("Rue de l'Ecluse");
        streetNumberEditText.setText("12");
        cityEditText.setText("Lobbes");
        postalCodeEditText.setText("6540");
    }

    // ----------------- Fields validation -------------------------

    private fun inputsVerifier(){
        emailInputVerifier()
        validationPasswordInputtVerifier()
    }

    private fun emailInputVerifier(){
        emailEditText.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(emailEditText.text.isEmpty()){
                    emailEditText.error = getString(R.string.email_empty_error)
                }
                else{
                    if(!(Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches())){
                        emailEditText.error = getString(R.string.email_format_error)
                    }
                    else{
                        emailEditText.error = null
                    }
                }
            }
        })
    }

    private fun validationPasswordInputtVerifier(){
        passwordValidationEditText.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(passwordValidationEditText.text.isEmpty()){
                    passwordValidationEditText.error = getString(R.string.validation_password_empty_error)
                }
                else{
                    if(passwordEditText.text != passwordValidationEditText.text){
                        passwordValidationEditText.error = getString(R.string.validation_password_format_error)
                    }
                    else{
                        passwordValidationEditText.error = null
                    }
                }
            }
        })
    }

    private fun validateEmail() : Boolean{
        return emailEditText.text.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches()
    }

    private fun validateValidationPassword() : Boolean{
        return passwordEditText.text == passwordValidationEditText.text && passwordValidationEditText.text.isNotEmpty()
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
                    country = "Belgique",
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