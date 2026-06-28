package eu.kanade.tachiyomi.source.manga

import android.content.Context
import ani.dantotsu.aniyomi.manga.sources.WeebCentral
import eu.kanade.tachiyomi.source.MangaSource

object InbuiltMangaSources {

    fun createSources(context: Context): List<MangaSource> {
        val prefs = context.getSharedPreferences("weebcentral_inbuilt", Context.MODE_PRIVATE)
        return listOf(WeebCentral(prefs))
    }
}
