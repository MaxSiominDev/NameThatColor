package dev.maxsiomin.ntc.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.ntc.util.UiActions
import javax.inject.Inject

/**
 * If a fragment or an activity uses only UiActions impl, don't create custom viewModel for it, use this
 */
@HiltViewModel
open class BaseViewModel @Inject constructor(uiActions: UiActions) : ViewModel(), UiActions by uiActions

fun stringMutableLiveData(value: String? = null): MutableLiveData<String> {
    return MutableLiveData<String>().apply {
        if (value != null)
            this.value = value
    }
}

fun intMutableLiveData(value: Int? = null): MutableLiveData<Int> {
    return MutableLiveData<Int>().apply {
        if (value != null)
            this.value = value
    }
}

