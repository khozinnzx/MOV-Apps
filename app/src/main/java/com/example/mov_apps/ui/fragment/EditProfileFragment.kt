package com.example.mov_apps.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mov_apps.R
import com.example.mov_apps.databinding.FragmentEditProfileBinding
import com.example.mov_apps.ui.MainActivity
import com.example.mov_apps.ui.MoviesViewModel
import com.example.mov_apps.utils.Constant
import com.example.mov_apps.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class EditProfileFragment: Fragment(R.layout.fragment_edit_profile) {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var viewModel: MoviesViewModel
    var uriPhoto: Uri? = null
    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditProfileBinding.bind(view)

        viewModel = (activity as MainActivity).viewModel
        viewModel.retrieveUser(auth.currentUser?.uid)
        viewModel.user.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressBarImage.visibility = View.INVISIBLE
                    it.data?.let { dataUser ->
                        Glide.with(this).load(dataUser.url).placeholder(R.drawable.ic_profile)
                            .centerCrop()
                            .into(binding.ivPhoto)
                        binding.etNama.setText(dataUser.nama)
                        binding.etEmail.setText(dataUser.email)
                        binding.etUsername.setText(dataUser.username)
                        binding.etPassword.setText(dataUser.password)

                    }
                }
                is Resource.Error -> {
                    binding.progressBarImage.visibility = View.INVISIBLE
                    it.message?.let {
                        Log.d("ProfileFragment", "eror akses data user: ")
                    }
                }
                is Resource.Loading -> {
                    binding.progressBarImage.visibility = View.VISIBLE
                }
            }
        })

        binding.btnUpdate.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            updateUser()
        }

        binding.ivPhoto.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, Constant.REQUEST_COD_IMAGE_PICK)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == Constant.REQUEST_COD_IMAGE_PICK) {
            uriPhoto = data?.data
            Glide.with(this).load(uriPhoto).placeholder(R.drawable.ic_profile).centerCrop()
                .into(binding.ivPhoto)
            binding.btnUpdate.visibility = View.VISIBLE

        }
    }

    private fun updateUser() {
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        val name = binding.etNama.text.toString()
        val email = binding.etEmail.text.toString()

        if(username.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && email.isNotEmpty()){
            viewModel.updateUser(username, password, name, email)

            val filename = UUID.randomUUID().toString()
            viewModel.uploadImageToStorage(filename, uriPhoto)
            binding.progressBar.visibility = View.INVISIBLE
            findNavController().navigate(R.id.action_editProfileFragment_to_homeFragment)

        }else{
            Toast.makeText(activity, "semua data harus di isi", Toast.LENGTH_SHORT).show()
        }



    }


}