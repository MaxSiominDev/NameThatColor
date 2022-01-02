package dev.maxsiomin.ntc.extensions

import androidx.fragment.app.Fragment
import dev.maxsiomin.ntc.activity.MainActivity

val Fragment.dataBundle get() = (activity as MainActivity).viewModel.dataBundle
