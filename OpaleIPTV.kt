package com.opaletv

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class OpaleIPTV : MainAPI() {
    override var mainUrl = "http://nodns1.top:8080"
    override var name = "Opale IPTV"
    override val hasMainPage = true
    override val supportedTypes = setOf(TvType.Live)

    private val m3uUrl = "$mainUrl/get.php?username=1131718110212168&password=7ceea78cf031&type=m3u_plus"

    override suspend fun getMainPage(): HomePageResponse {
        val channels = AppUtils.parseM3u(m3uUrl).map {
            LiveSearchResponse(
                name = it.name ?: "Unknown",
                url = it.url ?: "",
                apiName = this.name,
                type = TvType.Live,
                posterUrl = it.logo,
                quality = null
            )
        }
        return newHomePageResponse("Live", channels)
    }

    override suspend fun load(url: String): LoadResponse {
        return newMovieLoadResponse("Live Stream", url, TvType.Live) {
            this.streamLinks = listOf(ExtractorLink(this.name, this.name, url, this.mainUrl))
        }
    }
}
