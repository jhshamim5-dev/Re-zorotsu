package ani.dantotsu.aniyomi.anime.sources

import android.content.Context
import android.content.SharedPreferences
import eu.kanade.tachiyomi.animesource.AnimeSource
import eu.kanade.tachiyomi.extension.anime.model.AnimeExtension
import eu.kanade.tachiyomi.extension.anime.model.AvailableAnimeSources
import tachiyomi.domain.source.anime.model.AnimeSourceData

/**
 * Provides Miruro as a built-in anime source without requiring external extension installation.
 * This class mimics the behavior of an installed extension but is embedded in the app.
 */
class InbuiltMiruroExtension(private val context: Context) {

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences("miruro_inbuilt", Context.MODE_PRIVATE)
    }

    /**
     * Creates the Miruro source instance with shared preferences.
     */
    fun createSource(): AnimeSource {
        return Miruro(preferences)
    }

    /**
     * Creates an AnimeExtension.Installed object to register with the extension manager.
     */
    fun createInstalledExtension(): AnimeExtension.Installed {
        val miruro = createSource()
        return AnimeExtension.Installed(
            name = "Miruro.tv (Built-in)",
            pkgName = "ani.dantotsu.inbuilt.miruro",
            versionName = "1.0.0",
            versionCode = 1L,
            libVersion = 15.0,
            lang = "en",
            isNsfw = false,
            hasReadme = false,
            hasChangelog = false,
            pkgFactory = null,
            sources = listOf(miruro),
            icon = null,
            hasUpdate = false,
            isObsolete = false,
            isUnofficial = false,
        )
    }

    /**
     * Creates an AvailableAnimeSources entry for the Miruro source.
     */
    fun createAvailableSource(): AvailableAnimeSources {
        val miruro = createSource()
        return AvailableAnimeSources(
            id = miruro.id,
            lang = "en",
            name = "Miruro.tv",
            baseUrl = "https://www.miruro.tv",
        )
    }

    /**
     * Converts the source to AnimeSourceData for stub registration.
     */
    fun toSourceData(): AnimeSourceData {
        val miruro = createSource()
        return AnimeSourceData(
            id = miruro.id,
            lang = "en",
            name = "Miruro.tv",
        )
    }
}
