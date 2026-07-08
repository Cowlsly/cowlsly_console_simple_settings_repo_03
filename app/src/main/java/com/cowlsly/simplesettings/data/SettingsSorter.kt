package com.cowlsly.simplesettings.data

object SettingsSorter {
    /**
     * Lower sort key = closer to the front (page 1).
     * Usage and search frequency pull items toward the start over time.
     */
    fun sort(
        entries: List<SettingsEntry>,
        usageScores: Map<String, Int>,
        developerAccessGranted: Boolean,
    ): List<SettingsEntry> {
        return entries
            .filter { entry ->
                when {
                    entry.panelType == SettingsPanelType.CREDITS -> true
                    entry.requiresDeveloperAccess && !developerAccessGranted -> false
                    else -> true
                }
            }
            .sortedBy { entry ->
                val usageBoost = usageScores[entry.id] ?: 0
                // Credits always last; about near end
                val tailPenalty = when (entry.panelType) {
                    SettingsPanelType.CREDITS -> 10_000
                    SettingsPanelType.ABOUT -> 9_000
                    else -> 0
                }
                entry.basePriority * 100 - usageBoost + tailPenalty
            }
    }

    /** Web console — always alphabetical by title. Credits last, About second-last. */
    fun sortAlphabetical(
        entries: List<SettingsEntry>,
        developerAccessGranted: Boolean,
    ): List<SettingsEntry> {
        return entries
            .filter { entry ->
                when {
                    entry.requiresDeveloperAccess && !developerAccessGranted -> false
                    else -> true
                }
            }
            .sortedWith(
                compareBy<SettingsEntry> { entry ->
                    when (entry.panelType) {
                        SettingsPanelType.CREDITS -> 2
                        SettingsPanelType.ABOUT -> 1
                        else -> 0
                    }
                }.thenBy { it.title.lowercase() },
            )
    }

    fun paginate(entries: List<SettingsEntry>, pageSize: Int): List<List<SettingsEntry>> {
        if (entries.isEmpty()) return emptyList()
        return entries.chunked(pageSize)
    }
}