package com.example.kot_start

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented integration tests that validate the app context,
 * package naming, and basic environment setup on a real device/emulator.
 */
@RunWith(AndroidJUnit4::class)
class AppContextTest {

    @Test
    fun appPackageNameIsCorrect() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.kot_start", appContext.packageName)
    }

    @Test
    fun appContextIsNotNull() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertNotNull(appContext)
    }

    @Test
    fun applicationInfoIsAccessible() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val appInfo = appContext.applicationInfo
        assertNotNull(appInfo)
    }

    @Test
    fun resourcesAreAccessible() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val resources = appContext.resources
        assertNotNull(resources)
    }

    @Test
    fun appVersionNameIsSet() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val packageInfo = appContext.packageManager.getPackageInfo(appContext.packageName, 0)
        assertEquals("1.0", packageInfo.versionName)
    }

    @Test
    fun targetSdkIsCorrect() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertTrue(appContext.applicationInfo.targetSdkVersion >= 24)
    }
}
