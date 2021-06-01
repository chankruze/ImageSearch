package `in`.geekofia.imagesearch.utils

import android.app.DownloadManager
import java.io.File

class Utils {
    companion object {
        fun statusMessage(url: String, directory: File, status: Int): String? {
            return when (status) {
                DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
                DownloadManager.STATUS_PAUSED -> "Paused"
                DownloadManager.STATUS_PENDING -> "Pending"
                DownloadManager.STATUS_RUNNING -> "Downloading..."
                DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                    url.lastIndexOf("/") + 1
                )
                else -> "There's nothing to download"
            }
        }
    }
}