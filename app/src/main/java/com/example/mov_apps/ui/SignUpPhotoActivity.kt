package com.example.mov_apps.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mov_apps.R
import com.example.mov_apps.databinding.ActivitySignUpPhotoBinding
import com.example.mov_apps.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*


class SignUpPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpPhotoBinding
    lateinit var auth: FirebaseAuth
    var uriPhoto: Uri? = null
    val imageRef = Firebase.storage.reference
    private val userCollectionRef = Firebase.firestore.collection("user")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val name = intent.getStringExtra("extra_username")
        binding.textView4.setText("Selamat Datang,\n${name}")

        binding.btnLewati.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.ivPhoto.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, Constant.REQUEST_COD_IMAGE_PICK)

            }
        }

        binding.btnSimpan.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnSimpan.visibility = View.INVISIBLE
            val filename = UUID.randomUUID().toString()
            uploadImageToStorage(filename)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == Constant.REQUEST_COD_IMAGE_PICK) {
            uriPhoto = data?.data
            Glide.with(this).load(uriPhoto).placeholder(R.drawable.ic_profile).centerCrop()
                .into(binding.ivPhoto)
            binding.btnSimpan.visibility = View.VISIBLE

        }
    }

    private fun uploadImageToStorage(filename: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                uriPhoto?.let {
                    imageRef.child("images/$filename").putFile(it).await()
                    Log.d("SignUpActivity", "uploadImageToStorage: Success")
                    imageRef.child("images/$filename").downloadUrl.addOnSuccessListener { url ->
                        Log.d("SignUpActivity", "uploadImageToStorage: file location $url ")
                        saveToFirestore(url.toString())
                    }

                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SignUpPhotoActivity, e.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    binding.btnSimpan.visibility = View.VISIBLE
                }
            }
        }


    }

    private fun saveToFirestore(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val userQuery = userCollectionRef
                .whereEqualTo("uid", auth.currentUser?.uid).get().await()
            if (userQuery.documents.isNotEmpty()){
                for (document in userQuery){
                    try {
                        userCollectionRef.document(document.id).update("url", url).await()
                        withContext(Dispatchers.Main){
                            Log.d("SignUpPhotoActivity", "url : $url ")
                            binding.progressBar.visibility = View.GONE
                            binding.btnSimpan.visibility = View.VISIBLE
                        }
                    }catch (e:Exception){
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@SignUpPhotoActivity, e.message, Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                            binding.btnSimpan.visibility = View.VISIBLE
                        }
                    }
                }
                Intent(this@SignUpPhotoActivity, MainActivity::class.java).also {
                    startActivity(it)
                }
            }
        }

    }


}