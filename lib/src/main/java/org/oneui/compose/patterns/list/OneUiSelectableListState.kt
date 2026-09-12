package org.oneui.compose.patterns.list

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf

/**
 * Snapshot-aware selection state for One UI list/action-mode patterns.
 *
 * Selection mode is derived from whether any selectable keys are active, matching the reference
 * list adapters where long-press enters action mode and ending action mode clears selection.
 */
@Stable
class OneUiSelectableListState<K> {
    private val selected = mutableStateMapOf<K, Unit>()

    val selectedKeys: Set<K>
        get() = selected.keys.toSet()

    val selectedCount: Int
        get() = selected.size

    val isSelectionMode: Boolean
        get() = selected.isNotEmpty()

    fun isSelected(key: K): Boolean = selected.containsKey(key)

    fun toggle(key: K) {
        if (selected.remove(key) == null) {
            selected[key] = Unit
        }
    }

    fun setSelected(key: K, isSelected: Boolean) {
        if (isSelected) {
            selected[key] = Unit
        } else {
            selected.remove(key)
        }
    }

    fun selectAll(keys: Iterable<K>) {
        selected.clear()
        keys.forEach { key -> selected[key] = Unit }
    }

    fun allSelected(keys: Iterable<K>): Boolean {
        val candidates = keys.toSet()
        return candidates.isNotEmpty() && selected.keys.containsAll(candidates)
    }

    fun retainKeys(validKeys: Set<K>) {
        selected.keys.toList().forEach { key ->
            if (key !in validKeys) selected.remove(key)
        }
    }

    fun clear() {
        selected.clear()
    }
}
