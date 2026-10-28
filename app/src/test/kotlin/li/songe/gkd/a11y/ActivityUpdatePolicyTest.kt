package li.songe.gkd.a11y

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ActivityUpdatePolicyTest {
    private val launcherAppId = "com.miui.home"
    private val adAppId = "com.example.ad"

    @Test
    fun `window correction is not deferred after transient launcher state`() {
        assertFalse(
            shouldDeferA11yActivityUpdate(
                currentAppId = launcherAppId,
                newAppId = adAppId,
                newActivityId = "com.example.ad.SplashActivity",
                launcherAppId = launcherAppId,
                elapsedSinceTaskStackUpdate = 100,
            )
        )
    }

    @Test
    fun `task stack keeps priority during normal app switch`() {
        assertTrue(
            shouldDeferA11yActivityUpdate(
                currentAppId = adAppId,
                newAppId = launcherAppId,
                newActivityId = "com.miui.home.launcher.Launcher",
                launcherAppId = launcherAppId,
                elapsedSinceTaskStackUpdate = 100,
            )
        )
    }

    @Test
    fun `activity update remains deferred between one and three seconds`() {
        assertTrue(
            shouldDeferA11yActivityUpdate(
                currentAppId = adAppId,
                newAppId = "com.example.other",
                newActivityId = "com.example.other.MainActivity",
                launcherAppId = launcherAppId,
                elapsedSinceTaskStackUpdate = 1500,
            )
        )
    }

    @Test
    fun `app only update is accepted after one second`() {
        assertFalse(
            shouldDeferA11yActivityUpdate(
                currentAppId = adAppId,
                newAppId = "com.example.other",
                newActivityId = null,
                launcherAppId = launcherAppId,
                elapsedSinceTaskStackUpdate = 1500,
            )
        )
    }

    @Test
    fun `cached launcher id is not reused for ad app event`() {
        assertFalse(canUseCachedAppId(launcherAppId, adAppId, cacheAge = 50))
    }

    @Test
    fun `fresh cache is reused only for matching app event`() {
        assertTrue(canUseCachedAppId(adAppId, adAppId, cacheAge = 50))
        assertFalse(canUseCachedAppId(adAppId, adAppId, cacheAge = 101))
    }
}
