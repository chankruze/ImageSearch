package `in`.geekofia.imagesearch.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// implement parcelable
@Parcelize
data class UnsplashPhoto(
    val id: String,
    val width: Int,
    val height: Int,
    val likes: Int,
    val description: String?,
    val urls: UnsplashPhotoUrls,
    val user: UnsplashUser
) : Parcelable {
    @Parcelize
    data class UnsplashPhotoUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String,
    ) : Parcelable

    @Parcelize
    data class UnsplashUser(
        val id: String,
        val username: String,
        val name: String,
    ) : Parcelable {
        val attributionUrl get() = "https://unsplash.com/$username?utm_source=ImageSearch&utm_medium=referral"
    }
}