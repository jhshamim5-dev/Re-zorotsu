package eu.kanade.tachiyomi.source.manga

import android.content.Context
import ani.dantotsu.aniyomi.manga.sources.InbuiltWeebCentralExtension
import eu.kanade.tachiyomi.extension.manga.MangaExtensionManager
import eu.kanade.tachiyomi.source.CatalogueSource
import eu.kanade.tachiyomi.source.MangaSource
import eu.kanade.tachiyomi.source.online.HttpSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tachiyomi.domain.source.manga.model.MangaSourceData
import tachiyomi.domain.source.manga.model.StubMangaSource
import tachiyomi.domain.source.manga.service.MangaSourceManager
import tachiyomi.source.local.entries.manga.LocalMangaSource
import java.util.concurrent.ConcurrentHashMap

class AndroidMangaSourceManager(
    private val context: Context,
    private val extensionManager: MangaExtensionManager,
) : MangaSourceManager {

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    private val sourcesMapFlow = MutableStateFlow(ConcurrentHashMap<Long, MangaSource>())

    private val stubSourcesMap = ConcurrentHashMap<Long, StubMangaSource>()

    override val catalogueSources: Flow<List<CatalogueSource>> =
        sourcesMapFlow.map { it.values.filterIsInstance<CatalogueSource>() }

    init {
        // Add inbuilt sources immediately so they're always available
        val inbuiltExtension = InbuiltWeebCentralExtension(context).createInstalledExtension()
        inbuiltExtension.sources.forEach { source ->
            sourcesMapFlow.value[source.id] = source
            registerStubSource(source.toSourceData())
        }
        scope.launch {
            extensionManager.installedExtensionsFlow
                .collectLatest { extensions ->
                    val mutableMap = ConcurrentHashMap<Long, MangaSource>(
                        mapOf(
                            LocalMangaSource.ID to LocalMangaSource(
                                context,
                            ),
                        ),
                    )
                    // Re-add inbuilt sources (they should already be in the map)
                    inbuiltExtension.sources.forEach { source ->
                        mutableMap[source.id] = source
                    }
                    extensions.forEach { extension ->
                        extension.sources.forEach {
                            mutableMap[it.id] = it
                            registerStubSource(it.toSourceData())
                        }
                    }
                    sourcesMapFlow.value = mutableMap
                }
        }
    }

    override fun get(sourceKey: Long): MangaSource? {
        return sourcesMapFlow.value[sourceKey]
    }

    override fun getOrStub(sourceKey: Long): MangaSource {
        return sourcesMapFlow.value[sourceKey] ?: stubSourcesMap.getOrPut(sourceKey) {
            runBlocking { createStubSource(sourceKey) }
        }
    }

    override fun getOnlineSources() = sourcesMapFlow.value.values.filterIsInstance<HttpSource>()

    override fun getCatalogueSources() =
        sourcesMapFlow.value.values.filterIsInstance<CatalogueSource>()

    override fun getStubSources(): List<StubMangaSource> {
        val onlineSourceIds = getOnlineSources().map { it.id }
        return stubSourcesMap.values.filterNot { it.id in onlineSourceIds }
    }

    private fun registerStubSource(sourceData: MangaSourceData) {

    }

    private fun createStubSource(id: Long): StubMangaSource {
        return StubMangaSource(MangaSourceData(id, "", ""))
    }
}
