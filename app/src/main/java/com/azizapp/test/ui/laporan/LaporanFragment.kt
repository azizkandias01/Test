package com.azizapp.test.ui.laporan

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.system.Os.accept
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.azizapp.test.R
import com.azizapp.test.api.MyAPI
import com.azizapp.test.databinding.FragmentLaporanBinding
import com.azizapp.test.model.DataPengaduanMasyarakat
import com.azizapp.test.utill.Session
import com.azizapp.test.utill.snackbar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.fragment_laporan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.lang.StringBuilder
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LaporanFragment @Inject constructor(private val typeUser: String) : Fragment() {

    var address = "";
    var city = "";
    var lat: Double = 0.0
    var long: Double = 0.0
    lateinit var binding: FragmentLaporanBinding
    private val laporanViewModel: LaporanViewModel by viewModels()

    var imageUri: Uri? = null
    var sImage: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pilihLaporan()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_laporan, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModelLaporan = laporanViewModel
        }
        binding.editTextNamaJalan.setOnClickListener {
            val intent = Intent(activity, LaporanActivity::class.java)
            startActivityForResult(intent, 100)
        }
        laporanViewModel.action.observe(this.viewLifecycleOwner, Observer { action ->
            when (action) {
                LaporanViewModel.ACTION_SUCCESS -> actionSuccess()
                LaporanViewModel.ACTION_ERROR -> actionError()
                LaporanViewModel.ACTION_FAILED -> actionFailed()
            }

        })
        binding.editGambar.setOnClickListener {
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                startActivityForResult(it, 1)
            }
        }
        binding.buttonLapor.setOnClickListener() {
            when (typeUser) {
                "login" -> uploadImage()
                "anonim" -> uploadImageAnonym()
            }
        }
    }

    private fun openDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.pilih_jenis_laporan))
            .setMessage(resources.getString(R.string.dialog_content))
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
                pilihLaporan()
            }
            .show()
    }

    private fun actionFailed() {
        Snackbar.make(binding.root, "Action Failed", Snackbar.LENGTH_SHORT).show()
    }

    private fun actionError() {
        Snackbar.make(binding.root, "Action Error", Snackbar.LENGTH_SHORT).show()
    }

    private fun actionSuccess() {
        val intent = Intent(activity, SuccessPage::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val lat = data.getDoubleExtra("LAT", 0.0)
            val long = data.getDoubleExtra("LONG", 0.0)
            editTextNamaJalan.setText(data.getStringExtra("ADDRESS"))
            editTextLokasi.setText(StringBuilder("[$lat,$long]"))
        } else if (requestCode == 1 && data != null) {
            imageUri = data.data
            editGambar.setImageURI(imageUri)
            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

            val bytes = byteArrayOutputStream.toByteArray()
            sImage = Base64.encodeToString(bytes, Base64.DEFAULT)
        }
    }

    fun pilihLaporan(): String {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        val dialog = BottomSheetDialog(requireActivity())
        var jenis: String = ""
        dialog.setContentView(view)
        dialog.show()

        view.titik_tersumbat.setOnClickListener {
            jenis = getString(R.string.titik_tersumbat)
            Toast.makeText(
                activity,
                "Anda melaporkan $jenis", Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
            tv_laporkan.text = StringBuilder("Laporkan $jenis")
        }

        view.titik_banjir.setOnClickListener {
            jenis = getString(R.string.titik_banjir)
            Toast.makeText(
                activity,
                "Anda melaporkan $jenis", Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
            tv_laporkan.text = StringBuilder("Laporkan $jenis")
        }

        dialog.setOnDismissListener {
            if (jenis == "") {
                openDialog()
            }
        }
        return jenis
    }

    private fun uploadImage() {
        if (imageUri == null) {
            this.view?.snackbar("Select an Image First")
            return
        }

        val bearer = "Bearer " + Session.bearer

        val geometry = "{\"type\": \"Point\", \"coordinates\": ${editTextLokasi.text}}"
        val masyarakat = DataPengaduanMasyarakat(
            editTextNamaJalan.text.toString(),
            sImage,
            tv_laporkan.text.toString().substring(
                15
            ),
            editTextDeskripsi.text.toString(),
            "Belum diverifikasi",
            geometry
        )
        MyAPI().pengaduanMasyarakat(
            bearer,
            "application/json",
            masyarakat

        ).enqueue(object : Callback<DataPengaduanMasyarakat> {
            override fun onResponse(
                call: Call<DataPengaduanMasyarakat>,
                response: Response<DataPengaduanMasyarakat>
            ) {
                val intent = Intent(activity, SuccessPage::class.java)
                intent.putExtra("type", "login")
                startActivity(intent)
            }
            override fun onFailure(call: Call<DataPengaduanMasyarakat>, t: Throwable) {
                requireView().snackbar("gagal ${t.message}")
            }
        })
    }

    private fun uploadImageAnonym() {
        if (imageUri == null) {
            this.view?.snackbar("Select an Image First")
            return
        }
        val geometry = "{\"type\": \"Point\", \"coordinates\": ${editTextLokasi.text}}"
        val masyarakat = DataPengaduanMasyarakat(
            editTextNamaJalan.text.toString(),
            sImage,
            tv_laporkan.text.toString().substring(15),
            editTextDeskripsi.text.toString(),
            "Belum diverifikasi",
            geometry
        )
        MyAPI().pengaduanMasyarakatAnonim(
            "application/json",
            masyarakat

        ).enqueue(object : Callback<DataPengaduanMasyarakat> {
            override fun onResponse(
                call: Call<DataPengaduanMasyarakat>,
                response: Response<DataPengaduanMasyarakat>
            ) {
                val intent = Intent(activity, SuccessPage::class.java)
                intent.putExtra("type", "anonim")
                startActivity(intent)
            }

            override fun onFailure(call: Call<DataPengaduanMasyarakat>, t: Throwable) {
                requireView().snackbar("gagal ${t.message}")
            }
        })
    }
}


