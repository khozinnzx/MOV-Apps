package com.example.mov_apps.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mov_apps.R
import com.example.mov_apps.databinding.FragmentPilihBangkuBinding
import com.example.mov_apps.model.Checkout


class PilihBangkuFragment : Fragment(R.layout.fragment_pilih_bangku) {
    private lateinit var binding: FragmentPilihBangkuBinding
    val args: PilihBangkuFragmentArgs by navArgs()

    var statusA1: Boolean = false
    var statusA2: Boolean = false
    var statusA3: Boolean = false
    var statusA4: Boolean = false
    var statusB1: Boolean = false
    var statusB2: Boolean = false
    var statusB3: Boolean = false
    var statusB4: Boolean = false
    var statusC1: Boolean = false
    var statusC2: Boolean = false
    var statusC3: Boolean = false
    var statusC4: Boolean = false
    var statusD1: Boolean = false
    var statusD2: Boolean = false
    var statusD3: Boolean = false
    var statusD4: Boolean = false

    var total: Int = 0
    private var dataList = ArrayList<Checkout>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPilihBangkuBinding.bind(view)
        dataList.clear()

        val movie = args.resultMovie
        binding.tvTitle.text = movie.title

        checkKursi()


        binding.btnBeliTiket.setOnClickListener {
            findNavController().navigate(PilihBangkuFragmentDirections.actionPilihBangkuFragmentToCheckoutFragment(movie,dataList.toTypedArray()))
        }

    }

    private fun beliTiket(total: Int) {
        if (total == 0) {
            binding.btnBeliTiket.setText("Beli Tiket")
            binding.btnBeliTiket.visibility = View.INVISIBLE
        } else {
            binding.btnBeliTiket.setText("Beli Tiket ($total)")
            binding.btnBeliTiket.visibility = View.VISIBLE
        }
    }

    private fun checkKursi() {
        binding.a1.setOnClickListener {
            if (statusA1) {
                binding.a1.setImageResource(R.drawable.ic_rectangle_empty)
                statusA1 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("a1", 25000))
                }

            } else {
                binding.a1.setImageResource(R.drawable.ic_rectangle_selected)
                statusA1 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("a1", 25000)
                dataList.add(dataCheckout)

            }
        }

        binding.a2.setOnClickListener {
            if (statusA2) {
                binding.a2.setImageResource(R.drawable.ic_rectangle_empty)
                statusA2 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("a2", 25000))
                }
            } else {
                binding.a2.setImageResource(R.drawable.ic_rectangle_selected)
                statusA2 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("a2", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.a3.setOnClickListener {
            if (statusA3) {
                binding.a3.setImageResource(R.drawable.ic_rectangle_empty)
                statusA3 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("a3", 25000))
                }
            } else {
                binding.a3.setImageResource(R.drawable.ic_rectangle_selected)
                statusA3 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("a3", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.a4.setOnClickListener {
            if (statusA4) {
                binding.a4.setImageResource(R.drawable.ic_rectangle_empty)
                statusA4 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("a4", 25000))
                }
            } else {
                binding.a4.setImageResource(R.drawable.ic_rectangle_selected)
                statusA4 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("a4", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.b1.setOnClickListener {
            if (statusB1) {
                binding.b1.setImageResource(R.drawable.ic_rectangle_empty)
                statusB1 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("b1", 25000))
                }
            } else {
                binding.b1.setImageResource(R.drawable.ic_rectangle_selected)
                statusB1 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("b1", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.b2.setOnClickListener {
            if (statusB2) {
                binding.b2.setImageResource(R.drawable.ic_rectangle_empty)
                statusB2 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("b2", 25000))
                }
            } else {
                binding.b2.setImageResource(R.drawable.ic_rectangle_selected)
                statusB2 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("b2", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.b3.setOnClickListener {
            if (statusB3) {
                binding.b3.setImageResource(R.drawable.ic_rectangle_empty)
                statusB3 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("b3", 25000))
                }
            } else {
                binding.b3.setImageResource(R.drawable.ic_rectangle_selected)
                statusB3 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("b3", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.b4.setOnClickListener {
            if (statusB4) {
                binding.b4.setImageResource(R.drawable.ic_rectangle_empty)
                statusB4 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("b4", 25000))
                }
            } else {
                binding.b4.setImageResource(R.drawable.ic_rectangle_selected)
                statusB4 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("b4", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.c1.setOnClickListener {
            if (statusC1) {
                binding.c1.setImageResource(R.drawable.ic_rectangle_empty)
                statusC1 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("c1", 25000))
                }
            } else {
                binding.c1.setImageResource(R.drawable.ic_rectangle_selected)
                statusC1 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("c1", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.c2.setOnClickListener {
            if (statusC2) {
                binding.c2.setImageResource(R.drawable.ic_rectangle_empty)
                statusC2 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("c2", 25000))
                }
            } else {
                binding.c2.setImageResource(R.drawable.ic_rectangle_selected)
                statusC2 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("c2", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.c3.setOnClickListener {
            if (statusC3) {
                binding.c3.setImageResource(R.drawable.ic_rectangle_empty)
                statusC3 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("c3", 25000))
                }
            } else {
                binding.c3.setImageResource(R.drawable.ic_rectangle_selected)
                statusC3 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("c3", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.c4.setOnClickListener {
            if (statusC4) {
                binding.c4.setImageResource(R.drawable.ic_rectangle_empty)
                statusC4 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("c4", 25000))
                }
            } else {
                binding.c4.setImageResource(R.drawable.ic_rectangle_selected)
                statusC4 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("c4", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.d1.setOnClickListener {
            if (statusD1) {
                binding.d1.setImageResource(R.drawable.ic_rectangle_empty)
                statusD1 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("d1", 25000))
                }
            } else {
                binding.d1.setImageResource(R.drawable.ic_rectangle_selected)
                statusD1 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("d1", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.d2.setOnClickListener {
            if (statusD2) {
                binding.d2.setImageResource(R.drawable.ic_rectangle_empty)
                statusD2 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("d2", 25000))
                }
            } else {
                binding.d2.setImageResource(R.drawable.ic_rectangle_selected)
                statusD2 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("d2", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.d3.setOnClickListener {
            if (statusD3) {
                binding.d3.setImageResource(R.drawable.ic_rectangle_empty)
                statusD3 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("d3", 25000))
                }
            } else {
                binding.d3.setImageResource(R.drawable.ic_rectangle_selected)
                statusD3 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("d3", 25000)
                dataList.add(dataCheckout)
            }
        }

        binding.d4.setOnClickListener {
            if (statusD4) {
                binding.d4.setImageResource(R.drawable.ic_rectangle_empty)
                statusD4 = false
                total -= 1
                beliTiket(total)
                dataList.let {
                    dataList.remove(Checkout("d4", 25000))
                }
            } else {
                binding.d4.setImageResource(R.drawable.ic_rectangle_selected)
                statusD4 = true
                total += 1
                beliTiket(total)

                val dataCheckout = Checkout("d4", 25000)
                dataList.add(dataCheckout)
            }
        }
    }
}