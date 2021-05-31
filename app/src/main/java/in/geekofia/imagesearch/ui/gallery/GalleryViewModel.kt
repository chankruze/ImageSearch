package `in`.geekofia.imagesearch.ui.gallery

import `in`.geekofia.imagesearch.data.UnsplashRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {
    // current query
    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    // get photos from repository and update livedata in view model scope
    // to avoid crash on orientation changes
    val photos = currentQuery.switchMap { repository.getSearchResults(it).cachedIn(viewModelScope) }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    // default query
    companion object {
        private const val DEFAULT_QUERY = "dogs"
    }
}