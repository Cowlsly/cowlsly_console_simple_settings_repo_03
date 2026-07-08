package com.cowlsly.simplesettings.data

enum class SettingsRankHint {
    HOT,
    WAITING,
    NORMAL,
}

object SettingsSorter {
    private const val HOT_USAGE_THRESHOLD = 6
    private const val WAITING_NEGLECT_THRESHOLD = 20

    /**
     * App list order:
     * 1. Highest base priority first (P1 closest … P13 credits last)
     * 2. Within the same priority zone, most-used first (open×3 + search×2)
     * 3. Long-neglected panels gently float up as a light tie-breaker
     * 4. About / Credits always stay at the tail
     *
     * Every non-developer catalog entry is always available. Developer-gated
     * rows appear only after access is granted (still PIN-protected on open).
     */
    fun sort(
        entries: List<SettingsEntry>,
        usageScores: Map<String, Int>,
        neglectScores: Map<String, Int>,
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
            .sortedWith(
                compareBy<SettingsEntry> { entry ->
                    when (entry.panelType) {
                        SettingsPanelType.CREDITS -> 2
                        SettingsPanelType.ABOUT -> 1
                        else -> 0
                    }
                }
                    .thenBy { it.basePriority }
                    .thenByDescending { usageScores[it.id] ?: 0 }
                    .thenByDescending { neglectScores[it.id] ?: 0 }
                    .thenBy { it.title.lowercase() },
            )
    }

    fun rankHint(
        entryId: String,
        usageScores: Map<String, Int>,
        neglectScores: Map<String, Int>,
    ): SettingsRankHint {
        val usage = usageScores[entryId] ?: 0
        val neglect = neglectScores[entryId] ?: 0
        return when {
            usage >= HOT_USAGE_THRESHOLD -> SettingsRankHint.HOT
            neglect >= WAITING_NEGLECT_THRESHOLD -> SettingsRankHint.WAITING
            else -> SettingsRankHint.NORMAL
        }
    }

    fun rankHints(
        entries: List<SettingsEntry>,
        usageScores: Map<String, Int>,
        neglectScores: Map<String, Int>,
    ): Map<String, SettingsRankHint> = entries.associate { entry ->
        entry.id to rankHint(entry.id, usageScores, neglectScores)
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

    fun zoneLabel(basePriority: Int): String = when (basePriority) {
        1 -> "Closest"
        2 -> "Sound & display"
        3 -> "Security"
        4 -> "Privacy"
        5 -> "Personal"
        6 -> "Network"
        7 -> "Device"
        8 -> "Apps"
        9 -> "Notifications"
        10 -> "Accounts"
        11 -> "Developer"
        12 -> "About"
        13 -> "Credits"
        else -> "Settings"
    }
}