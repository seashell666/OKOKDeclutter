package io.github.seashell666.okokdeclutter

/** Pure, version-specific rules. Keeping this free of Android types makes the scope easy to audit. */
object OkokRules {
    const val TARGET_PACKAGE = "com.chipsea.btcontrol"
    const val SUPPORTED_VERSION_CODE = 255L
    const val SUPPORTED_VERSION_NAME = "3.7.63"

    private const val INIT_ACTIVITY = "com.chipsea.btcontrol.InitActivity"
    private const val MAIN_ACTIVITY = "com.chipsea.btcontrol.homePage.NewMainActivity"
    private const val REPORT_ACTIVITY = "com.chipsea.btcontrol.homePage.ReportActivity"
    private const val BODY_MEASURE_REPORT_ACTIVITY =
        "com.chipsea.btcontrol.homePage.BodyMeasureReportActivity"

    private val homeViews = setOf(
        "banner_card",
        "food_banner_view",
        "ai_float_entry",
        "pop_suspension_bottom_ll",
    )

    fun supportsVersion(versionCode: Long): Boolean = versionCode == SUPPORTED_VERSION_CODE

    fun shouldHideResource(resourceName: String, activityClassName: String): Boolean = when {
        activityClassName == INIT_ACTIVITY && resourceName == "ll_container" -> true
        activityClassName == MAIN_ACTIVITY && resourceName in homeViews -> true
        activityClassName == REPORT_ACTIVITY &&
            resourceName == "body_status_edit_guide_hint_booble" -> true
        (activityClassName == REPORT_ACTIVITY || activityClassName == BODY_MEASURE_REPORT_ACTIVITY) &&
            resourceName == "containerLayout" -> true
        else -> false
    }
}
