package com.example.mov_apps.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.mov_apps.R
import com.example.mov_apps.model.Checkout
import com.example.mov_apps.model.Movie
import com.example.mov_apps.model.MovieCheckout
import com.example.mov_apps.model.User
import com.example.mov_apps.repository.MoviesRepository
import com.example.mov_apps.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception
import java.net.URI

const val TAG = "ViewModel"

class MoviesViewModel(val moviesRepository: MoviesRepository) : ViewModel() {

    private val userCollectionRef = Firebase.firestore.collection("user")
    private val checkoutCollectionRef = Firebase.firestore.collection("checkout")
    val imageRef = Firebase.storage.reference

    private var auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid

    val user: MutableLiveData<Resource<User>> = MutableLiveData()
    val moviesCheckout: MutableLiveData<Resource<MovieCheckout>> = MutableLiveData()
    val listMoviesCheckout: MutableLiveData<Resource<List<MovieCheckout>>> = MutableLiveData()

    val moviesNowPlaying: MutableLiveData<Resource<Movie>> = MutableLiveData()
    var moviesNowPlayingPage = 1
    var moviesNowPlayingResponse: Movie? = null

    val moviesComingSoon: MutableLiveData<Resource<Movie>> = MutableLiveData()
    var moviesComingSoonPage = 1
    var moviesComingSoonResponse: Movie? = null

    init {
        getMoviesNowPlaying()
        getMoviesComingSoon()
    }


    fun getMoviesCheckout(uid: String?) = viewModelScope.launch {
        user.postValue(Resource.Loading())
        try {
            val querySnapshot = checkoutCollectionRef
                .whereEqualTo("uid", uid).get().await()
            var list = ArrayList<MovieCheckout>()
            for (document in querySnapshot) {
                val data = document.toObject<MovieCheckout>()
                list.add(data)
                Log.d(TAG, "list : $list")
                listMoviesCheckout.postValue(Resource.Success(list))
            }

        } catch (e: Exception) {
            Log.d(TAG, "eror get movie checkout ${e.message.toString()}")
            Resource.Error(e.message.toString(), null)
        }
    }

    fun updateSeatSelected(seatSelected: ArrayList<Checkout>) {
        CoroutineScope(Dispatchers.IO).launch {
            val userQuery = checkoutCollectionRef
                .whereEqualTo("uid", auth.currentUser?.uid).get().await()
            if (userQuery.documents.isNotEmpty()) {
                for (document in userQuery) {
                    try {
                        checkoutCollectionRef.document(document.id).update("seat", seatSelected)
                            .await()
                        Log.d(TAG, "updateSeatToFirestore: success ")

                    } catch (e: Exception) {
                        Log.d(TAG, "updateSeatToFirestore: failed ")
                    }
                }
            }

        }
    }

    fun addMoviesCheckout(
        uid: String?,
        title: String,
        rating: Int,
        date: String,
        place: String,
        seat: ArrayList<Checkout>,
        poster: String
    ) {
        viewModelScope.launch {
            try {
                val movieCheckout =
                    MovieCheckout(
                        uid,
                        title,
                        rating,
                        date,
                        place,
                        seat,
                        poster
                    )
                checkoutCollectionRef.add(movieCheckout).await()
                Log.d(TAG, "addMoviesCheckout: success")
            } catch (e: Exception) {
                Log.d(TAG, "addMoviesCheckout: failed because ${e.message}")
            }
        }
    }

