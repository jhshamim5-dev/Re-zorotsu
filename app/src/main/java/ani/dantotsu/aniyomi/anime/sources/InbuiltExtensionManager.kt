package ani.dantotsu.aniyomi.anime.sources

import android.content.Context
import eu.kanade.tachiyomi.extension.anime.AnimeExtensionManager
import eu.kanade.tachiyomi.extension.anime.model.AnimeExtension
import eu.kanade.tachiyomi.extension.anime.model.AnimeLoadResult
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge

/**
 * Wrapper around AnimeExtensionManager that adds built-in extensions (like Miruro)
 * to the installed extensions list, so users don't need to install them separately.
 */
class InbuiltExtensionManager(
    private val delegate: AnimeExtensionManager,
) : AnimeExtensionManager(delegate.context) {

    private val inbuiltExtensions = listOf(
        InbuiltMiruroExtension(delegate.context).createInstalledExtension()
    )

    override val installedExtensionsFlow = merge(
        flowOf(inbuiltExtensions),
        delegate.installedExtensionsFlow
    )

    override val availableExtensionsFlow = delegate.availableExtensionsFlow
    override val untrustedExtensionsFlow = delegate.untrustedExtensionsFlow
    override val isInitialized: Boolean get() = delegate.isInitialized

    override fun getAppIconForSource(sourceId: Long): android.graphics.drawable.Drawable? {
        return delegate.getAppIconForSource(sourceId)
    }

    override fun getSourceData(id: Long) = delegate.getSourceData(id)

    override suspend fun findAvailableExtensions() {
        delegate.findAvailableExtensions()
    }

    override fun installExtension(extension: AnimeExtension.Available) = delegate.installExtension(extension)

    override fun updateExtension(extension: AnimeExtension.Installed) = delegate.updateExtension(extension)

    override fun cancelInstallUpdateExtension(extension: AnimeExtension) = delegate.cancelInstallUpdateExtension(extension)

    override fun setInstalling(downloadId: Long) = delegate.setInstalling(downloadId)

    override fun updateInstallStep(downloadId: Long, step: eu.kanade.tachiyomi.extension.InstallStep) =
        delegate.updateInstallStep(downloadId, step)

    override fun uninstallExtension(pkgName: String) = delegate.uninstallExtension(pkgName)
}
