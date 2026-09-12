package org.oneui.compose.parity

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReferenceManifestTest {
    private val repoRoot: File by lazy(::findRepoRoot)

    @Test
    fun requiredReferenceManifestsExistAndContainUniqueEntries() {
        REQUIRED_MANIFESTS.forEach { relativePath ->
            val manifest = File(repoRoot, relativePath)
            assertTrue("Missing parity manifest: $relativePath", manifest.isFile)

            val ids = readManifestIds(manifest)
            assertTrue("Parity manifest must not be empty: $relativePath", ids.isNotEmpty())
            assertEquals(
                "Duplicate IDs in parity manifest: $relativePath",
                ids.size,
                ids.toSet().size,
            )
        }
    }

    @Test
    fun routesManifestContainsEveryReferenceTopLevelDestination() {
        val routes = File(repoRoot, "reference/oneui8-demo/routes.txt")
        assertTrue("Missing routes parity manifest", routes.isFile)

        val routeIds = readManifestIds(routes).toSet()
        REQUIRED_TOP_LEVEL_ROUTES.forEach { route ->
            assertTrue("Missing reference demo route: $route", route in routeIds)
        }
    }

    private fun readManifestIds(file: File): List<String> = file
        .readLines()
        .map(String::trim)
        .filter { it.isNotEmpty() && !it.startsWith("#") }
        .map { line ->
            val id = line.substringBefore('|').trim()
            require(id.isNotEmpty()) { "Manifest entry has no ID: $line" }
            id
        }

    private fun findRepoRoot(): File {
        var current: File? = File(System.getProperty("user.dir")).absoluteFile
        while (current != null) {
            if (File(current, "settings.gradle.kts").isFile) return current
            current = current.parentFile
        }
        error("Could not locate oneui-compose repository root")
    }

    private companion object {
        val REQUIRED_MANIFESTS = listOf(
            "reference/oneui8-demo/routes.txt",
            "reference/oneui8-demo/components.txt",
            "reference/oneui8-demo/drawables.txt",
            "reference/oneui8-demo/motion.txt",
        )

        val REQUIRED_TOP_LEVEL_ROUTES = setOf(
            "ProgressBars",
            "SeekBars",
            "Pickers",
            "QRCodes",
            "Navigation",
            "RecyclerViews",
            "MiscWidgets",
            "CustomAbout",
        )
    }
}