    fun retrieveUser(uid: String?) = viewModelScope.launch {
        user.postValue(Resource.Loading())
        try {
            val querySnapshot = userCollectionRef
                .whereEqualTo("uid", uid).get().await()
            for (document in querySnapshot.documents) {
                val data = document.toObject<User>()
                data?.let {
                    user.postValue(Resource.Success(it))
                    Log.d(TAG, "data : ${it.toString()}")
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            Resource.Error(e.message.toString(), null)
        }
    }

    fun updateUser(username: String, password: String, name: String, email: String) {
        viewModelScope.launch {
            val userQuery = userCollectionRef
                .whereEqualTo("uid", auth.currentUser?.uid).get().await()
            if (userQuery.documents.isNotEmpty()) {
                for (document in userQuery) {
                    try {
                        userCollectionRef.document(document.id).update("nama", name).await()
                        userCollectionRef.document(document.id).update("password", password).await()
                        auth.currentUser?.updatePassword(password)
                        userCollectionRef.document(document.id).update("email", email).await()
                        userCollectionRef.document(document.id).update("username", username).await()
                        val imageUrl = document["url"]
                        val imageRefFromUrl = Firebase.storage.getReferenceFromUrl(imageUrl as String)
                        deleteImage(imageRefFromUrl)
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "UpdateUser: Success ")
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "updateUser: failed")
                    }
                }
            }
        }
    }

    private fun deleteImage(imagerefFromUrl: StorageReference) {
        viewModelScope.launch {
           try {
               imagerefFromUrl.delete().await()

               Log.d(TAG, "deleteImage: success")

           }catch (e:Exception){}
            Log.d(TAG, "deleteImage: failed")
        }

    }

    fun uploadImageToStorage(filename: String, uriPhoto: Uri?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                uriPhoto?.let {
                    imageRef.child("images/$filename").putFile(it).await()
                    Log.d(TAG, "uploadImageToStorage: Success")
                    imageRef.child("images/$filename").downloadUrl.addOnSuccessListener { url ->
                        Log.d(TAG, "uploadImageToStorage: file location $url ")
                        saveToFirestore(url.toString())
                    }

                }

            } catch (e: Exception) {
                Log.d(TAG, "uploadImageToStorage: failed with ${e.message}")
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
                            Log.d(TAG, "success save to firestore with url : $url ")
                        }
                    }catch (e:Exception){
                        Log.d(TAG, "saveToFirestore: failed")
                    }
                }
            }
        }

    }


    fun getMoviesNowPlaying() = viewModelScope.launch {
        moviesNowPlaying.postValue(Resource.Loading())
        val response = moviesRepository.getMoviesNowPlaying(moviesNowPlayingPage)
        moviesNowPlaying.postValue(handleMoviesNowPlaying(response))
    }

    fun getMoviesComingSoon() = viewModelScope.launch {
        moviesComingSoon.postValue(Resource.Loading())
        val response = moviesRepository.getComingSoonMovie(moviesComingSoonPage)
        moviesComingSoon.postValue(handleMoviesComingSoon(response))
    }

    private fun handleMoviesNowPlaying(response: Response<Movie>): Resource<Movie> {
        if (response.isSuccessful) {
            try {
                response.body()?.let {
                    moviesNowPlayingPage++
                    if (moviesNowPlayingResponse == null) {
                        moviesNowPlayingResponse = it
                    } else {
                        val oldMovies = moviesNowPlayingResponse?.results
                        val newMovies = it.results
                        oldMovies?.addAll(newMovies)
                    }
                    return Resource.Success(moviesNowPlayingResponse ?: it)
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())

            }
        }
        return Resource.Error(response.message())
    }

    private fun handleMoviesComingSoon(response: Response<Movie>): Resource<Movie> {
        if (response.isSuccessful) {
            try {
                response.body()?.let {
                    moviesComingSoonPage++
                    if (moviesComingSoonResponse == null) {
                        moviesComingSoonResponse = it
                    } else {
                        val oldMovies = moviesComingSoonResponse?.results
                        val newMovies = it.results
                        oldMovies?.addAll(newMovies)
                    }
                    return Resource.Success(moviesComingSoonResponse ?: it)
                }
            } catch (e: Exception) {
                Log.d("ViewModel", e.message.toString())

            }
        }
        return Resource.Error(response.message())
    }
}