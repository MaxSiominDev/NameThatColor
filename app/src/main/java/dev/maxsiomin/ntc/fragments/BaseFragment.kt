package dev.maxsiomin.ntc.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.core.view.allViews
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    abstract val mRoot: View

    protected lateinit var imm: InputMethodManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onStart() {
        super.onStart()
        mRoot.setOnClickListener { root ->
            root.allViews.forEach { v -> v.clearFocus() }
            imm.hideSoftInputFromWindow(root.windowToken, 0)
        }
    }
}
