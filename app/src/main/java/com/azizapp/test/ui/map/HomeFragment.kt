package com.azizapp.test.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.azizapp.test.R
import com.azizapp.test.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_persistent_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_persistent_bottom_sheet.view.*

@AndroidEntryPoint
class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var binding : FragmentHomeBinding
    private val HomeViewModel: HomeFragmentViewModel by viewModels()
    private var markerList: ArrayList<Marker>? = null
    private var markerListTersumbat: ArrayList<Marker>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView2.onCreate(savedInstanceState)
        mapView2.onResume()

        mapView2.getMapAsync(this)
        HomeViewModel.onLoad()
        HomeViewModel.titikTersumbat()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val i = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        bottomSheetBehavior = BottomSheetBehavior.from(i.bottomsheet)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        return binding.root
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
        }

        googleMap.setOnMapLoadedCallback {
            HomeViewModel.listTitikBanjir.forEach { titik ->
                val mark = googleMap.addMarker(
                    MarkerOptions()
                        .position(geoToLatLong(titik.geometry))
                        .title(titik.namaJalan)
                        .snippet("${titik.keterangan}|${titik.foto}")
                )
                markerList?.add(mark)
            }
            HomeViewModel.listTitikTersumbat.forEach { titik ->
                val mark = googleMap.addMarker(
                    MarkerOptions()
                        .position(geoToLatLong(titik.geometry))
                        .title(titik.namaJalan)
                        .snippet("${titik.keterangan}|${titik.foto}")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                )
                markerListTersumbat?.add(mark)
            }
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(0.510440, 101.438309)))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f))

        googleMap.setOnMarkerClickListener { marker ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            tvTitle.text = marker.title
            val keteranganFoto: List<String> = marker.snippet.split("|")
            tvSubtitle.text = keteranganFoto[0]
            Glide.with(this)
                .load("https://gis-drainase.pocari.id/storage/app/public/images/${keteranganFoto[1]}")
                .into(gambar)
            true
        }
        HomeViewModel.loadingEnable.observe(viewLifecycleOwner,{
            if (it){
                binding.pbLoadTitik.visibility = View.VISIBLE
            }else{
                binding.pbLoadTitik.visibility = View.GONE
            }
        })
    }

    fun geoToLatLong(string: String): LatLng {
        val substring = string.substring(34, string.length - 2)
        val latlong: List<String> = substring.split(",")
        val latLng = LatLng(latlong[1].toDouble(), latlong[0].toDouble())
        return latLng
    }
}



