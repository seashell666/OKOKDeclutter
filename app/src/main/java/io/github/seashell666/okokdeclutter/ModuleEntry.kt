package io.github.seashell666.okokdeclutter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.util.Log
import android.view.View
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.PackageReadyParam
import java.util.concurrent.ConcurrentHashMap

@Suppress("unused")
class ModuleEntry : XposedModule() {
    private val resourceNames = ConcurrentHashMap<Int, String>()

    override fun onPackageReady(param: PackageReadyParam) {
        if (param.packageName != OkokRules.TARGET_PACKAGE) return

        val systemContext = currentSystemContext() ?: run {
            info("cannot read the host version; no hooks installed")
            return
        }
        val versionCode = readVersionCode(systemContext, param.packageName)
        if (!OkokRules.supportsVersion(versionCode ?: -1L)) {
            info(
                "unsupported OKOK version ${versionCode ?: "unknown"}; " +
                    "expected ${OkokRules.SUPPORTED_VERSION_CODE} " +
                    "(${OkokRules.SUPPORTED_VERSION_NAME}); no hooks installed",
            )
            return
        }

        installSplashBypass(param.classLoader)
        installViewHooks()
        info("active for OKOK ${OkokRules.SUPPORTED_VERSION_NAME} (255)")
    }

    /**
     * Keep OKOK's original login/account routing. The original splash loader still runs, then the
     * app's own doJump() immediately takes the same route used by its ad-complete callback.
     */
    private fun installSplashBypass(classLoader: ClassLoader) {
        runCatching {
            val initActivity = classLoader.loadClass("com.chipsea.btcontrol.InitActivity")
            val toLoad = initActivity.getDeclaredMethod("toLoad").apply { isAccessible = true }
            val doJump = initActivity.getDeclaredMethod("doJump").apply { isAccessible = true }

            hook(toLoad).intercept { chain ->
                val result = chain.proceed()
                val activity = chain.thisObject as? Activity
                if (activity != null) {
                    activity.runOnUiThread {
                        runCatching { doJump.invoke(activity) }
                            .onFailure { failure("splash jump failed", it) }
                    }
                }
                result
            }
        }.onFailure { failure("splash hook unavailable", it) }
    }

    private fun installViewHooks() {
        runCatching {
            val setVisibility = View::class.java.getMethod(
                "setVisibility",
                Int::class.javaPrimitiveType!!,
            )
            hook(setVisibility).intercept { chain ->
                val view = chain.thisObject as? View
                val requestedVisibility = chain.getArg(0) as Int
                if (view != null && requestedVisibility != View.GONE && shouldHide(view)) {
                    chain.proceed(arrayOf<Any?>(View.GONE))
                } else {
                    chain.proceed()
                }
            }
        }.onFailure { failure("visibility hook unavailable", it) }

        runCatching {
            val onAttached = View::class.java.getDeclaredMethod("onAttachedToWindow")
                .apply { isAccessible = true }
            hook(onAttached).intercept { chain ->
                val result = chain.proceed()
                val view = chain.thisObject as? View
                if (view != null && shouldHide(view)) view.visibility = View.GONE
                result
            }
        }.onFailure { failure("attach hook unavailable", it) }
    }

    private fun shouldHide(view: View): Boolean = runCatching {
        if (view.id == View.NO_ID) return@runCatching false
        val activity = findActivity(view.context) ?: return@runCatching false
        val resourceName = resourceNames[view.id] ?: view.resources.getResourceEntryName(view.id)
            .also { resourceNames[view.id] = it }
        OkokRules.shouldHideResource(resourceName, activity.javaClass.name)
    }.getOrDefault(false)

    private fun findActivity(context: Context): Activity? {
        var current: Context = context
        var depth = 0
        while (depth < 8) {
            when (val candidate = current) {
                is Activity -> return candidate
                is ContextWrapper -> {
                    val base = candidate.baseContext
                    if (base === current) return null
                    current = base
                }
                else -> return null
            }
            depth++
        }
        return null
    }

    @Suppress("DEPRECATION")
    private fun readVersionCode(context: Context, packageName: String): Long? = runCatching {
        val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }
    }.onFailure { failure("failed to read host version", it) }.getOrNull()

    @SuppressLint("PrivateApi") // The system context exists before the host Application is created.
    private fun currentSystemContext(): Context? = runCatching {
        val activityThread = Class.forName("android.app.ActivityThread")
            .getMethod("currentActivityThread")
            .invoke(null)
        activityThread?.javaClass?.getMethod("getSystemContext")
            ?.invoke(activityThread) as? Context
    }.getOrNull()

    private fun info(message: String) = log(Log.INFO, LOG_TAG, message)

    private fun failure(message: String, error: Throwable) =
        log(Log.ERROR, LOG_TAG, message, error)

    private companion object {
        const val LOG_TAG = "OKOKDeclutter"
    }
}
