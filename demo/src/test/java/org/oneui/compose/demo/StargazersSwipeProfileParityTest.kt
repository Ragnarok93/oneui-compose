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
import org.oneui.compose.demo.screens.stargazerProfileActionTarget
import org.oneui.compose.demo.screens.stargazerProfileActions
import org.oneui.compose.demo.screens.stargazerSwipeAction
import org.oneui.compose.demo.screens.stargazerSwipeFeedback
import org.oneui.compose.demo.screens.stargazerVCardContent
import org.oneui.compose.demo.screens.stargazerVCardFileName

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
    fun swipeActionsCarryReferenceLabelsColorsAndProgressBehavior() {
        assertEquals("Call", CatalogStargazerSwipeAction.Call.label)
        assertEquals(0xFF11A85F.toInt(), CatalogStargazerSwipeAction.Call.containerArgb)
        assertEquals("Message", CatalogStargazerSwipeAction.Message.label)
        assertEquals(0xFF31A5F3.toInt(), CatalogStargazerSwipeAction.Message.containerArgb)

        val call = stargazerSwipeFeedback(CatalogStargazerSwipeAction.Call, "Ada Lovelace")
        assertEquals("Calling Ada Lovelace...", call.message)
        assertTrue(call.indeterminate)
        assertEquals(4_000L, call.dismissAfterMillis)
        assertNull(call.progressStepDelayMillis)
        assertNull(call.progressStepCount)

        val message = stargazerSwipeFeedback(CatalogStargazerSwipeAction.Message, "Ada Lovelace")
        assertEquals("Sending message to Ada Lovelace...", message.message)
        assertFalse(message.indeterminate)
        assertNull(message.dismissAfterMillis)
        assertEquals(50L, message.progressStepDelayMillis)
        assertEquals(101, message.progressStepCount)
    }

    @Test
    fun profileActionsMatchReferenceOrderAndNullableVisibility() {
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
                CatalogStargazerProfileAction.GitHub,
                CatalogStargazerProfileAction.X,
                CatalogStargazerProfileAction.Email,
                CatalogStargazerProfileAction.Blog,
            ),
            stargazerProfileActions(profile),
        )

        assertEquals("https://github.com/ada", stargazerProfileActionTarget(profile, CatalogStargazerProfileAction.GitHub))
        assertEquals("https://x.com/ada_lovelace", stargazerProfileActionTarget(profile, CatalogStargazerProfileAction.X))
        assertEquals("mailto:ada@example.com", stargazerProfileActionTarget(profile, CatalogStargazerProfileAction.Email))
        assertEquals("https://ada.example.com", stargazerProfileActionTarget(profile, CatalogStargazerProfileAction.Blog))

        val sparse = profile.copy(email = null, twitterUsername = null, blog = "")
        assertEquals(
            listOf(CatalogStargazerProfileAction.GitHub),
            stargazerProfileActions(sparse),
        )
        assertNull(stargazerProfileActionTarget(sparse, CatalogStargazerProfileAction.X))
        assertNull(stargazerProfileActionTarget(sparse, CatalogStargazerProfileAction.Email))
        assertNull(stargazerProfileActionTarget(sparse, CatalogStargazerProfileAction.Blog))
    }

    @Test
    fun vCardMatchesReferenceContactFieldsAndOmitsNullOptionals() {
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

        assertEquals("stargazer_7_Grace Hopper.vcf", stargazerVCardFileName(profile))

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
            email = null,
            company = null,
            location = null,
            bio = null,
            twitterUsername = null,
            blog = null,
            organizationsUrl = null,
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
