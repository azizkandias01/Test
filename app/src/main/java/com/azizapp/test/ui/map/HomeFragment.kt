package com.azizapp.test.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.azizapp.test.R
import com.azizapp.test.binding.loadImgFromUrl
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_detail.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


@AndroidEntryPoint
class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var i: View
    private val HomeViewModel: HomeFragmentViewModel by viewModels()
    private var markerList: ArrayList<Marker>? = null
    var markerListTersumbat: ArrayList<Marker>? = null

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
        i = inflater.inflate(R.layout.fragment_home, container, false)
        // Inflate the layout for this fragment
        bottomSheetBehavior = BottomSheetBehavior.from(i.bottomsheet)
            .apply { BottomSheetDialog(requireContext(), R.style.BottomSheetDialog) }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        return i
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
                markerList?.add(mark!!)
            }
            HomeViewModel.listTitikTersumbat.forEach { titik ->
                val mark = googleMap.addMarker(
                    MarkerOptions()
                        .position(geoToLatLong(titik.geometry))
                        .title(titik.namaJalan)
                        .snippet("${titik.keterangan}|${titik.foto}")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                )
                markerListTersumbat?.add(mark!!)
            }
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(0.510440, 101.438309)))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f))

        googleMap.setOnMarkerClickListener { marker ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            i.tvTitle.text = marker.title
            val keteranganFoto: List<String> = marker.snippet!!.split("|")
            i.tvSubtitle.text = StringBuilder("Keterangan Drainase: ${keteranganFoto[0]}")
            i.gambar.loadImgFromUrl(keteranganFoto[1])
            true
        }
        HomeViewModel.loadingEnable.observe(viewLifecycleOwner, {
            i.pb_load_titik.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    fun geoToLatLong(string: String): LatLng {
        val substring = string.substring(34, string.length - 2)
        val latlong: List<String> = substring.split(",")
        return LatLng(latlong[1].toDouble(), latlong[0].toDouble())
    }
}



