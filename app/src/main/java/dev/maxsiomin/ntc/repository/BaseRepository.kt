package dev.maxsiomin.ntc.repository

import dev.maxsiomin.ntc.extensions.notNull
import dev.maxsiomin.ntc.util.UiActions

/**
 * All repositories in project must extend this class
 */
abstract class BaseRepository(uiActions: UiActions) : UiActions by uiActions {

    companion object {

        private const val CONNECTION_ERROR = "unable to resolve host"

        @JvmStatic
        fun isConnectionError(error: String?) = CONNECTION_ERROR in error.notNull().lowercase()
    }
}

