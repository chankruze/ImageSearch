package `in`.geekofia.imagesearch.ui.details

import `in`.geekofia.imagesearch.R
import `in`.geekofia.imagesearch.data.UnsplashPhoto
import `in`.geekofia.imagesearch.databinding.BottomSheetDialogDownloadBinding
import `in`.geekofia.imagesearch.utils.Utils
import android.Manifest
import android.annotation.TargetApi
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File


class DownloadBottomSheetDialogFragment(
    photo: UnsplashPhoto
) : BottomSheetDialogFragment() {

    private val urls = photo.urls
    private val id = photo.id

    companion object {
        const val TAG = "DownloadBottomSheetDialogFragment"
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_dialog_download, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = BottomSheetDialogDownloadBinding.bind(view)

        binding.apply {
            layoutRaw.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    askPermissions(id, urls.small)
                } else {
                    downloadImage(id, urls.raw)
                }
            }

            layoutFull.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    askPermissions(id, urls.full)
                } else {
                    downloadImage(id, urls.full)
                }
            }

            layoutRegular.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    askPermissions(id, urls.regular)
                } else {
                    downloadImage(id, urls.regular)
                }
            }

            layoutSmall.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    askPermissions(id, urls.small)
                } else {
                    downloadImage(id, urls.small)
                }
            }

        }
    }

    private fun downloadImage(id: String, url: String) {
        // the image url
        val downloadUri = Uri.parse(url)
        // image file name to be saved
        val fileName = id.plus(getImageExtension(url))
//        val fileName = getImageFileName(url)
        // download manager instance
        val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        // download directory
        val directory = File(Environment.DIRECTORY_PICTURES, "Unsplash")
        // if directory don't exist, create it
        if (!directory.exists()) {
            directory.mkdirs()
        }
        // download request
        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(fileName)
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    fileName
                )
        }

        // download status message
        var msg: String?
        var lastMsg = ""
        // download id
        val downloadId = downloadManager.enqueue(request)
        // query
        val query = DownloadManager.Query().setFilterById(downloadId)
        // thread
        Thread {
            // init downloading flag as true
            var downloading = true
            // loop
            while (downloading) {
                // cursor
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                // check if download is finished or not
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    == DownloadManager.STATUS_SUCCESSFUL
                ) {
                    downloading = false
                }

                // download status
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                // update message
                msg = Utils.statusMessage(fileName, directory, status)
                // if status changed show toast
                if (msg != lastMsg) {
                    activity?.runOnUiThread {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }.start()
    }

    // helper function to get image file name
//    private fun getImageFileName(url: String) = url.split("/")
//        .filter { it.contains("photo-") }[0].split("?")[0] + "." + url.split("&").filter {
//        it.contains(
//            "fm="
//        )
//    }[0].split("=")[1]

    // helper function to get image file extension
    private fun getImageExtension(url: String) =
        ".${url.split("&").filter { it.contains("fm=") }[0].split("=")[1]}"


    // Permission
    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions(id: String, url: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle("Permission required")
                        .setMessage("Permission required to save photos from the Web.")
                        .setPositiveButton("Accept") { _, _ ->
                            ActivityCompat.requestPermissions(
                                requireActivity(),
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                            )
                            activity?.finish()
                        }
                        .setNegativeButton("Deny") { dialog, _ -> dialog.cancel() }
                        .show()
                }
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            downloadImage(id, url)
        }
    }
}


