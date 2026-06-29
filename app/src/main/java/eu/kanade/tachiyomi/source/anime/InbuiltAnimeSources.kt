package eu.kanade.tachiyomi.source.anime

import android.content.Context
import ani.dantotsu.aniyomi.anime.sources.Miruro
import ani.dantotsu.aniyomi.anime.sources.anikoto.Anikoto
import eu.kanade.tachiyomi.animesource.AnimeSource

object InbuiltAnimeSources {

    fun createSources(context: Context): List<AnimeSource> {
        val miruroPrefs = context.getSharedPreferences("miruro_inbuilt", Context.MODE_PRIVATE)
        val anikotoPrefs = context.getSharedPreferences("anikoto_inbuilt", Context.MODE_PRIVATE)
        return listOf(Miruro(miruroPrefs), Anikoto(anikotoPrefs))
    }
}
