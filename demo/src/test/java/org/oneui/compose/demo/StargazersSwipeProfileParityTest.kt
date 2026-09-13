package org.oneui.compose.demo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oneui.compose.components.list.OneUiSwipeDirection
import org.oneui.compose.demo.screens.CatalogStargazer
import org.oneui.compose.demo.screens.CatalogStargazerProfileAction
import org.oneui.compose.demo.screens.CatalogStargazerSwipeAction
import org.oneui.compose.demo.screens.stargazerProfileActions
import org.oneui.compose.demo.screens.stargazerSwipeAction
import org.oneui.compose.demo.screens.stargazerVCardContent

class StargazersSwipeProfileParityTest {
    @Test
    fun physicalSwipeDirectionsMatchPinnedReferenceBehavior() {
        assertEquals(
            CatalogStargazerSwipeAction.Call,
            stargazerSwipeAction(OneUiSwipeDirection.Right),
        )
        assertEquals(
            CatalogStargazerSwipeAction.Message,
            stargazerSwipeAction(OneUiSwipeDirection.Left),
        )
        assertNull(stargazerSwipeAction(OneUiSwipeDirection.None))
    }

    @Test
    fun profileActionsFollowReferenceFieldVisibility() {
        val profile = CatalogStargazer(
            id = 42,
            name = "Ada Lovelace",
            login = "ada",
            url = "https://github.com/ada",
            location = "London, UK",
            company = "Analytical Engine",
            email = "ada@example.com",
            bio = "Mathematics and early computing",
            twitterUsername = "ada_lovelace",
            blog = "https://ada.example.com",
        )

        assertEquals(
            listOf(
                CatalogStargazerProfileAction.Website,
                CatalogStargazerProfileAction.Email,
                CatalogStargazerProfileAction.X,
                CatalogStargazerProfileAction.Blog,
            ),
            stargazerProfileActions(profile),
        )

        val sparse = profile.copy(email = "", twitterUsername = null, blog = "")
        assertEquals(
            listOf(CatalogStargazerProfileAction.Website),
            stargazerProfileActions(sparse),
        )
    }

    @Test
    fun vCardMatchesReferenceContactFieldsAndOmitsBlankOptionals() {
        val profile = CatalogStargazer(
            id = 7,
            name = "Grace Hopper",
            login = "grace",
            url = "https://github.com/grace",
            location = "Arlington, VA",
            company = "Compiler Systems",
            email = "grace@example.com",
            bio = "Compilers and programming languages",
            twitterUsername = "ghopper",
            blog = "https://grace.example.com",
            organizationsUrl = "https://api.github.com/users/grace/orgs",
            starredRepos = setOf("oneui-compose", "oneui-design"),
        )

        val vCard = stargazerVCardContent(profile)

        assertTrue(vCard.startsWith("BEGIN:VCARD\nVERSION:2.1\n"))
        assertTrue(vCard.contains("FN:Grace Hopper\n"))
        assertTrue(vCard.contains("NICKNAME:grace\n"))
        assertTrue(vCard.contains("URL:https://github.com/grace\n"))
        assertTrue(vCard.contains("EMAIL:grace@example.com\n"))
        assertTrue(vCard.contains("ORG:Compiler Systems\n"))
        assertTrue(vCard.contains("ADR:Arlington, VA\n"))
        assertTrue(vCard.contains("TITLE:Compilers and programming languages\n"))
        assertTrue(vCard.contains("URL:https://grace.example.com\n"))
        assertTrue(vCard.contains("URL:https://api.github.com/users/grace/orgs\n"))
        assertTrue(vCard.contains("X-TWITTER:https://x.com/ghopper\n"))
        assertTrue(vCard.contains("NOTE:Starred repos: oneui-compose, oneui-design\n"))
        assertTrue(vCard.endsWith("END:VCARD\n"))

        val sparse = profile.copy(
            email = "",
            company = "",
            location = "",
            bio = "",
            twitterUsername = "",
            blog = "",
            organizationsUrl = "",
            starredRepos = emptySet(),
        )
        val sparseVCard = stargazerVCardContent(sparse)
        assertFalse(sparseVCard.contains("EMAIL:"))
        assertFalse(sparseVCard.contains("ORG:"))
        assertFalse(sparseVCard.contains("ADR:"))
        assertFalse(sparseVCard.contains("TITLE:"))
        assertFalse(sparseVCard.contains("X-TWITTER:"))
        assertEquals(1, sparseVCard.lineSequence().count { it.startsWith("URL:") })
        assertTrue(sparseVCard.contains("NOTE:Starred repos: \n"))
    }
}
