package com.smartcity.petsservices.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.FragmentProfileBinding
import com.smartcity.petsservices.model.Token
import com.smartcity.petsservices.ui.viewModel.ProfileViewModel
import java.util.*

class ProfileFragment : Fragment() {

    lateinit var binding : FragmentProfileBinding
    lateinit var sharedPref : SharedPreferences
    private lateinit var profileViewModel : ProfileViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        profileViewModel =  ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding.viewModel = profileViewModel

        // get preferences
        //sharedPref = this.requireActivity().getPreferences(Context.MODE_PRIVATE)
        sharedPref = requireActivity().getSharedPreferences("test", Context.MODE_PRIVATE);


        var userId = sharedPref.getInt(getString(R.string.user_id_payload), 0)
        var token = sharedPref.getString(getString(R.string.token), "")!!
        var email  = sharedPref.getString(getString(R.string.email_payload), "")!!
        var exp  =sharedPref.getLong(getString(R.string.exp_date_payload), 0)!!

        // convert long in date
        val exp_date = Date(exp  * 1000)
       var jwt : Token =  Token(email, userId, exp_date, token)

        getUser(jwt)

        return binding.root
    }

    // ----------------- Get User & AsyncTask  -------------------------

    private fun getUser(token: Token){
        UserTask().execute(token)
    }

    inner class UserTask : AsyncTask<Token, Void, String>(){
        override fun doInBackground(vararg token: Token): String {
            profileViewModel.getUser(token[0])

            return ""
        }
    }

}