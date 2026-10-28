package li.songe.gkd.a11y

internal fun shouldDeferA11yActivityUpdate(
    currentAppId: String,
    newAppId: String,
    newActivityId: String?,
    launcherAppId: String,
    elapsedSinceTaskStackUpdate: Long,
): Boolean {
    // 任务栈可能把仍在显示的广告页短暂上报为桌面，窗口确认回原应用时应立即纠偏
    if (currentAppId == launcherAppId && newAppId != launcherAppId) return false
    if (elapsedSinceTaskStackUpdate < 1000) return true
    return newActivityId != null && elapsedSinceTaskStackUpdate < 3000
}

internal fun canUseCachedAppId(
    cachedAppId: String?,
    eventAppId: String,
    cacheAge: Long,
): Boolean {
    return cachedAppId == eventAppId && cacheAge <= 100
}
