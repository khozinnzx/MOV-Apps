package com.example.mov_apps.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mov_apps.R
import com.example.mov_apps.databinding.ActivitySignUpBinding
import com.example.mov_apps.model.User
import com.example.mov_apps.ui.fragment.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    lateinit var auth: FirebaseAuth
    private val userCollectionRef = Firebase.firestore.collection("user")
    private val TAG = "SignUpActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.imageBack.setOnClickListener {
            Intent(this, SignInActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnMasukAkun.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val name = binding.etNama.text.toString()
        val username = binding.etUsername.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
            binding.btnMasukAkun.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    val user = User(auth.currentUser?.uid, email, name, password, url = "", username, saldo = 100000)
                    userCollectionRef.add(user).await()
                    Log.d(TAG, "registerUser: berhasil -> $user ")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Registrasi Berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressBar.visibility = View.GONE
                        binding.btnMasukAkun.visibility = View.VISIBLE
                        Intent(this@SignUpActivity, SignUpPhotoActivity::class.java).also {
                            it.putExtra("extra_username", username)
                            startActivity(it)
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SignUpActivity, e.message, Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                        binding.btnMasukAkun.visibility = View.VISIBLE
                    }
                }
            }

        } else {
            when {
                username.isEmpty() -> binding.etUsername.error = "mohon di isi terlebih dahulu"
                password.isEmpty() -> binding.etPassword.error = "mohon di isi terlebih dahulu"
                email.isEmpty() -> binding.etEmail.error = "mohon di isi terlebih dahulu"
                name.isEmpty() -> binding.etNama.error = "mohon di isi terlebih dahulu"

            }
        }
    }

}