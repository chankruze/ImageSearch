package `in`.geekofia.imagesearch.ui.gallery

import `in`.geekofia.imagesearch.data.UnsplashRepository
import androidx.lifecycle.*
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: UnsplashRepository,
    state: SavedStateHandle
) : ViewModel() {
    // current query
    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    // get photos from repository and update livedata in view model scope
    // to avoid crash on orientation changes
    val photos = currentQuery.switchMap { repository.getSearchResults(it).cachedIn(viewModelScope) }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    // default query
    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "dogs"
    }
}