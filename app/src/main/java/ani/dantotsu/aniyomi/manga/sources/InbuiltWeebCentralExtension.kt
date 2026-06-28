package ani.dantotsu.aniyomi.manga.sources

import android.content.Context
import android.content.SharedPreferences
import eu.kanade.tachiyomi.source.MangaSource
import eu.kanade.tachiyomi.extension.manga.model.MangaExtension
import eu.kanade.tachiyomi.extension.manga.model.AvailableMangaSources
import tachiyomi.domain.source.manga.model.MangaSourceData

/**
 * Provides Weeb Central as a built-in manga source without requiring external extension installation.
 * This class mimics the behavior of an installed extension but is embedded in the app.
 */
class InbuiltWeebCentralExtension(private val context: Context) {

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences("weebcentral_inbuilt", Context.MODE_PRIVATE)
    }

    /**
     * Creates the Weeb Central source instance with shared preferences.
     */
    fun createSource(): MangaSource {
        return WeebCentral(preferences)
    }

    /**
     * Creates a MangaExtension.Installed object to register with the extension manager.
     */
    fun createInstalledExtension(): MangaExtension.Installed {
        val source = createSource()
        return MangaExtension.Installed(
            name = "Weeb Central (Built-in)",
            pkgName = "ani.dantotsu.inbuilt.weebcentral",
            versionName = "1.0.0",
            versionCode = 1L,
            libVersion = 15.0,
            lang = "en",
            isNsfw = false,
            hasReadme = false,
            hasChangelog = false,
            pkgFactory = null,
            sources = listOf(source),
            icon = null,
            hasUpdate = false,
            isObsolete = false,
            isUnofficial = false,
        )
    }

    /**
     * Creates an AvailableMangaSources entry for the Weeb Central source.
     */
    fun createAvailableSource(): AvailableMangaSources {
        val source = createSource()
        return AvailableMangaSources(
            id = source.id,
            lang = "en",
            name = "Weeb Central",
            baseUrl = "https://weebcentral.com",
        )
    }

    /**
     * Converts the source to MangaSourceData for stub registration.
     */
    fun toSourceData(): MangaSourceData {
        val source = createSource()
        return MangaSourceData(
            id = source.id,
            lang = "en",
            name = "Weeb Central",
        )
    }
}
