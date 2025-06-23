package com.opaletv

import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadM3u
import com.lagradost.cloudstream3.utils.AppUtils

class OpaleIPTV : MainAPI() {
    override var mainUrl = "http://nodns1.top:8080"
    override var name = "Opale IPTV"
    override val hasMainPage = true
    override val supportedTypes = setOf(TvType.Live, TvType.TvSeries, TvType.Movie)

    private val fullM3uUrl = "$mainUrl/get.php?username=1131718110212168&password=7ceea78cf031&type=m3u_plus"

    override suspend fun getMainPage(): HomePageResponse {
        val channels = AppUtils.parseM3u(fullM3uUrl).map {
            LiveSearchResponse(
                name = it.name ?: "Unknown",
                url = it.url ?: "",
                apiName = this.name,
                type = TvType.Live,
                posterUrl = it.logo,
                quality = null
            )
        }
        return newHomePageResponse("Live Channels", channels)
    }

    override suspend fun load(url: String): LoadResponse {
        return newMovieLoadResponse("Live Stream", url, TvType.Live) {
            this.streamLinks = listOf(ExtractorLink(this.name, this.name, url, this.mainUrl))
        }
    }
}