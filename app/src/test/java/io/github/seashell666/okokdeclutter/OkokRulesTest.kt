package io.github.seashell666.okokdeclutter

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OkokRulesTest {
    private val init = "com.chipsea.btcontrol.InitActivity"
    private val main = "com.chipsea.btcontrol.homePage.NewMainActivity"
    private val report = "com.chipsea.btcontrol.homePage.ReportActivity"
    private val bodyReport = "com.chipsea.btcontrol.homePage.BodyMeasureReportActivity"

    @Test
    fun `only the analysed OKOK build is supported`() {
        assertTrue(OkokRules.supportsVersion(255L))
        assertFalse(OkokRules.supportsVersion(254L))
        assertFalse(OkokRules.supportsVersion(256L))
    }

    @Test
    fun `all requested home elements are hidden`() {
        listOf("banner_card", "food_banner_view", "ai_float_entry", "pop_suspension_bottom_ll")
            .forEach { assertTrue(it, OkokRules.shouldHideResource(it, main)) }
    }

    @Test
    fun `splash and report advertising containers are hidden in their own activities`() {
        assertTrue(OkokRules.shouldHideResource("ll_container", init))
        assertTrue(OkokRules.shouldHideResource("containerLayout", report))
        assertTrue(OkokRules.shouldHideResource("containerLayout", bodyReport))
    }

    @Test
    fun `animated status guide is hidden but its edit control remains`() {
        assertTrue(OkokRules.shouldHideResource("body_status_edit_guide_hint_booble", report))
        assertFalse(OkokRules.shouldHideResource("body_state_edit_bt", report))
    }

    @Test
    fun `same resource names in unrelated screens are not touched`() {
        assertFalse(OkokRules.shouldHideResource("containerLayout", main))
        assertFalse(OkokRules.shouldHideResource("banner_card", report))
        assertFalse(OkokRules.shouldHideResource("food_banner_view", init))
    }

    @Test
    fun `normal health and navigation controls remain visible`() {
        listOf("ai_card", "weight_card", "diet_card", "tab_layout", "body_state_edit_bt")
            .forEach { assertFalse(it, OkokRules.shouldHideResource(it, main)) }
    }
}
