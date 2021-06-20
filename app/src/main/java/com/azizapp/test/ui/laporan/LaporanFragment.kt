package com.azizapp.test.ui.laporan

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.azizapp.test.R
import com.azizapp.test.databinding.FragmentLaporanBinding
import com.azizapp.test.utill.Session
import com.azizapp.test.utill.snackbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.fragment_laporan.*
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class LaporanFragment @Inject constructor(private val typeUser: String) : Fragment() {

    var address = ""
    var city = ""
    var lat: Double = 0.0
    var long: Double = 0.0

    //    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
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
        binding.tvNamaJalan.setOnClickListener {
            val intent = Intent(activity, LaporanActivity::class.java)
            startActivityForResult(intent, 100)
        }
        laporanViewModel.action.observe(this.viewLifecycleOwner, { action ->
            when (action) {
                LaporanViewModel.ACTION_SUCCESS -> actionSuccess()
                LaporanViewModel.ACTION_ERROR -> actionError()
                LaporanViewModel.ACTION_FAILED -> actionFailed()
            }

        })
        binding.tvPilihDariGaleri.setOnClickListener {
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                startActivityForResult(it, 1)
            }
        }
        binding.buttonLapor.setOnClickListener {
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

    private fun actionSuccess() {
        val intent = Intent(activity, SuccessPage::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val lat = data.getDoubleExtra("LAT", 0.0)
            val long = data.getDoubleExtra("LONG", 0.0)
            tv_nama_jalan.text = data.getStringExtra("ADDRESS")
            tv_koordinat.text = StringBuilder("[$lat,$long]")
        } else if (requestCode == 1 && data != null) {
            imageUri = data.data
            binding.editGambar.setImageURI(imageUri)
            binding.tvPilihDariGaleri.visibility = View.GONE
            binding.imageView2.visibility = View.GONE
            binding.editGambar.visibility = View.VISIBLE
            val bitmap: Bitmap? = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
            } else {
                val source =
                    ImageDecoder.createSource(requireActivity().contentResolver, imageUri!!)
                ImageDecoder.decodeBitmap(source)
            }

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

            val bytes = byteArrayOutputStream.toByteArray()
            sImage = Base64.encodeToString(bytes, Base64.DEFAULT)
        }
    }

    fun pilihLaporan(): String {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        val dialog = BottomSheetDialog(requireActivity())
        var jenis = ""
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
        val bearer = "Bearer " + Session.bearer

        if (imageUri == null) {
            this.view?.snackbar("Select an Image First")
            return
        }

        val geometry = "{\"type\": \"Point\", \"coordinates\": ${tv_koordinat.text}}"
        val namaJalan = tv_nama_jalan.text.toString()
        val image = sImage
        val tipePengaduan = tv_laporkan.text.toString().substring(15)
        val deskripsi = editTextDeskripsi.text.toString()
        val statusPengaduan = "Belum diverifikasi"

        laporanViewModel.uploadLaporan(
            bearer,
            namaJalan,
            image,
            deskripsi,
            tipePengaduan,
            geometry,
            statusPengaduan
        )
        laporanViewModel.loadingEnable.observe(viewLifecycleOwner, {
            if (it) {
                binding.pbLoginLoading.visibility = View.VISIBLE
                binding.buttonLapor.visibility = View.GONE
            } else {
                binding.pbLoginLoading.visibility = View.GONE
                binding.buttonLapor.visibility = View.VISIBLE
            }
        })
        laporanViewModel.action.observe(viewLifecycleOwner, {
            when (it) {
                LaporanViewModel.ACTION_SUCCESS -> {
                    val intent = Intent(activity, SuccessPage::class.java)
                    intent.putExtra("type", "login")
                    startActivity(intent)
                }
                LaporanViewModel.ACTION_ERROR -> actionError()
                LaporanViewModel.ACTION_FAILED -> actionFailed()
            }
        })

    }

    private fun uploadImageAnonym() {
        if (imageUri == null) {
            this.view?.snackbar("Select an Image First")
            return
        }

        val geometry = "{\"type\": \"Point\", \"coordinates\": ${binding.tvKoordinat.text}}"
        val namaJalan = binding.tvNamaJalan.text.toString()
        val image = sImage
        val tipePengaduan = tv_laporkan.text.toString().substring(15)
        val deskripsi = editTextDeskripsi.text.toString()
        val statusPengaduan = "Belum diverifikasi"

        laporanViewModel.uploadLaporanAnonymous(
            namaJalan,
            image,
            deskripsi,
            tipePengaduan,
            geometry,
            statusPengaduan
        )
        laporanViewModel.loadingEnable.observe(viewLifecycleOwner, {
            if (it) {
                binding.pbLoginLoading.visibility = View.GONE
                binding.buttonLapor.visibility = View.VISIBLE
            } else {
                binding.pbLoginLoading.visibility = View.VISIBLE
                binding.buttonLapor.visibility = View.GONE
            }
        })
        laporanViewModel.action.observe(viewLifecycleOwner, {
            when (it) {
                LaporanViewModel.ACTION_SUCCESS -> {
                    val intent = Intent(activity, SuccessPage::class.java)
                    intent.putExtra("type", "anonim")
                    startActivity(intent)
                }
                LaporanViewModel.ACTION_ERROR -> actionError()
                LaporanViewModel.ACTION_FAILED -> actionFailed()
            }
        })
    }

    private fun actionFailed() {
        this.view?.snackbar("Upload Failed")
    }

    private fun actionError() {
        this.view?.snackbar("Upload Error")
    }
}


