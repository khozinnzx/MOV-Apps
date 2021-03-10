package com.example.mov_apps.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mov_apps.R
import com.example.mov_apps.adapter.TransaksiAdapter
import com.example.mov_apps.databinding.FragmentMyWalletBinding
import com.example.mov_apps.databinding.FragmentTopUpBinding
import com.example.mov_apps.ui.MainActivity
import com.example.mov_apps.ui.MoviesViewModel
import com.example.mov_apps.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class TopUpFragment : Fragment(R.layout.fragment_top_up) {

    private lateinit var binding: FragmentTopUpBinding
    lateinit var viewModel: MoviesViewModel
    private val auth = FirebaseAuth.getInstance()

    var status10k: Boolean = false
    var status25k: Boolean = false
    var status50k: Boolean = false
    var status100k: Boolean = false
    var status200k: Boolean = false
    var status500k: Boolean = false

    var topuUp: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTopUpBinding.bind(view)

        viewModel.user.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { dataUser ->
                        binding.tvSaldo.text = dataUser.saldo.toString()
                    }
                }
                is Resource.Error -> {
                    it.message?.let {
                        Log.d("ProfileFragment", "eror akses data user: ")
                    }
                }
            }
        })
        checkTopup()


        binding.btnTOpUpNow.setOnClickListener {
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateInstance()
            val formatedDate = formatter.format(date)
            val customAmount = binding.etCustomAmount.text
            if (customAmount.isNotEmpty()){
                val amount = customAmount.toString().toDouble()
                viewModel.topUpSaldo(amount, formatedDate)
                Log.d("TopUp", "topup sebesar $customAmount success ")
                findNavController().navigate(R.id.action_topUpFragment_to_successTopUpFragment)

            }else if (status10k || status25k || status50k || status100k || status200k || status500k){
                if (topuUp != 0.0){
                    viewModel.topUpSaldo(topuUp, formatedDate)
                    Log.d("TopUp", "topup sebesar $topuUp success")
                    findNavController().navigate(R.id.action_topUpFragment_to_successTopUpFragment)
                }else{
                    Log.d("TopUp", "topup sebesar $topuUp gagal")
                }
            }else{
                Toast.makeText(activity, "silahkan memilih berapa yang akan anda top up", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun checkTopup() {
        binding.tv10k.setOnClickListener {
            if (status10k) {
                binding.tv10k.setBackgroundResource(R.drawable.shapes_line_white)
                binding.tv10k.setTextColor(resources.getColor(R.color.color_white_gray))
                status10k = false
                topuUp -= 10000
            } else {
                binding.tv10k.setBackgroundResource(R.drawable.shapes_line_pink)
                binding.tv10k.setTextColor(resources.getColor(R.color.color_pink))
                status10k = true
                topuUp += 10000
            }
        }

        binding.tv25k.setOnClickListener {
            if (status25k) {
                binding.tv25k.setBackgroundResource(R.drawable.shapes_line_white)
                binding.tv25k.setTextColor(resources.getColor(R.color.color_white_gray))
                status25k = false
                topuUp -= 25000
            } else {
                binding.tv25k.setBackgroundResource(R.drawable.shapes_line_pink)
                binding.tv25k.setTextColor(resources.getColor(R.color.color_pink))
                status25k = true
                topuUp += 25000
            }
        }

        binding.tv50k.setOnClickListener {
            if (status50k) {
                binding.tv50k.setBackgroundResource(R.drawable.shapes_line_white)
                binding.tv50k.setTextColor(resources.getColor(R.color.color_white_gray))
                status50k = false
                topuUp -= 50000
            } else {
                binding.tv50k.setBackgroundResource(R.drawable.shapes_line_pink)
                binding.tv50k.setTextColor(resources.getColor(R.color.color_pink))
                status50k = true
                topuUp += 50000
            }
        }

        binding.tv100k.setOnClickListener {
            if (status100k) {
                binding.tv100k.setBackgroundResource(R.drawable.shapes_line_white)
                binding.tv100k.setTextColor(resources.getColor(R.color.color_white_gray))
                status100k = false
                topuUp -= 100000
            } else {
                binding.tv100k.setBackgroundResource(R.drawable.shapes_line_pink)
                binding.tv100k.setTextColor(resources.getColor(R.color.color_pink))
                status100k = true
                topuUp += 100000
            }
        }

        binding.tv200k.setOnClickListener {
            if (status200k) {
                binding.tv200k.setBackgroundResource(R.drawable.shapes_line_white)
                binding.tv200k.setTextColor(resources.getColor(R.color.color_white_gray))
                status200k = false
                topuUp -= 200000
            } else {
                binding.tv200k.setBackgroundResource(R.drawable.shapes_line_pink)
                binding.tv200k.setTextColor(resources.getColor(R.color.color_pink))
                status10k = true
                topuUp += 200000
            }
        }

        binding.tv500k.setOnClickListener {
            if (status500k) {
                binding.tv500k.setBackgroundResource(R.drawable.shapes_line_white)
                binding.tv500k.setTextColor(resources.getColor(R.color.color_white_gray))
                status500k = false
                topuUp -= 500000
            } else {
                binding.tv500k.setBackgroundResource(R.drawable.shapes_line_pink)
                binding.tv500k.setTextColor(resources.getColor(R.color.color_pink))
                status10k = true
                topuUp += 500000
            }
        }
    }

}