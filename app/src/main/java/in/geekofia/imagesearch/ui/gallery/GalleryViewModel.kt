package `in`.geekofia.imagesearch.ui.gallery

import `in`.geekofia.imagesearch.data.UnsplashRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {
}