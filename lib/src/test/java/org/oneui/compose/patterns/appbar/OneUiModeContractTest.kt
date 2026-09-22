package org.oneui.compose.patterns.appbar

import org.junit.Assert.assertEquals
import org.junit.Test

class OneUiModeContractTest {
    @Test
    fun dismissSearchClearsQueryWhileConcurrentSearchPreservesIt() {
        val dismiss = OneUiSearchModeState(
            active = true,
            query = "Ada",
            behavior = OneUiSearchModeBehavior.Dismiss,
        ).close()
        val concurrent = OneUiSearchModeState(
            active = true,
            query = "Ada",
            behavior = OneUiSearchModeBehavior.Concurrent,
        ).close()

        assertEquals("", dismiss.query)
        assertEquals("Ada", concurrent.query)
    }

    @Test
    fun reopeningAndQueryUpdatesArePureStateTransitions() {
        val state = OneUiSearchModeState(behavior = OneUiSearchModeBehavior.NoDismiss)
            .open()
            .updateQuery("Grace")

        assertEquals(true, state.active)
        assertEquals("Grace", state.query)
        assertEquals(OneUiSearchModeBehavior.NoDismiss, state.behavior)
    }
}

