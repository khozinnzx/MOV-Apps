package com.example.mov_apps.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mov_apps.R
import com.example.mov_apps.adapter.CheckOutAdapter
import com.example.mov_apps.databinding.FragmentCheckoutBinding
import com.example.mov_apps.ui.MainActivity
import com.example.mov_apps.ui.MoviesViewModel
import com.example.mov_apps.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutFragment : Fragment(R.layout.fragment_checkout) {
    private lateinit var binding: FragmentCheckoutBinding
    val args: CheckoutFragmentArgs by navArgs()
    lateinit var checkOutAdapter: CheckOutAdapter
    lateinit var viewModel: MoviesViewModel
    private val userCollectionRef = Firebase.firestore.collection("user")
    private val auth = FirebaseAuth.getInstance()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCheckoutBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        viewModel.retrieveUser(auth.currentUser?.uid)

        val movie = args.resultMovie
        val dataList = args.dataList.toCollection(ArrayList())
        var totalBalance = 0
        var total = 0
        var sisaSaldo = 0
        val place = binding.tvPlace.text.toString()

        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance()
        val formatedDate = formatter.format(date)

        val localID = Locale("id", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localID)
        binding.tvDateaAndTime.text = formatedDate


        checkOutAdapter = CheckOutAdapter(dataList)
        binding.rvSeat.apply {
            adapter = checkOutAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.user.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { dataUser ->
                        totalBalance = dataUser.saldo
                        val harga = dataList.map { checkout ->
                            checkout.harga }
                        Log.d("Checkout", "list harga = ${harga} ")
                        total = harga.sum()
                        binding.tvTotalBalance.text = formatRupiah.format(totalBalance)
                        if (total <= totalBalance) {
                            sisaSaldo = totalBalance - total
                            binding.tvTotalHarga.setTextColor(Color.GREEN)
                            binding.tvTotalHarga.text = formatRupiah.format(total)
                            binding.btnBayarSekarang.visibility = View.VISIBLE
                        } else {
                            binding.tvTotalHarga.setTextColor(Color.RED)
                            binding.tvTotalHarga.text = formatRupiah.format(total)
                            binding.tvAlertSaldo.text =
                                "Maaf Saldo E-Wallet anda tidak cukup, silahkan isi terlebih dahulu"
                        }

                    }
                }
            }
        })

        binding.btnBatalkan.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutFragment_to_homeFragment)
        }

        binding.btnBayarSekarang.setOnClickListener {
            sisaSaldo.let {
                updateSaldoToFirestore(sisaSaldo)
                binding.progressBar3.visibility = View.VISIBLE
                binding.btnBayarSekarang.visibility = View.INVISIBLE
                viewModel.addMoviesCheckout(
                    auth.currentUser?.uid,
                    movie.title,
                    movie.vote_average.toInt(),
                    formatedDate,
                    place,
                    dataList,
                    movie.poster_path,
                    total.toDouble(),
                    "bayar"
                )

                Log.d("CheckOut", "save moviesCheckout: success ")
                binding.progressBar3.visibility = View.GONE
                binding.btnBayarSekarang.visibility = View.VISIBLE
                findNavController().navigate(R.id.action_checkoutFragment_to_successFragment)
            }

        }
    }


    private fun updateSaldoToFirestore(sisaSaldo: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val userQuery = userCollectionRef
                .whereEqualTo("uid", auth.currentUser?.uid).get().await()
            if (userQuery.documents.isNotEmpty()) {
                for (document in userQuery) {
                    try {
                        userCollectionRef.document(document.id).update("saldo", sisaSaldo)
                            .await()
                        Log.d("CheckOut", "updateSaldoToFirestore: success ")
                        findNavController().navigate(R.id.action_checkoutFragment_to_successFragment)

                    } catch (e: Exception) {
                        Log.d("CheckOut", "updateSaldoToFirestore: failed ")
                    }
                }
            }

        }
    }


}