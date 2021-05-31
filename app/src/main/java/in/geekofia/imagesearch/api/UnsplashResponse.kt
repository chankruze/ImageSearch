package `in`.geekofia.imagesearch.api

import `in`.geekofia.imagesearch.data.UnsplashPhoto

data class UnsplashResponse(
    val results: List<UnsplashPhoto>,
)