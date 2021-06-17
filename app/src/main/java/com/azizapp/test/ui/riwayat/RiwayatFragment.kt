package com.azizapp.test.ui.riwayat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizapp.test.R
import com.azizapp.test.databinding.FragmentRiwayatBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RiwayatFragment : Fragment() {

    private val riwayatViewModel: RiwayatViewModel by viewModels()
    private lateinit var riwayatAdapter: RiwayatRecyclerAdapter
    private lateinit var dataBinding: FragmentRiwayatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        riwayatAdapter = RiwayatRecyclerAdapter(riwayatViewModel)
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_riwayat, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        dataBinding.apply {
            lifecycleOwner = this@RiwayatFragment
            viewModelRiwayat = riwayatViewModel
        }

        riwayatViewModel.action.observe(viewLifecycleOwner, Observer { action ->
            when (action) {
                RiwayatViewModel.ACTION_RIWAYAT_FETCHED -> listItemUpdate()
                RiwayatViewModel.ACTION_RIWAYAT_ONCLICK -> listItemOnClick()
            }
        })

        riwayatViewModel.onLoad()
    }

    private fun listItemOnClick() {
        val itemClicked =
            riwayatViewModel.listPengaduan[riwayatViewModel.actionItemPosition.value ?: 0]

        val intent = Intent(requireContext(), DetilRiwayat::class.java)
        intent.putExtra(DetilRiwayat.DETAIL_EXTRA_PARCEL, itemClicked)

        startActivity(intent)
    }

    private fun setupRecyclerView() {
        dataBinding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@RiwayatFragment.riwayatAdapter
        }
    }

    private fun listItemUpdate() {
        riwayatAdapter.items = riwayatViewModel.listPengaduan
        riwayatAdapter.notifyDataSetChanged()
    }

}