package com.github.olegzuev.yukarinotes.common

import android.content.Context
import com.github.olegzuev.yukarinotes.user.UserSettings
import com.github.olegzuev.yukarinotes.utils.LogUtils

class CrashManager(
    private val context: Context,
    private val defaultCrashHandler: Thread.UncaughtExceptionHandler?
) : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        LogUtils.file(LogUtils.E, e::class.simpleName + ":" + e.message, e.stackTrace)
        UserSettings.get().setAbnormalExit(true)
        Thread.sleep(100)
        defaultCrashHandler?.uncaughtException(t, e)
    }
}