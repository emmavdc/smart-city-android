package com.smartcity.petsservices.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.textfield.TextInputLayout
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.FragmentLoginBinding
import com.smartcity.petsservices.model.Error
import com.smartcity.petsservices.model.Login
import com.smartcity.petsservices.model.Token
import com.smartcity.petsservices.ui.activity.MainActivity
import com.smartcity.petsservices.ui.viewModel.LoginViewModel


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {


    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText

    lateinit var sharedPref : SharedPreferences

    private lateinit var binding : FragmentLoginBinding
    private lateinit var loginViewModel : LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        loginViewModel =  ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this


        //sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        sharedPref = requireActivity().getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);

        emailEditText = binding.emailTextInputLayout.editText!!
        passwordEditText = binding.passwordTextInputLayout.editText!!

        loginViewModel.error.observe(viewLifecycleOwner){ error: Error -> this.displayErrorScreen(
            error
        )
        }

        binding.loginConnectionButton.setOnClickListener {
            loginUser()
        }

        binding.loginRegistration.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        loginViewModel.jwt.observe(viewLifecycleOwner){ token: Token -> this.savePreferedValue(
            token
        )
            goToProfileActivity()
        }

        return binding.root
    }

    // ----------------- SharedPreferences  -------------------------
    private fun savePreferedValue(token: Token){
        var editor : SharedPreferences.Editor = sharedPref.edit()
        editor.putString(getString(R.string.email_payload), token.email)
        editor.putInt(getString(R.string.user_id_payload), token.userId!!)
        editor.putString(getString(R.string.token), token.token)
        editor.putLong(getString(R.string.exp_date_payload), token.expDate!!.getTime()).apply()
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
            Error.BAD_CREDENTIALS -> Toast.makeText(
                activity,
                R.string.bad_credentials,
                Toast.LENGTH_SHORT
            ).show()
            else -> {

            }
        }
    }

    // ----------------- Navigation -------------------------
    private fun goToProfileActivity(){
        var intent : Intent = Intent(
            requireActivity().applicationContext,
            MainActivity::class.java
        )
        startActivity(intent)
    }

    // ----------------- Login User & AsyncTask  -------------------------
    private fun loginUser(){
        var login = Login(
            emailEditText.text.toString(),
            passwordEditText.text.toString()
        )
       LoginUserTask().execute(login)
    }

    inner class LoginUserTask : AsyncTask<Login, Void, String>(){
        override fun doInBackground(vararg login : Login): String {
            loginViewModel.loginUser(login[0])

            return login[0].email
        }
    }
}