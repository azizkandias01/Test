package com.azizapp.test.ui.riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizapp.test.databinding.FragmentRiwayatBinding
import com.azizapp.test.model.Pengaduan
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_riwayat.*
import kotlinx.android.synthetic.main.item_list_riwayat.view.*
import kotlinx.android.synthetic.main.splashscreen.*


@AndroidEntryPoint
class RiwayatFragment : Fragment() {

    private val riwayatViewModel: RiwayatViewModel by viewModels()
    private lateinit var recyclerAdapter: RiwayatAdapter
    private lateinit var dataBinding: FragmentRiwayatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentRiwayatBinding.inflate(inflater, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        riwayatViewModel.onLoad()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerAdapter = RiwayatAdapter()
        riwayatViewModel.pengaduan.observe(viewLifecycleOwner, { data ->
            dataBinding.chipGroupFilter.setOnCheckedChangeListener { group, checkedId ->
                val chip: Chip? = group.findViewById(checkedId)
                chip?.let { chipView ->
                    val filter = data.filter {
                        it.statusPengaduan == chipView.text
                    }
                    if (chipView.text == "Semua Laporan") recyclerAdapter.setData(data) else recyclerAdapter.setData(
                        filter as ArrayList<Pengaduan>
                    )
                }
            }
            recyclerAdapter.setData(data)
            dataBinding.recyclerView.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = this@RiwayatFragment.recyclerAdapter
            }
        })
        riwayatViewModel.loadingEnable.observe(viewLifecycleOwner, {
            dataBinding.pbLoadRiwayat.visibility = if (it) View.VISIBLE else View.GONE
        })
    }
}