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
import androidx.navigation.fragment.NavHostFragment
import com.smartcity.petsservices.R
import com.smartcity.petsservices.databinding.FragmentProfileBinding
import com.smartcity.petsservices.model.Error
import com.smartcity.petsservices.model.Token
import com.smartcity.petsservices.model.User
import com.smartcity.petsservices.ui.viewModel.ProfileViewModel
import java.util.*

class ProfileFragment : Fragment() {

    lateinit var binding : FragmentProfileBinding
    lateinit var sharedPref : SharedPreferences
    lateinit var user : User
    private lateinit var profileViewModel : ProfileViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        profileViewModel =  ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding.viewModel = profileViewModel

        // get preferences
        sharedPref = requireActivity().getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);


        var userId = sharedPref.getInt(getString(R.string.user_id_payload), 0)
        var token = sharedPref.getString(getString(R.string.token), "")!!
        var email  = sharedPref.getString(getString(R.string.email_payload), "")!!
        var exp  =sharedPref.getLong(getString(R.string.exp_date_payload), 0)!!

        // convert long in date
        val exp_date = Date(exp * 1000)
       var jwt : Token =  Token(email, userId, exp_date, token)


        profileViewModel.error.observe(viewLifecycleOwner){ error: Error -> this.displayErrorScreen(
                error)
        }

        binding.updateProfileButton.setOnClickListener {
            var slogan =  if (profileViewModel.user.value!!.supplier.slogan == null ) getString(R.string.EMPTY) else  profileViewModel.user.value!!.supplier.slogan
            var customerLocality =  if (profileViewModel.user.value!!.customer.locality == null )  getString(R.string.EMPTY) else  profileViewModel.user.value!!.customer.locality
            var supplierLocality =  if (profileViewModel.user.value!!.supplier.locality == null )  getString(R.string.EMPTY) else  profileViewModel.user.value!!.supplier.locality
            val editProfileFragment : Bundle = EditProfileFragment.newArguments(
                    profileViewModel.user.value!!.email,
                    profileViewModel.user.value!!.lastname,
                    profileViewModel.user.value!!.firstname,
                    profileViewModel.user.value!!.phone,
                    profileViewModel.user.value!!.streetName,
                    profileViewModel.user.value!!.streetNumber,
                    profileViewModel.user.value!!.locality,
                    profileViewModel.user.value!!.postalCode,
                    profileViewModel.user.value!!.country,
                    profileViewModel.user.value!!.supplier.isHost,
                    profileViewModel.user.value!!.supplier.isAnimalWalker,
                    profileViewModel.user.value!!.customer.searchHost,
                    profileViewModel.user.value!!.customer.searchWalker,
                    slogan,
                    profileViewModel.user.value!!.supplier.weightMax,
                    supplierLocality,
                    customerLocality)
            NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_editProfileFragment, editProfileFragment)
        }

        getUser(jwt)
        binding.identity.visibility = View.GONE
        binding.updateProfileButton.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.absencesButton.visibility = View.GONE
        binding.animalsTypeButton.visibility = View.GONE
        binding.animalsButton.visibility = View.GONE

        return binding.root
    }

    private fun displayErrorScreen(error: Error){
        binding.progressBar.visibility = View.GONE
        when(error){
            Error.NO_ERROR -> {
                binding.identity.visibility = View.VISIBLE
                binding.updateProfileButton.visibility = View.VISIBLE
                displayFieldsAccordingData()
                displayButtonsAccordingRole()
                binding.errorLayout.visibility = View.GONE
            }
            Error.REQUEST_ERROR -> {
                binding.errorLayout.visibility = View.VISIBLE
                binding.connectivityError.visibility = View.GONE
                binding.requestError.visibility = View.VISIBLE
                binding.technicalError.visibility = View.GONE
                binding.identity.visibility = View.GONE
            }
            Error.TECHNICAL_ERROR -> {
                binding.errorLayout.visibility = View.VISIBLE
                binding.connectivityError.visibility = View.GONE
                binding.requestError.visibility = View.GONE
                binding.technicalError.visibility = View.VISIBLE
                binding.identity.visibility = View.GONE
            }
            else ->{
                binding.errorLayout.visibility = View.VISIBLE
                binding.connectivityError.visibility = View.VISIBLE
                binding.requestError.visibility = View.GONE
                binding.technicalError.visibility = View.GONE
                binding.identity.visibility = View.GONE
            }
        }
    }

    private fun displayButtonsAccordingRole(){
        if(profileViewModel.user.value!!.supplier.isAnimalWalker || profileViewModel.user.value!!.supplier.isHost){
            binding.absencesButton.visibility = View.VISIBLE
            binding.animalsTypeButton.visibility = View.VISIBLE
        }
        if(profileViewModel.user.value!!.customer.searchHost || profileViewModel.user.value!!.customer.searchWalker){
            binding.animalsButton.visibility = View.VISIBLE
        }
    }

    private fun displayFieldsAccordingData(){
        if(profileViewModel.user.value!!.supplier.slogan == null || ((!profileViewModel.user.value!!.supplier.isAnimalWalker) && (!profileViewModel.user.value!!.supplier.isHost))){
            binding.slogan.visibility = View.GONE
        }
        if(profileViewModel.user.value!!.supplier.weightMax == null || ((!profileViewModel.user.value!!.supplier.isAnimalWalker) && (!profileViewModel.user.value!!.supplier.isHost))){
            binding.weightMax.visibility = View.GONE
        }
        if(!(profileViewModel.user.value!!.supplier.isAnimalWalker) && !(profileViewModel.user.value!!.supplier.isHost)){
            binding.evaluations.visibility = View.GONE
        }
    }

    // ----------------- Get User & AsyncTask  -------------------------

    private fun getUser(token: Token){
        UserTask().execute(token)
    }

    inner class UserTask : AsyncTask<Token, Void, String>(){
        override fun doInBackground(vararg token: Token): String {
            profileViewModel.getUser(token[0])

            return R.string.EMPTY.toString()
        }
    }

}